package org.cc.torganizer.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.rest.json.RestrictionJsonConverter;

@Stateless
@Path("restrictions")
@Produces("application/json")
public class RestrictionsResource {

  @Inject
  private RestrictionJsonConverter converter;
  
	@PersistenceContext(name = "torganizer")
	EntityManager entityManager;

	@GET
	public JsonObject all() {

		TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findAll", Restriction.class);
		List<Restriction> restrictions = namedQuery.getResultList();

		return null;
	}

	@GET
	@Path("{id}")
	public JsonObject byId(@PathParam("id") Long id) {

		TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findById", Restriction.class);
		namedQuery.setParameter("id", id);
		List<Restriction> restrictions = namedQuery.getResultList();

		return null;
	}

	@GET
	@Path("/new/gender")
	public JsonObject newGenderRestriction(@QueryParam("validGender") Gender validGender) {
		GenderRestriction genderRestriction = new GenderRestriction();
		genderRestriction.setValidGender(validGender);

		entityManager.persist(genderRestriction);

		return null;
	}

	@GET
	@Path("/new/age")
	public JsonObject newAgeRestriction(@QueryParam("maxDateOfBirth") String maxDateOfBirthISO,
			@QueryParam("minDateOfBirth") String minDateOfBirthISO) {

		AgeRestriction ageRestriction = new AgeRestriction();
    
    LocalDate maxDateOfBirth = maxDateOfBirthISO == null ? null
				: LocalDate.parse(maxDateOfBirthISO, DateTimeFormatter.ISO_DATE);
    
    LocalDate minDateOfBirth = minDateOfBirthISO == null ? null
				: LocalDate.parse(minDateOfBirthISO, DateTimeFormatter.ISO_DATE);
    
		ageRestriction.setMaxDateOfBirth(maxDateOfBirth);
		ageRestriction.setMinDateOfBirth(minDateOfBirth);

		entityManager.persist(ageRestriction);

		return null;
	}

	@GET
	@Path("/new/opponenttype")
	public JsonObject newOpponentTypeRestriction(@QueryParam("validOpponentType") OpponentType validOpponentType) {

		OpponentTypeRestriction otRestriction = new OpponentTypeRestriction();
		otRestriction.setValidOpponentType(validOpponentType);

		entityManager.persist(otRestriction);

		return null;
	}
}