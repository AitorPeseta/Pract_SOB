/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;


import authn.Credentials;
import authn.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import model.entities.Customer;
import java.util.Date;



@Path("customer")

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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllCustomers() {
        // Usamos la Named Query para recuperar los datos
        List<Customer> customers = em.createNamedQuery("Customer.findAllWithoutSensitiveData", Customer.class)
                                     .getResultList();

        // Crear la lista de usuarios para respuesta sin datos sensibles
        List<Customer> customerResponses = customers.stream()
            .map(customer ->{
            // Crear una nueva instancia de Customer y copiar los datos, excepto la contraseña
            Customer newCustomer = new Customer(
                customer.getId(), 
                customer.getCredenciales(), 
                customer.getEmail(), 
                customer.getIsAuthor(), 
                customer.getLastArticleId(), 
                customer.getRegistrationDate()
            );
            newCustomer.getCredenciales().setPassword("null");
            return newCustomer;
            })
            .collect(Collectors.toList());

        return Response.ok(customerResponses).build();
    }
    
    /**
     * GET /rest/api/v1/customer/${id}

Retorna la informació de l'usuari amb identificador ${id}. 

Aquesta crida no pot retornar informació confidencial, p. ex., la  contrasenya d'aquest usuari.
     */
    
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCustomerId(@PathParam("id") String ids){
        int id;
        try{
            id = Integer.parseInt(ids);
        } catch (NumberFormatException e2){
            return Response.status(Response.Status.BAD_REQUEST).entity("Introduce una ID numerica valida").build();
        }
        try{
            if (id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
            }
            Customer customer = em.createNamedQuery("Customer.findCustomerWithoutSensitiveData", Customer.class).setParameter("id",id).getSingleResult();

            customer.getCredenciales().setPassword("null");

            if(customer == null) return Response.status(Response.Status.BAD_REQUEST).entity("Invalid id author").build();
            else return Response.ok(customer).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente no encontrado.").build();
        }
      
    }
    
    /**
     * PUT /rest/api/v1/customer/${id}

Opcional! Modifica les dades del client amb identificador ${id} al sistema amb les dades JSON/XML proporcionades.Per aquesta operació, cal que el client estigui autentificat.

     */
    
    @PUT
    @Path("/{id}")
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {

        Customer currentCustomer = em.find(Customer.class, id);
        if (currentCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cliente no encontrado.").build();
        }

        if (id != updatedCustomer.getId()) {
            return Response.status(Response.Status.FORBIDDEN).entity("No tienes permisos para modificar este cliente.").build();
        }

        // Actualizar los campos permitidos del cliente
        currentCustomer.setEmail(updatedCustomer.getEmail());
        currentCustomer.setIsAuthor(updatedCustomer.getIsAuthor());
        currentCustomer.setRegistrationDate(updatedCustomer.getRegistrationDate());
        Credentials creds = updatedCustomer.getCredenciales();
            // Actualiza la relación de credenciales (si es necesario)
        if (creds != null) {
            currentCustomer.setCredenciales(creds);
        }
        
        // Guardar cambios en la base de datos
        em.merge(currentCustomer);

        return Response.ok("Cliente actualizado correctamente.").build();
    }
}
