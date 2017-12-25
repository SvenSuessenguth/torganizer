package org.cc.torganizer.persistence;

import java.util.List;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;

public class PersonsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-persons.xml");
  }

  @Test
  public void testFindAll() {
    List<Person> allPersons = entityManager.createNamedQuery("Person.findAll", Person.class)
        .getResultList();
    assertThat(allPersons, hasSize(5));
  }
  
  @Test
  public void testFindAll_byGenderMale() {
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findByGender", Person.class);
    namedQuery.setParameter("gender", Gender.MALE);
    
    List<Person> allMalePersons = namedQuery.getResultList();
    assertThat(allMalePersons, hasSize(2));
  }
  
  @Test
  public void testFindAll_byGenderFemale() {
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findByGender", Person.class);
    namedQuery.setParameter("gender", Gender.FEMALE);
    
    List<Person> allMalePersons = namedQuery.getResultList();
    assertThat(allMalePersons, hasSize(3));
  }
  
  @Test
  public void testFindAll_byGenderUnknown() {
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findByGender", Person.class);
    namedQuery.setParameter("gender", Gender.UNKNOWN);
    
    List<Person> allMalePersons = namedQuery.getResultList();
    assertThat(allMalePersons, hasSize(0));
  }
}
