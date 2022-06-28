package mx.org.client.ws.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.org.domain.Foto;
import mx.org.domain.Persona;

/**
 *
 * @author elopez
 */
public class Test {

    private static final String URL_BASE = "http://localhost:8080/WS-Rest/webservice";
    private static Client cliente;
    private static WebTarget webTarget;
    private static Persona persona;
    private static List<Persona> personas;
    private static Invocation.Builder invocationBuilder;
    private static Response response;

    /**
     *
     */
    public static void getPersona() {

        System.out.println("");

        persona = webTarget.path("getPersona/1").request(MediaType.APPLICATION_XML).get(Persona.class);

        System.out.println("Persona Recuperada -->> " + persona);
    }

    /**
     *
     */
    public static void getListPersonas() {

        System.out.println("");

        personas = webTarget.path("/getListPersonas").request(MediaType.APPLICATION_XML).get(Response.class).readEntity(new GenericType<List<Persona>>() {
        });

        for (Persona item : personas) {
            System.out.println("Persona Recuperada -->> " + item);
        }
    }

    /**
     *
     */
    public static void addPersona() throws IOException {

        System.out.println("");

        String filePath = "D:\\Users\\elopez\\Pictures\\photo-of-siberian-husky-puppy-2853130-1024x683.jpg";

        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        
        Foto foto = new Foto();
        foto.setName("photo-of-siberian-husky-puppy-2853130-1024x683.jpg");
        foto.setDocument(bytes);

        Persona _entity = new Persona();
        _entity.setName("Bianey López Fernandez");
        _entity.setAddress("Fortin de las flores #7");
        _entity.setPhone("5525584793");
        _entity.setEmail("blopez.sun@gmail.com");
        _entity.setFoto(foto);

        invocationBuilder = webTarget.path("addPersona").request(MediaType.APPLICATION_XML);

        response = invocationBuilder.post(Entity.entity(_entity, MediaType.APPLICATION_XML));

        System.out.println(response.getStatus());
    }

    /**
     *
     * @param entity
     */
    public static void updatePersona(Persona entity) {

        System.out.println("");

        invocationBuilder = webTarget.path("updatePersona/" + entity.getId()).request(MediaType.APPLICATION_XML);

        response = invocationBuilder.put(Entity.entity(entity, MediaType.APPLICATION_XML));

        System.out.println(response.getStatus());

        System.out.println("Persona modificada -->> " + response.readEntity(Persona.class));
    }

    /**
     *
     * @param id
     */
    public static void deletePersona(int id) {

        System.out.println("");

        invocationBuilder = webTarget.path("deletePersona/" + id).request(MediaType.APPLICATION_XML);

        response = invocationBuilder.delete();

        System.out.println(response.getStatus());

    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) throws IOException {

        cliente = ClientBuilder.newClient();

        webTarget = cliente.target(URL_BASE).path("/personas");

        //GET
        Test.getPersona();

        Test.getListPersonas();

        //POST
        Test.addPersona();

        Persona personaRecuperada = response.readEntity(Persona.class);

        System.out.println("Persona Recuperada -->> " + personaRecuperada);

        personaRecuperada.setName("Bianey López Hernández");

        //PUT
        Test.updatePersona(personaRecuperada);

        Test.getListPersonas();

        //DELETE
        Test.deletePersona(personaRecuperada.getId());

        Test.getListPersonas();
    }
}
