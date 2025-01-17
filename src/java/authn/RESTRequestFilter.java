package authn;

import com.sun.xml.messaging.saaj.util.Base64;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.container.ResourceInfo;
import model.entities.Article;
import model.entities.Customer;


/**
 * @author Marc Sanchez
 */
@Priority(Priorities.AUTHENTICATION)
@Provider
public class RESTRequestFilter implements ContainerRequestFilter {
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";      
    
    // to access the resource class and resource method matched by the current request
    @Context
    private ResourceInfo resourceInfo;

    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    @Override
    public void filter(ContainerRequestContext requestCtx) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method != null) 
        {

            Secured secured = method.getAnnotation(Secured.class);
            if(secured != null)
            {

                List<String> headers = requestCtx.getHeaders()
                        .get(HttpHeaders.AUTHORIZATION);

                if(headers != null && !headers.isEmpty())
                {
                    String username;
                    String password;
                    try {
                        String auth = headers.get(0);
                        auth = auth.replace(AUTHORIZATION_HEADER_PREFIX, "");
                        String decode = Base64.base64Decode(auth);
                        StringTokenizer tokenizer = new StringTokenizer(decode, ":");
                        username = tokenizer.nextToken();
                        password = tokenizer.nextToken();
                    } catch(@SuppressWarnings("unused") Exception e){
                        requestCtx.abortWith(
                                Response.status(Response.Status.BAD_REQUEST).build()
                        );
                        return;
                    }
                    try {
                        TypedQuery<Credentials> query = em.createNamedQuery("Credentials.findUser", Credentials.class);
                        Credentials c = query.setParameter("username", username)
                            .getSingleResult();
                        if(!c.getPassword().equals(password)) {
                            requestCtx.abortWith(
                                Response.status(Response.Status.FORBIDDEN).entity("Wrong username or password.").build()
                            );
                        }
                    } catch(@SuppressWarnings("unused") NoResultException e) {
                        requestCtx.abortWith(
                            Response.status(Response.Status.UNAUTHORIZED).entity("Please authenticate to get access.").build()
                        );
                    }
                    if (method.getName().equalsIgnoreCase("getArticleId")){
                        if(comprovaPrivat(requestCtx)){ //Si es article privat comprovem si es autor el que ho demana
                            if (username == null && password == null){  //CANVI RESPECTE PRAC 1
                                requestCtx.abortWith(
                                    Response.status(Response.Status.UNAUTHORIZED).entity("Access to the article is restricted to the owner because it's private.").build()
                                );
                            }
                        }
                    }
                    if(method.getName().equalsIgnoreCase("deleteArticle")){
                        if (!(comprovaAutor(requestCtx, username, password))){
                            requestCtx.abortWith(
                                Response.status(Response.Status.UNAUTHORIZED).entity("Access to delete the article is reserved to the owner.").build()
                            );
                        }
                    }
         // Fer la comprovació de si  l'article és privat o no amb JPQL; i si ho és, fer l'autentificació.
            
            
                } else {
                    if (!(method.getName().equalsIgnoreCase("getArticleId") && !comprovaPrivat(requestCtx))){   //Si demanen article per ID pero no es privat, no cal auth.
                        requestCtx.abortWith(
                            Response.status(Response.Status.UNAUTHORIZED).entity("Please authenticate to get access.").build()
                        );
                    }
                }
            }
        }
    }

    private boolean comprovaPrivat(ContainerRequestContext requestCtx) {
        try {
            String uri = requestCtx.getUriInfo().getRequestUri().toString();
            String[] cosas = uri.split("/");
            String id_String = cosas[cosas.length-1];
            int id;
            try{
                id = Integer.parseInt(id_String);
            } catch (NumberFormatException e2){ //Id no numeric
                return false;
            }
            boolean esPublic = em.createNamedQuery("Article.isPublic", boolean.class).setParameter("id",id).getSingleResult();
            return !esPublic;   //Es privat
        } catch (NoResultException e) { //No existeix article
            return false;
        }
    }

    private boolean comprovaAutor(ContainerRequestContext requestCtx, String username, String password) {
        boolean esAitor = false;
        String uri = requestCtx.getUriInfo().getRequestUri().toString();
        String[] split = uri.split("/");
        String ident = split[split.length-1];
        int id;
        try{
            id = Integer.parseInt(ident);
        } catch (NumberFormatException e2){ //Id no numeric
            return false;
        }
        try {
            Article articulo = (em.createNamedQuery("Article.findArticleId", Article.class).setParameter("id",id).getSingleResult());
            if(articulo.getAuthor().getCredenciales().getUsername().equals(username) && articulo.getAuthor().getCredenciales().getPassword().equals(password)){ esAitor = true; }
            return esAitor;
        } catch (NoResultException e) { //No existeix article
            return false;
        }
    }
}