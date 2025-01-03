package se.kth.cbh.cm1007.ui.resource;

import io.smallrye.mutiny.Uni;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/persons")
public class PersonResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<se.kth.cbh.cm1007.core.model.Person>> getAllPersons() {
        return se.kth.cbh.cm1007.core.model.Person.listAll();
    }
}