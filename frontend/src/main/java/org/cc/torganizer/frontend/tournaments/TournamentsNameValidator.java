package org.cc.torganizer.frontend.tournaments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.cc.torganizer.core.entities.Tournament;

@FacesValidator("tournamentsNameValidator")
public class TournamentsNameValidator implements Validator<String> {

  @Override
  public void validate(FacesContext facesContext, UIComponent uiComponent, String tournamentsName) {

    // to keep it simple a new tournament with the given name is created and then validated
    Tournament tournament = new Tournament();
    tournament.setName(tournamentsName);
    Set<ConstraintViolation<Tournament>> constraintViolations = validate(tournament);

    if (!constraintViolations.isEmpty()) {
      Collection<FacesMessage> facesMessages = createFacesMessages(constraintViolations);
      throw new ValidatorException(facesMessages);
    }
  }

  private Set<ConstraintViolation<Tournament>> validate(Tournament newTournament) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    javax.validation.Validator validator = factory.getValidator();
    return validator.validate(newTournament);
  }

  private Collection<FacesMessage> createFacesMessages(
      Set<ConstraintViolation<Tournament>> constraintViolations) {

    Collection<FacesMessage> facesMessages = new ArrayList<>(constraintViolations.size());

    for (ConstraintViolation violation : constraintViolations) {
      String messageText = violation.getMessage();
      FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
          messageText, messageText);
      facesMessages.add(facesMessage);
    }

    return facesMessages;
  }
}
