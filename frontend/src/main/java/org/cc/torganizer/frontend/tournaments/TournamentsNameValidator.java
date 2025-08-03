package org.cc.torganizer.frontend.tournaments;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.cc.torganizer.core.entities.Tournament;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validating the name of a Tournament.
 */
@FacesValidator("tournamentsNameValidator")
public class TournamentsNameValidator implements Validator<String> {


  @Override
  public void validate(FacesContext facesContext, UIComponent uiComponent, String tournamentsName) {
    // to keep it simple a new tournament with the given name/id is created and then validated
    var tournament = new Tournament();
    tournament.setName(tournamentsName);
    var constraintViolations = validate(tournament);

    if (!constraintViolations.isEmpty()) {
      var facesMessages = createFacesMessages(constraintViolations);
      throw new ValidatorException(facesMessages);
    }
  }

  private Set<ConstraintViolation<Tournament>> validate(Tournament newTournament) {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    return validator.validate(newTournament);
  }

  private Collection<FacesMessage> createFacesMessages(Set<ConstraintViolation<Tournament>> constraintViolations) {

    return constraintViolations
      .stream()
      .map(ConstraintViolation::getMessage)
      .map(messageText -> new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, messageText))
      .collect(Collectors.toCollection(() -> new ArrayList<>(constraintViolations.size())));
  }
}
