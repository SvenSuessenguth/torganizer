package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.MALE;

import java.util.List;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonsRepositoryTest extends AbstractDbUnitJpaTest {

  private PersonsRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-persons.xml");
    repository = new PersonsRepository(entityManager);
  }

  @Test
  void testById() {
    Person person = repository.read(1L);

    assertThat(person.getGender()).isEqualTo(MALE);
  }

  @Test
  void testFindAll() {
    List<Person> allPersons = repository.read(0, 100);

    assertThat(allPersons).hasSize(5);
  }

  @Test
  void testFindAll_byGenderMale() {
    List<Person> allMalePersons = repository.read(0, 100, MALE);

    assertThat(allMalePersons).hasSize(2);
  }

  @Test
  void testFindAll_byGenderFemale() {
    List<Person> allFemalePersons = repository.read(0, 100, Gender.FEMALE);

    assertThat(allFemalePersons).hasSize(3);
  }

  @Test
  void testFindAll_byGenderUnknown() {
    List<Person> allUnknownPersons = repository.read(0, 100, Gender.UNKNOWN);

    assertThat(allUnknownPersons).isEmpty();
  }


  @Test
  void testCreate() {
    Person person = new Person("firstName", "lastName");

    repository.create(person);

    assertThat(person.getId()).isNotNull();
  }
}
