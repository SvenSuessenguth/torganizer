package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;
import java.util.List;

import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;
import org.junit.Before;
import org.junit.Test;

public class RestrictionsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-restrictions.xml");
  }

  @Test
  public void testFindAll() {
    List<Restriction> restrictions = entityManager.createNamedQuery("Restriction.findAll", Restriction.class)
        .getResultList();
    assertThat(restrictions, hasSize(4));
  }

  @Test
  public void testFindById_GenderRestriction() {
    Restriction restriction = entityManager.createNamedQuery("Restriction.findById", Restriction.class)
        .setParameter("id", 1L).getSingleResult();

    assertThat(restriction, is(instanceOf(GenderRestriction.class)));
    GenderRestriction genderRestriction = (GenderRestriction) restriction;
    assertThat(genderRestriction.getGender(), is(Gender.FEMALE));
  }

  @Test
  public void testFindById_AgeRestriction() {
    Restriction restriction = entityManager.createNamedQuery("Restriction.findById", Restriction.class)
        .setParameter("id", 3L).getSingleResult();

    assertThat(restriction, is(instanceOf(AgeRestriction.class)));
    AgeRestriction ageRestriction = (AgeRestriction) restriction;
    assertThat(ageRestriction, is(notNullValue()));
    assertThat(ageRestriction.getMaxDateOfBirth(), is(equalTo(LocalDate.of(1999, 1, 2))));
    assertThat(ageRestriction.getMinDateOfBirth(), is(equalTo(LocalDate.of(1998, 6, 6))));
  }

  @Test
  public void testFindById_OpponentTypeRestriction() {
    Restriction restriction = entityManager.createNamedQuery("Restriction.findById", Restriction.class)
        .setParameter("id", 4L).getSingleResult();

    assertThat(restriction, is(instanceOf(OpponentTypeRestriction.class)));
    OpponentTypeRestriction otRestriction = (OpponentTypeRestriction) restriction;
    assertThat(otRestriction.getOpponentType(), is(OpponentType.PLAYER));
  }
}
