package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.MALE;

class PersonsRepositoryTest extends AbstractDbUnitJpaTest {

  private PersonsRepository repository;

  @BeforeEach
  void beforeAll() throws Exception {
    super.initDatabase("test-data-persons.xml");
    repository = new PersonsRepository(entityManager);
  }

  @Test
  void testById() {
    var person = repository.read(1L);

    assertThat(person.getGender()).isEqualTo(MALE);
  }

  @Test
  void testFindAll() {
    var allPersons = repository.read(0, 100);

    assertThat(allPersons).hasSize(5);
  }

  @Test
  void testFindAll_byGenderMale() {
    var allMalePersons = repository.read(0, 100, MALE);

    assertThat(allMalePersons).hasSize(2);
  }

  @Test
  void testFindAll_byGenderFemale() {
    var allFemalePersons = repository.read(0, 100, Gender.FEMALE);

    assertThat(allFemalePersons).hasSize(3);
  }

  @Test
  void testFindAll_byGenderUnknown() {
    var allUnknownPersons = repository.read(0, 100, Gender.UNKNOWN);

    assertThat(allUnknownPersons).isEmpty();
  }


  @Test
  void testCreate() {
    var person = Person.builder().build();

    repository.create(person);

    assertThat(person.getId()).isNotNull();
  }
}
