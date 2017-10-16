package org.cc.torganizer.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.rest.container.RestrictionsContainer;

@Stateless
@Path("restrictions")
@Produces("application/json")
public class RestrictionsResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @GET
  @Path("/")
  public RestrictionsContainer all() {

    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findAll", Restriction.class);
    List<Restriction> restrictionsAsList = namedQuery.getResultList();

    RestrictionsContainer restrictions = new RestrictionsContainer();
    for (Restriction restriction : restrictionsAsList) {
      restrictions.add(restriction);
    }

    return restrictions;
  }

  @GET
  @Path("/add/gender")
  public Restriction addGenderRestriction(@QueryParam("validGender") Gender validGender) {
    GenderRestriction genderRestriction = new GenderRestriction();
    genderRestriction.setValidGender(validGender);

    entityManager.persist(genderRestriction);

    return genderRestriction;
  }

  @GET
  @Path("/add/age")
  public Restriction addAgeRestriction(@QueryParam("maxDateOfBirth") String maxDateOfBirthISO,
      @QueryParam("minDateOfBirth") String minDateOfBirthISO) {

    AgeRestriction ageRestriction = new AgeRestriction();
    ageRestriction.setMaxDateOfBirthISO(maxDateOfBirthISO);
    ageRestriction.setMinDateOfBirthISO(minDateOfBirthISO);

    entityManager.persist(ageRestriction);

    return ageRestriction;
  }
  
  @GET
  @Path("/add/opponenttype")
  public Restriction addOpponentTypeRestriction(@QueryParam("validOpponentType") OpponentType validOpponentType) {

    OpponentTypeRestriction otRestriction = new OpponentTypeRestriction();
    otRestriction.setValidOpponentType(validOpponentType);
    
    entityManager.persist(otRestriction);

    return otRestriction;
  }
}