package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PersonsRepository extends Repository{

  public PersonsRepository() {
  }

  /**
   * Constructor for testing.
   */
  public PersonsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Person create(Person person){
    // Person wird als nicht-persistente entity betrachtet.
    // vom client kann die id '0' geliefert werden, sodass eine detached-entity-Exception geworfen wird.
    person.setId(null);
    entityManager.persist(person);

    return person;
  }

  public Person read(Long personId){
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findById", Person.class);
    namedQuery.setParameter("id", personId);
    return namedQuery.getSingleResult();
  }

  public List<Person> read(Integer offset, Integer maxResults){
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findAll", Person.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public Person update(Person person){
    entityManager.merge(person);

    return person;
  }

  public Person delete(Long personId){
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findById", Person.class);
    namedQuery.setParameter("id", personId);
    Person person = namedQuery.getSingleResult();

    entityManager.remove(person);

    return person;
  }


  public long count() {
    Query query = entityManager.createQuery("SELECT count(p) FROM Person p");
    return (long) query.getSingleResult();
  }
}
