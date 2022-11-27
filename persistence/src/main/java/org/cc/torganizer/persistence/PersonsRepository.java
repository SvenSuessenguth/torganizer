package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

/**
 * Accessing the Repository for Persons and related entities.
 */
@RequestScoped
public class PersonsRepository extends Repository<Person> {

  @SuppressWarnings("unused")
  public PersonsRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  PersonsRepository(EntityManager entityManager) {
    this.em = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Person read(Long personId) {
    return em.find(Person.class, personId);
  }

  @Override
  public List<Person> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Person> namedQuery = em.createNamedQuery("Person.findAll",
        Person.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  /**
   * Reading <code>maxResults</code> Persons by Gender from <code>offset</code>.
   */
  public List<Person> read(Integer offset, Integer maxResults, Gender gender) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Person> namedQuery = em.createNamedQuery("Person.findByGender",
        Person.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    namedQuery.setParameter("gender", gender);

    return namedQuery.getResultList();
  }

  public long count() {
    var query = em.createQuery("SELECT count(p) FROM Person p");
    return (long) query.getSingleResult();
  }
}
