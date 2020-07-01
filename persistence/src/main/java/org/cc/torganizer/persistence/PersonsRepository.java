package org.cc.torganizer.persistence;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

@RequestScoped
public class PersonsRepository extends Repository<Person> {

  public PersonsRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  PersonsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Person read(Long personId) {
    return entityManager.find(Person.class, personId);
  }

  @Override
  public List<Person> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findAll",
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


    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findByGender",
        Person.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    namedQuery.setParameter("gender", gender);

    return namedQuery.getResultList();
  }

  public long count() {
    Query query = entityManager.createQuery("SELECT count(p) FROM Person p");
    return (long) query.getSingleResult();
  }
}
