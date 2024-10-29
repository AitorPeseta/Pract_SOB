/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

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
    
    /**
     * GET /rest/api/v1/customer/${id}

Retorna la informació de l'usuari amb identificador ${id}. 

Aquesta crida no pot retornar informació confidencial, p. ex., la  contrasenya d'aquest usuari.
     */
    
    /**
     * PUT /rest/api/v1/customer/${id}

Opcional! Modifica les dades del client amb identificador ${id} al sistema amb les dades JSON/XML proporcionades.

Per aquesta operació, cal que el client estigui autentificat.
     */
}
