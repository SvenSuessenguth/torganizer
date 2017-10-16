package org.cc.torganizer.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.rest.container.PersonsContainer;

@Stateless
@Path("/persons")
@Produces("application/json")
public class PersonsResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @GET
  @Path("/")
  public PersonsContainer all() {

    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findAll", Person.class);
    List<Person> persons = namedQuery.getResultList();

    return new PersonsContainer(persons);
  }
  
  @GET
  @Path("{id}")
  public Person byId(@PathParam("id") Long id) {

    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findById", Person.class);
    namedQuery.setParameter("id", id);
    List<Person> persons = namedQuery.getResultList();

    return persons.get(0);
  }
  
  @GET
  @Path("/{gender}")
  public PersonsContainer allByGender(@PathParam("gender") String gender) {

    Gender g = Gender.valueOf(gender.toUpperCase());
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findByGender", Person.class);
    namedQuery.setParameter("gender", g);
    
    List<Person> personsByGender = namedQuery.getResultList();

    return new PersonsContainer(personsByGender);
  }

  @GET
  @Path("/add")
  public Person addGenderRestriction(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
      @QueryParam("gender") Gender gender, @QueryParam("dobISO") String dobISO) {

    Person person = new Person(firstName, lastName);
    person.setGender(gender);
    LocalDate dateOfBirth = LocalDate.parse(dobISO, DateTimeFormatter.ISO_DATE);
    person.setDateOfBirth(dateOfBirth);

    entityManager.persist(person);

    return person;
  }
}