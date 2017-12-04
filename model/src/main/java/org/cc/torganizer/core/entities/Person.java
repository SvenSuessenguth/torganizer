package org.cc.torganizer.core.entities;

import java.time.LocalDate;
import javax.json.bind.annotation.JsonbProperty;

/**
 * A person represents an man or a woman with core data.
 */
public class Person extends Entity {

  private String firstName;

  private String lastName;

  @JsonbProperty(nillable = true)
  private LocalDate dateOfBirth;

  private Gender gender = Gender.UNKNOWN;

  public Person() {
    // gem. Bean-Spec.
  }

  public Person(String newFirstName, String newLastName) {
    this.firstName = newFirstName;
    this.lastName = newLastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String newFirstName) {
    this.firstName = newFirstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String newLastName) {
    this.lastName = newLastName;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate newDateOfBirth) {
    this.dateOfBirth = newDateOfBirth;
  }
  
  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender newGender) {
    this.gender = newGender;
  }
}
