package org.cc.torganizer.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.rest.container.PersonsContainer;
import org.cc.torganizer.rest.json.PersonJsonAdapter;

@Stateless
@Path("/persons")
@Produces("application/json")
public class PersonsResource extends AbstractResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  private Jsonb jsonb;
  
  public PersonsResource() {
    PersonJsonAdapter adapter = new PersonJsonAdapter();

    JsonbConfig jc = new JsonbConfig().withAdapters(adapter);
    jsonb = JsonbBuilder.create(jc);
  }
  
  @POST
  @Path("/create")
  public Person create(JsonObject jsonObject) {
    Person person = jsonb.fromJson(jsonObject.toString(), Person.class);
    
    // Person wird als nicht-persistente entity betrachtet.
    // vom client wird die id '0' geliefert, sodass eine detached-entity-Exception geworfen wird.
    person.setId(null);
    entityManager.persist(person);

    return person;
  }

  @GET
  @Path("{id}")
  public Person read(@PathParam("id") Long id) {

    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findById", Person.class);
    namedQuery.setParameter("id", id);
    List<Person> persons = namedQuery.getResultList();

    Person person = new Person("Sven", "Süssenguth");
    person.setId(id);
    return person;
  }

  @POST
  @Path("/update")
  public Person update(Person person) {
    entityManager.merge(person);

    return person;
  }

  @DELETE
  @Path("/delete/{id}")
  public Person delete(@PathParam("id") Long id) {
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findById", Person.class);
    namedQuery.setParameter("id", id);
    List<Person> persons = namedQuery.getResultList();

    Person personToDelete = persons.get(0);
    entityManager.remove(personToDelete);

    return personToDelete;
  }

  @GET
  @Path("/")
  public PersonsContainer all(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {

    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findAll", Person.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    List<Person> persons = namedQuery.getResultList();

    return new PersonsContainer(persons);
  }

  @GET
  @Path("/count")
  public long count() {
    Query query = entityManager.createQuery("SELECT count(*) FROM Person p");
    return (long) query.getSingleResult();
  }
}