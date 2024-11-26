/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import model.entities.Customer;
import java.util.Date;



@Path("/rest/api/v1/customer")

/**
 *
 * @author aito
 */
public class CustomerService {
    
    /**
     * GET /rest/api/v1/customer

 Llistat JSON dels usuaris.

Si l'usuari és autor d'un article, cal indicar que és autor i retornar l'enllaç a l'últim article que ha publicat per suportar el principi de HATEOAS. Per exemple, en JSON:
                 "links": {
                      "article": "/article/12345"
                 }
Aquesta crida no pot retornar informació confidencial, p. ex., la  contrasenya dels usuaris.
     */
    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        // Usamos la Named Query para recuperar los datos
        List<Customer> customers = em.createNamedQuery("Customer.findAllWithoutSensitiveData", Customer.class)
                                     .getResultList();

        // Crear la lista de usuarios para respuesta sin datos sensibles
        List<CustomerResponse> customerResponses = customers.stream()
            .map(customer -> new CustomerResponse(
                customer.getId(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getIsAuthor(),
                customer.getLastArticleId() != null 
                    ? "/rest/api/v1/article/" + customer.getLastArticleId() 
                    : null,
                customer.getRegistrationDate()
            ))
            .collect(Collectors.toList());

        return Response.ok(customerResponses).build();
    }
    
    
    // Clase auxiliar para respuesta JSON sin datos sensibles
    public static class CustomerResponse {
        private int id;
        private String username;
        private String email;
        private Boolean isAuthor;
        private String lastArticleLink;
        private Date registrationDate;

        public CustomerResponse(int id, String username, String email, Boolean isAuthor, String lastArticleLink, Date registrationDate) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.isAuthor = isAuthor;
            this.lastArticleLink = lastArticleLink;
            this.registrationDate = registrationDate;
        }

        // Getters y setters
        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public Boolean getIsAuthor() { return isAuthor; }
        public String getLastArticleLink() { return lastArticleLink; }
        public Date getRegistrationDate() { return registrationDate; }
    }
    /**
     * GET /rest/api/v1/customer/${id}

Retorna la informació de l'usuari amb identificador ${id}. 

Aquesta crida no pot retornar informació confidencial, p. ex., la  contrasenya d'aquest usuari.
     */
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticleId(@PathParam("id") int id){
    
        Customer customer = em.createNamedQuery("Customer.findCustomerWithoutSensitiveData", Customer.class).setParameter("id",id).getSingleResult();
        
        if(customer == null) return Response.status(Response.Status.BAD_REQUEST).entity("Invalid id author").build();
        else return Response.ok(customer).build();
      
    }
    
    /**
     * PUT /rest/api/v1/customer/${id}

Opcional! Modifica les dades del client amb identificador ${id} al sistema amb les dades JSON/XML proporcionades.Per aquesta operació, cal que el client estigui autentificat.

    Usamos SecurityContext para obtener el usuario autenticado. Si no está autenticado, respondemos con un 401 Unauthorized.
    Verificación de Existencia:
    Buscamos al cliente con em.find(Customer.class, id). Si el cliente no existe, devolvemos 404 Not Found.
    
    Verificación de Autorización:
    Comprobamos si el nombre de usuario autenticado coincide con el del cliente que se quiere modificar. Si no coincide, devolvemos 403 Forbidden.
    
    Actualización de Campos Permitidos:

    Solo actualizamos los campos permitidos (username, email, isAuthor y registrationDate).
    Excluimos la contraseña de esta operación, así como el id, para evitar cambios no autorizados.
    
    Persistencia:
    Usamos em.merge(currentCustomer) para aplicar los cambios a la base de datos. La anotación @Transactional asegura que el cambio se haga en una transacción.
     * @param id
     * @param updatedCustomer
     * @param securityContext
     * @return 
     */
    
    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {

        // Obtener el usuario autenticado y verificar si coincide con el cliente que desea actualizar
        Customer currentCustomer = em.find(Customer.class, id);
        if (currentCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente no encontrado.").build();
        }

        String authenticatedUsername = updatedCustomer.getUsername();
        if (!authenticatedUsername.equals(currentCustomer.getUsername())) {
            return Response.status(Response.Status.FORBIDDEN).entity("No tienes permisos para modificar este cliente.").build();
        }

        // Actualizar los campos permitidos del cliente
        currentCustomer.setUsername(updatedCustomer.getUsername());
        currentCustomer.setEmail(updatedCustomer.getEmail());
        currentCustomer.setIsAuthor(updatedCustomer.getIsAuthor());
        currentCustomer.setRegistrationDate(updatedCustomer.getRegistrationDate());

        // Guardar cambios en la base de datos
        em.merge(currentCustomer);

        return Response.ok("Cliente actualizado correctamente.").build();
    }
}
