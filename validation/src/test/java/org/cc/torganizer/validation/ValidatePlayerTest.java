package org.cc.torganizer.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.BeforeClass;
import org.junit.Test;

public class ValidatePlayerTest {
  
  private static Validator validator;
  
  @BeforeClass
  public static void beforeClass() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }
  
  @Test
  public void testValidatePlayer_firstNameTooShort() {
    String tooShortFirstName = "";
    Person p = new Person(tooShortFirstName, "ok");
    Player player = new Player(p);
    
    Set<ConstraintViolation<Player>> violations = validator.validate( player );
    
    // notBlank, min-size=1
    assertThat(violations.size(), is(2));
  }
  
  @Test
  public void testValidatePlayer_firstNameTooLong() {
    String tooShortFirstName = "012345678901234567890123456789";
    Person p = new Person(tooShortFirstName, "ok");
    Player player = new Player(p);
    
    Set<ConstraintViolation<Player>> violations = validator.validate( player );
    
    // notBlank, min-size=1
    assertThat(violations.size(), is(1));
  }
}
