package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;

class RestrictionsRepositoryTest extends AbstractDbUnitJpaTest {

  private RestrictionsRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-restrictions.xml");
    repository = new RestrictionsRepository(entityManager);
  }

  @Test
  void testFindAll() {
    var restrictions = entityManager.createNamedQuery("Restriction.findAll", Restriction.class)
      .getResultList();
    assertThat(restrictions).hasSize(4);
  }

  @Test
  void testFindById_GenderRestriction() {
    var restriction = repository.read(1L);

    assertThat(restriction).isInstanceOf(GenderRestriction.class);
    var genderRestriction = (GenderRestriction) restriction;
    assertThat(genderRestriction.getGender()).isEqualTo(FEMALE);
  }

  @Test
  void testFindById_AgeRestriction() {
    var restriction = repository.read(3L);

    assertThat(restriction).isInstanceOf(AgeRestriction.class);

    var ageRestriction = (AgeRestriction) restriction;
    assertThat(ageRestriction).isNotNull();
    assertThat(ageRestriction.getMaxDateOfBirth()).isEqualTo(LocalDate.of(1999, 1, 2));
    assertThat(ageRestriction.getMinDateOfBirth()).isEqualTo(LocalDate.of(1998, 6, 6));
  }

  @Test
  void testFindById_OpponentTypeRestriction() {
    var restriction = repository.read(4L);

    assertThat(restriction).isInstanceOf(OpponentTypeRestriction.class);

    var otRestriction = (OpponentTypeRestriction) restriction;
    assertThat(otRestriction.getOpponentType()).isEqualTo(PLAYER);
  }

  @Test
  void testCreateAgeRestrictionWithNull() {
    var ageRestriction = new AgeRestriction();
    repository.create(ageRestriction);

    assertThat(ageRestriction.getId()).isNotNull();
  }

  @Test
  void testDefaultsNotUsed() {
    var read = repository.read(1, 1);
    assertThat(read).hasSize(1);
  }

  @Test
  void testDefaultsUsed() {
    var read = repository.read(null, null);
    assertThat(read).hasSize(4);
  }
}