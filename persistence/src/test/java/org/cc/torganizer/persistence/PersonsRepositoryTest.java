package org.cc.torganizer.persistence;

import java.util.List;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;

public class PersonsRepositoryTest extends AbstractDbUnitJpaTest {

  private PersonsRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-persons.xml");
    repository = new PersonsRepository(entityManager);
  }

  @Test
  public void testFindAll() {
    List<Person> allPersons = repository.read(0, 100);
    assertThat(allPersons, hasSize(5));
  }
  
  @Test
  public void testFindAll_byGenderMale() {
    List<Person> allMalePersons = repository.read(0,100, Gender.MALE);
    assertThat(allMalePersons, hasSize(2));
  }
  
  @Test
  public void testFindAll_byGenderFemale() {
    List<Person> allFemalePersons = repository.read(0,100, Gender.FEMALE);
    assertThat(allFemalePersons, hasSize(3));
  }
  
  @Test
  public void testFindAll_byGenderUnknown() {
    List<Person> allUnknownPersons = repository.read(0,100, Gender.UNKNOWN);
    assertThat(allUnknownPersons, hasSize(0));
  }
}
