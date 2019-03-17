package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;

import java.time.LocalDate;
import java.util.List;

import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestrictionsRepositoryTest extends AbstractDbUnitJpaTest {

  private RestrictionsRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-restrictions.xml");
    repository = new RestrictionsRepository(entityManager);
  }

  @Test
  void testFindAll() {
    List<Restriction> restrictions = entityManager.createNamedQuery("Restriction.findAll", Restriction.class)
        .getResultList();
    assertThat(restrictions).hasSize(4);
  }

  @Test
  void testFindById_GenderRestriction() {
    Restriction restriction = repository.read(1L);

    assertThat(restriction).isInstanceOf(GenderRestriction.class);
    GenderRestriction genderRestriction = (GenderRestriction) restriction;
    assertThat(genderRestriction.getGender()).isEqualTo(FEMALE);
  }

  @Test
  void testFindById_AgeRestriction() {
    Restriction restriction = repository.read(3L);

    assertThat(restriction).isInstanceOf(AgeRestriction.class);

    AgeRestriction ageRestriction = (AgeRestriction) restriction;
    assertThat(ageRestriction).isNotNull();
    assertThat(ageRestriction.getMaxDateOfBirth()).isEqualTo(LocalDate.of(1999, 1, 2));
    assertThat(ageRestriction.getMinDateOfBirth()).isEqualTo(LocalDate.of(1998, 6, 6));
  }

  @Test
  void testFindById_OpponentTypeRestriction() {
    Restriction restriction = repository.read(4L);

    assertThat(restriction).isInstanceOf(OpponentTypeRestriction.class);

    OpponentTypeRestriction otRestriction = (OpponentTypeRestriction) restriction;
    assertThat(otRestriction.getOpponentType()).isEqualTo(PLAYER);
  }
}