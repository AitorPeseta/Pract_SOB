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
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.entities.Article;
import jakarta.transaction.Transactional;
import java.util.Date;
import model.entities.Customer;
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getArticles(@QueryParam("topic") List<Integer>topics,
                                @QueryParam("author") String authors) {
        
        
        TypedQuery<Article> query = null;
        if (topics.size()>2) { return Response.status(Response.Status.BAD_REQUEST).entity("Introduce como maximo 2 topics").build(); }
        int topicid1, topicid2;
        try {
            topicid1 = topics.get(0);
        } catch (Exception e){
            topicid1 = 0;
        }
        try {
            topicid2 = topics.get(1);
        } catch (Exception e){
            topicid2 = 0;
        }
        
        int author;
        try{
            if(authors != null)
                author = Integer.parseInt(authors);
            else 
                author = 0;
        } catch (NumberFormatException e2){
            return Response.status(Response.Status.BAD_REQUEST).entity("Introduce una ID numerica valida").build();
        }
        
        if ((topicid1 == 0 && topicid2 == 0) && (author == 0)) { // Caso 1: No filtros
            query= em.createNamedQuery("Article.findAll", Article.class);
            List<Article> articles = query.getResultList();
            return Response.ok(articles).build();
        } else if ((topicid1 == 0 && topicid2 == 0) && (author != 0)){  //Caso 2 solo 1 autor
            query = em.createNamedQuery("Article.findByAuthor", Article.class).setParameter("author", author);
        } else if ((topicid1 != 0 && topicid2 == 0) && (author == 0)){  //Caso 3 solo 1 topic
            query = em.createNamedQuery("Article.findByTopic", Article.class).setParameter("topic", topicid1);
        } else if ((topicid1 != 0 && topicid2 != 0) && (author == 0)){  //Caso 4 solo 2 topic
            query = em.createNamedQuery("Article.findByTwoTopic", Article.class).setParameter("topic1", topicid1).setParameter("topic2", topicid2);
        } else if ((topicid1 != 0 && topicid2 == 0) && (author != 0)){  //Caso 5 autor + 1 topic
            query = em.createNamedQuery("Article.findByAuthorAndSingleTopic", Article.class).setParameter("author", author).setParameter("topic", topicid1);
        } else if ((topicid1 != 0 && topicid2 != 0) && (author != 0)){  //Caso 5 autor + 2 topics
            query = em.createNamedQuery("Article.findByAuthorAndBothTopics", Article.class).setParameter("author", author).setParameter("topic1", topicid1).setParameter("topic2", topicid2);
        }

        List<Article> articles = query.getResultList();
        
        if (articles.isEmpty()){ 
            return Response.status(Response.Status.NOT_FOUND).entity("Article not found").build(); 
        } else { 
            return Response.ok(articles).build(); 
        }
    }

    
    
    /**
     * GET /rest/api/v1/article/${id}

Retorna l'article sencer a partir de l'identificador de l'article, és a dir, tota la informació indicada en la Figura 2. 

Si l'article és privat, només es podrà retornar si l'usuari està registrat.

Aquesta operació implica augmentar el nombre de visualitzacions de l'article en +1. 
     */
    
    @GET
    @Path("/{id}")
    @Secured
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response getArticleId(@PathParam("id") String ids, @HeaderParam("Authorization") String authorization) {
        System.out.println("Entro p1");
        int id;
        try{
            id = Integer.parseInt(ids);
        } catch (NumberFormatException e2){
            return Response.status(Response.Status.BAD_REQUEST).entity("Introduce una ID numerica valida").build();
        }
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }

        // Obtener el artículo de la base de datos usando su ID
        Article article;
        // Buscamos articulos en lista para ver si está vacia
        TypedQuery<Article> query = em.createNamedQuery("Article.findArticleId", Article.class)
                .setParameter("id", id);
        List<Article> result = query.getResultList();

        if (result.isEmpty()) {
            // Si la lista está vacía, significa que no se encontró el artículo
            return Response.status(Response.Status.NOT_FOUND).entity("Article not found").build();
        } else {
            // Si se encuentra, obtener el primer artículo de la lista
            article = result.get(0);
        }

        // Verificar si el artículo es privado
        if (!article.getIsPublic()) {
            if (authorization == null || authorization.isEmpty()) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("Access to the article is restricted. Authorization header is missing.")
                               .build();
            }
        }

        // Incrementar el contador de vistas
        article.incrementViews();
        em.merge(article); // Persistir el cambio en la base de datos
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
    public Response deleteArticle(@PathParam("id") String ids) {
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
            // Buscar el artículo en la base de datos
            TypedQuery<Article> query = em.createNamedQuery("Article.findArticleId", Article.class)
                        .setParameter("id", id);
            List<Article> result = query.getResultList();
            if (result.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Artículo no encontradoo.").build();
            }
            Article article = result.get(0);

            // Verificar que el usuario autenticado es el autor del artículo
            String username = article.getAuthor().getCredenciales().getUsername(); // supondremos que este es el nombre del usuario autenticado
            if (!article.getAuthor().getCredenciales().getUsername().equals(username)) {
                return Response.status(Response.Status.FORBIDDEN).entity("No tienes permisos para eliminar este artículo.").build();
            }

            // Eliminar el artículo
            em.remove(article);
            return Response.noContent().build();
        } catch (NumberFormatException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Introduce una ID numerica").build();
        }
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Transactional
    public Response createArticle(Article article) {
        if (article == null || article.getAuthor() == null || article.getTopic().isEmpty() || article.getTitle() == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Introduce un artículo con autor y topicos").build();
        } else if(article.getId() != 0){
            return Response.status(Response.Status.BAD_REQUEST).entity("El artículo no debe tener ID").build();
        } else {
            try{
                
                TypedQuery<Long> query = em.createNamedQuery("Customer.existAuthor", Long.class).setParameter("id", article.getAuthor().getId());
                Long authResult = query.getSingleResult();
                Long topicResult = -1L;
                if (authResult == 0) { return Response.status(Response.Status.NOT_FOUND).entity("Autor con ID "+article.getAuthor().getId()+" no existe.").build(); }
                List<Topic> topics = article.getTopic();
                for (Topic topic : topics) {
                    TypedQuery<Long> querytopic = em.createNamedQuery("Topic.existTopic", Long.class).setParameter("id", topic.getId());
                    topicResult = querytopic.getSingleResult();
                    if (topicResult == 0) { return Response.status(Response.Status.NOT_FOUND).entity("Topico con ID "+topic.getId()+" no existe.").build(); }
                }
                em.persist(article);
                article.setPublishedDate(new Date());
                article.getAuthor().setLastArticleId((long)(article.getId()));
                article.setViews(0);
                em.merge(article);
                return Response.status(Response.Status.CREATED).entity("Article created with ID: " + article.getId()+"Authid: "+article.getAuthor().getId()).build();
             }catch (NoResultException e) {
                 // Manejo de caso donde no se encuentran resultados
                 return Response.status(Response.Status.NOT_FOUND).entity("Autor o tópicos no existen.").build();
             } catch (Exception e) {
                 e.printStackTrace();  // Asegúrate de ver los detalles de la excepción en los logs
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("An unexpected error occurred: " + e.getMessage())
                         .build();
            }
        }
    }
    
    @GET
    @Path("/isprivate/{id}")
    public Response isPrivate(@PathParam("id") String ids) {
        int id;
        try{
            id = Integer.parseInt(ids);
        } catch (NumberFormatException e2){
            return Response.status(Response.Status.BAD_REQUEST).entity("Introduce una ID numerica valida").build();
        }
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID").build();
        }

        // Obtener el artículo de la base de datos usando su ID
        Article article;
        // Buscamos articulos en lista para ver si está vacia
        TypedQuery<Article> query = em.createNamedQuery("Article.findArticleId", Article.class)
                .setParameter("id", id);
        List<Article> result = query.getResultList();

        if (result.isEmpty()) {
            // Si la lista está vacía, significa que no se encontró el artículo
            return Response.status(Response.Status.NOT_FOUND).entity("Article not found").build();
        } else {
            // Si se encuentra, obtener el primer artículo de la lista
            article = result.get(0);
        }

        // Verificar si el artículo es privado
        if (!article.getIsPublic()) {
            return Response.status(Response.Status.FORBIDDEN).entity("Access to the article is restricted.").build();
        }

        return Response.ok(article).build();
    }
}
