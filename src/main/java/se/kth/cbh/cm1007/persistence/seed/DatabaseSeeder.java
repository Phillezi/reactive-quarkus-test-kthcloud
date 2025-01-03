package se.kth.cbh.cm1007.persistence.seed;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import se.kth.cbh.cm1007.core.model.Person;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class DatabaseSeeder {

    private final PgPool client;
    private final boolean schemaCreate;

    public DatabaseSeeder(PgPool client,
            @ConfigProperty(name = "myapp.schema.create", defaultValue = "true") boolean schemaCreate) {
        this.client = client;
        this.schemaCreate = schemaCreate;
    }

    void onStart(@Observes StartupEvent ev) {
        if (schemaCreate) {
            initdb();
        }
    }

    private void initdb() {
        client.query("DROP TABLE IF EXISTS person").execute()
                .flatMap(r -> client.query(
                        "CREATE TABLE person (id BIGSERIAL PRIMARY KEY, first_name TEXT NOT NULL, last_name TEXT NOT NULL, age INT NOT NULL)")
                        .execute())
                .flatMap(r -> insertPersons(List.of(
                        createPerson("Alice", "Smith", 30),
                        createPerson("Bob", "Brown", 25),
                        createPerson("Charlie", "Davis", 35))))
                .await().indefinitely();
    }

    private Uni<Void> insertPersons(List<Person> persons) {
        List<Uni<Void>> insertQueries = persons.stream()
                .map(person -> client
                        .preparedQuery("INSERT INTO person (first_name, last_name, age) VALUES ($1, $2, $3)")
                        .execute(Tuple.of(person.firstName, person.lastName, person.age))
                        .replaceWithVoid())
                .toList();

        return Uni.combine().all().unis(insertQueries).discardItems();
    }

    private Person createPerson(String firstName, String lastName, int age) {
        Person person = new Person();
        person.firstName = firstName;
        person.lastName = lastName;
        person.age = age;
        return person;
    }
}
