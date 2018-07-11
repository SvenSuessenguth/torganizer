package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PersonsRepository extends Repository {

    public PersonsRepository() {
    }

    /**
     * Constructor for testing.
     *
     * @param entityManager EntityManager
     */
    PersonsRepository(final EntityManager entityManager) {
        super(entityManager);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Person CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Person create(final Person person) {
        // Person wird als nicht-persistente entity betrachtet.
        // vom client kann die id '0' geliefert werden, sodass eine detached-entity-Exception geworfen wird.
        person.setId(null);
        getEntityManager().persist(person);

        return person;
    }

    public final Person read(final Long personId) {
        return getEntityManager().find(Person.class, personId);
    }

    public final List<Person> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);


        TypedQuery<Person> namedQuery = getEntityManager().createNamedQuery("Person.findAll", Person.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    public final List<Person> read(Integer offset, Integer maxResults, final Gender gender) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);


        TypedQuery<Person> namedQuery = getEntityManager().createNamedQuery("Person.findByGender", Person.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        namedQuery.setParameter("gender", gender);

        return namedQuery.getResultList();
    }

    public final Person update(final Person person) {
        getEntityManager().merge(person);

        return person;
    }

    public final Person delete(final Long personId) {
        Person person = getEntityManager().find(Person.class, personId);

        getEntityManager().remove(person);

        return person;
    }


    public final long count() {
        Query query = getEntityManager().createQuery("SELECT count(p) FROM Person p");
        return (long) query.getSingleResult();
    }
}
