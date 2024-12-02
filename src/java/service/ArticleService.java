/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.entities.Article;
import model.entities.Customer;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;
import javax.annotation.security.RolesAllowed;
import java.util.Calendar;
import java.util.Date;
import model.entities.Topic;

@Path("article")


/**
 *
 * @author aito
 */
public class ArticleService {
    
    
    /**
     * GET /rest/api/v1/article?topic=${topic}&author=${author}

Per defecte, proporciona un llistat JSON amb tots els articles ordenats per popularitat de forma descendent. La informació dels articles és la que es correspon a la Figura 1.

Els paràmetres "topic" i "author" són opcionals.

Si s'especifica el paràmetre "topic", només s'inclouran en el llistat els articles del tipus ${topic}. Es podrà especificar un màxim de dos topics en l'atribut topic, i per tant la part de consulta de l'URL podria ser del tipus: topic=${topic1}&topic=${topic2}&author=${author}

Si s'especifica el paràmetre "author", només s'inclouran en el llistat els articles de l'autor ${author}.

El filtratge s'ha de fer mitjançant una consulta a la base de dades. No s'acceptarà com a vàlid retornar el llistat de tots els articles i filtrar-los de forma programàtica amb Java.
     */
    
    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticles(@QueryParam("topic") List<String> topics,
                                @QueryParam("author") String author) {
        // Verificación del número de topics
        if (topics != null && topics.size() > 2) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pueden especificar más de dos topics.").build();
        }

        // Caso 1: No hay filtros, usar findAllPublic
        if ((topics == null || topics.isEmpty()) && (author == null || author.isEmpty())) {
            TypedQuery<Article> query = em.createNamedQuery("Article.findAllPublic", Article.class);
            List<Article> articles = query.getResultList();
            return Response.ok(articles).build();
        }

        // Caso 2: Filtros aplicados, usar findByAuthorAndTopics
        TypedQuery<Article> query = em.createNamedQuery("Article.findByAuthor", Article.class);
        TypedQuery<Topic> query2 = em.createNamedQuery("Topic.existTopic", Topic.class);
        TypedQuery<Topic> query3 = em.createNamedQuery("Topic.existTopic", Topic.class);

        query2.setParameter("topic1", topics != null && !topics.isEmpty() ? topics.get(0) : null);
        query3.setParameter("topic2", topics != null && topics.isEmpty() ? topics.get(1) : null);
        
        int num1 = query2.getFirstResult();
        int num2 = query3.getFirstResult();
        
        if(num1 == 0 || num2 == 0 ) return Response.noContent().build(); 
            
        // Asignación de parámetros, si no existen se asigna a null para no aplicarlos en la consulta
        query.setParameter("author", author != null && !author.isEmpty() ? author : null);
        query.setParameter("topic1", topics != null && !topics.isEmpty() ? topics.get(0) : null);
        query.setParameter("topic2", topics != null && topics.isEmpty() ? topics.get(1) : null);

        List<Article> articles = query.getResultList();
        return Response.ok(articles).build();
    }
    
    
    /**
     * GET /rest/api/v1/article/${id}

Retorna l'article sencer a partir de l'identificador de l'article, és a dir, tota la informació indicada en la Figura 2. 

Si l'article és privat, només es podrà retornar si l'usuari està registrat.

Aquesta operació implica augmentar el nombre de visualitzacions de l'article en +1. 
     */
    
    @GET
    @Path("/{id}")
    @Transactional
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticleId(@PathParam("id") int id){
        
        
        // Obtener el artículo de la base de datos usando su ID
        Article article = em.createNamedQuery("Article.findArticleId", Article.class).setParameter("id",id).getSingleResult();
        
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Article not found").build();
        }

        // Verificar si el artículo es privado
        if (!article.getIsPublic()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Usuario no autenticado.").build();
        }

        // Incrementar el contador de vistas
        article.incrementViews();

        return Response.ok(article).build();
    }
    
    
    /**
     * DELETE /rest/api/v1/article/${id}
Opcional! Esborra l'article amb identificador ${id} del sistema.Per aquesta operació, cal que l'usuari sigui l'autor de l'article i estigui autentificat.
     * @param id
     * @param securityContext
     * @return 
     */
    @DELETE
    @Path("/{id}")
    @Secured
    @Transactional
    public Response deleteArticle(@PathParam("id") int id) {

        // Buscar el artículo en la base de datos
        Article article = em.find(Article.class, id);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Artículo no encontrado.").build();
        }

        // Verificar que el usuario autenticado es el autor del artículo
        String username = article.getAuthor().getCredenciales().getUsername(); // supondremos que este es el nombre del usuario autenticado
        if (!article.getAuthor().getCredenciales().getUsername().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).entity("No tienes permisos para eliminar este artículo.").build();
        }

        // Eliminar el artículo
        em.remove(article);
        return Response.noContent().build();
    }
    /**
     * POST/rest/api/v1/article

Permetre afegir un article nou a partir de les dades proporcionades en format JSON/XML.

La data de la publicació es determinarà de forma automàtica i s'haurà de validar que els tòpics escollits siguin vàlids i que l'usuari existeixi. 

Cal retornar el codi HTTP 201 Created en cas afirmatiu juntament amb l'identificador de l'article.

Per aquesta operació, cal que l'usuari estigui autentificat.
     */
    
    @POST
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response createArticle(Article article) {
        
        em.persist(article);
        
        return Response.status(Response.Status.CREATED)
                .entity("Article created with ID: " + article.getId())
                .build();
    }
}
