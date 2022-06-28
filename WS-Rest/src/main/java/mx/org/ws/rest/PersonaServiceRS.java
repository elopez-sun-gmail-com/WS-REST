package mx.org.ws.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import mx.org.dto.Persona;
import javax.ws.rs.core.Response.Status;

/**
 * URL WADL -->> http://localhost:8080/WS-Rest/webservice/application.wadl URL
 * XSD -->> http://localhost:8080/WS-Rest/webservice/application.wadl/xsd0.xsd
 *
 * @author elopez
 */
@Path("/personas")
@Stateless
public class PersonaServiceRS {

    private List<Persona> listPersona = null;

    public PersonaServiceRS() {

        this.listPersona = new ArrayList<>();

        {
            Persona entity = new Persona();
            entity.setId(1);
            entity.setName("Ernesto López Hernández");
            entity.setAddress("Aquiles Serdan");
            entity.setPhone("5525584792");
            entity.setEmail("elopez.sun@gmail.com");

            this.listPersona.add(entity);
        }

        {
            Persona entity = new Persona();
            entity.setId(2);
            entity.setName("Carlos López Hernández");
            entity.setAddress("Fortin de las flores #7");
            entity.setPhone("5525584793");
            entity.setEmail("clopez.sun@gmail.com");

            this.listPersona.add(entity);
        }

        {
            Persona entity = new Persona();
            entity.setId(3);
            entity.setName("Jesús López Hernández");
            entity.setAddress("Fortin de las flores #7");
            entity.setPhone("5525584794");
            entity.setEmail("jlopez.sun@gmail.com");

            this.listPersona.add(entity);
        }

    }

    @GET
    @Path("getListPersonas")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Persona> getListPersonas() {
        return this.listPersona;
    }

    @GET
    @Path("getPersona/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Persona getPersona(@PathParam("id") int id) {

        Persona entity = null;

        List<Persona> listPersona = this.listPersona.stream()
                .filter(row -> row.getId() == id)
                .collect(Collectors.toList());

        if (listPersona != null && listPersona.size() > 0) {
            entity = listPersona.get(0);
        }

        return entity;
    }

    @POST
    @Path("addPersona")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addPersona(Persona entity) {

        try {

            if (entity != null) {

                entity.setId(this.listPersona.size() + 1);

                this.listPersona.add(entity);

                if (entity.getFoto() != null) {

                    InputStream stream = new ByteArrayInputStream(entity.getFoto().getDocument());

                    Files.copy(stream, Paths.get("D:\\Users\\elopez\\Pictures\\photo-of-siberian-husky-puppy-2853130-1024x683_COPY_DB.jpg"));

                }

            }

            return Response.ok().entity(entity).build();

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PUT
    @Path("updatePersona/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updatePersona(@PathParam("id") int id, Persona entity) {

        try {

            Persona persona = this.getPersona(id);

            if (persona != null) {

                entity.setId(persona.getId());

                List<Persona> listPersona = this.listPersona.stream()
                        .filter(row -> row.getId() != id)
                        .collect(Collectors.toList());

                if (listPersona != null && listPersona.size() > 0) {
                    listPersona.add(entity);

                    this.listPersona = listPersona;
                }

                return Response.ok().entity(entity).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DELETE
    @Path("deletePersona/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deletePersona(@PathParam("id") int id) {

        try {
            List<Persona> listPersona = this.listPersona.stream()
                    .filter(row -> row.getId() != id)
                    .collect(Collectors.toList());

            if (listPersona != null && listPersona.size() > 0) {
                this.listPersona = listPersona;
            }

            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
