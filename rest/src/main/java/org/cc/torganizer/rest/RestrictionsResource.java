package org.cc.torganizer.rest;

import java.util.List;

import javax.ejb.Stateless;
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
		List<Restriction> restrictions = namedQuery.getResultList();

		return new RestrictionsContainer(restrictions);
	}

	@GET
	@Path("{id}")
	public Restriction byId(@PathParam("id") Long id) {

		TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findById", Restriction.class);
		namedQuery.setParameter("id", id);
		List<Restriction> restrictions = namedQuery.getResultList();

		return restrictions.get(0);
	}

	@GET
	@Path("/new/gender")
	public Restriction newGenderRestriction(@QueryParam("validGender") Gender validGender) {
		GenderRestriction genderRestriction = new GenderRestriction();
		genderRestriction.setValidGender(validGender);

		entityManager.persist(genderRestriction);

		return genderRestriction;
	}

	@GET
	@Path("/new/age")
	public Restriction newAgeRestriction(@QueryParam("maxDateOfBirth") String maxDateOfBirthISO,
			@QueryParam("minDateOfBirth") String minDateOfBirthISO) {

		AgeRestriction ageRestriction = new AgeRestriction();
		ageRestriction.setMaxDateOfBirthISO(maxDateOfBirthISO);
		ageRestriction.setMinDateOfBirthISO(minDateOfBirthISO);

		entityManager.persist(ageRestriction);

		return ageRestriction;
	}

	@GET
	@Path("/new/opponenttype")
	public Restriction newOpponentTypeRestriction(@QueryParam("validOpponentType") OpponentType validOpponentType) {

		OpponentTypeRestriction otRestriction = new OpponentTypeRestriction();
		otRestriction.setValidOpponentType(validOpponentType);

		entityManager.persist(otRestriction);

		return otRestriction;
	}
}