package org.cc.torganizer.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.cc.torganizer.core.entities.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ValidatePersonTest {
  
  private static Validator validator;
  
  @BeforeAll
  public static void beforeClass() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }
  
  @Test
  public void testValidatePerson_firstNameTooShort() {
    String tooShortFirstName = "";
    Person p = new Person(tooShortFirstName, "ok");
    
    Set<ConstraintViolation<Person>> violations = validator.validate( p );
    
    // notBlank, min-size=1
    assertThat(violations).hasSize(2);
  }
  
  @Test
  public void testValidatePerson_firstNameBlank() {
    String tooShortFirstName = "   ";
    Person p = new Person(tooShortFirstName, "ok");
    
    Set<ConstraintViolation<Person>> violations = validator.validate( p );
    
    // notBlank
    assertThat(violations).hasSize(1);
  }
  
  @Test
  public void testValidatePerson_firstNameNull() {
    String tooShortFirstName = null;
    Person p = new Person(tooShortFirstName, "ok");
    
    Set<ConstraintViolation<Person>> violations = validator.validate( p );
    
    // notNull, notBlank
    assertThat(violations).hasSize(2);
  }
  
  @Test
  public void testValidatePerson_firstNameTooLong() {
    // 21 Zeichen
    String tooShortFirstName = "012345678901234567890";
    Person p = new Person(tooShortFirstName, "ok");
    
    Set<ConstraintViolation<Person>> violations = validator.validate( p );
    
    // max-size=20
    assertThat(violations).hasSize(1);
  }
}
