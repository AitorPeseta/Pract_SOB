/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 *
 * @author xaviv
 */
@Path("example")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getExample() {
        return "Hello, World!";
    }
}
