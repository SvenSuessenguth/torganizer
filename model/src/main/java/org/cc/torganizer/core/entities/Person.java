package org.cc.torganizer.core.entities;

import java.time.LocalDate;

/**
 * A person represents an man or a woman with core data.
 */
public class Person extends Entity {

  private String firstName;

  private String lastName;

  private LocalDate dateOfBirth;

  private Gender gender = Gender.UNKNOWN;

  public Person() {
    // gem. Bean-Spec.
  }

  public Person(Long id) {
    setId(id);
  }

  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * Convenience constructor.
   */
  public Person(String firstName, String lastName, LocalDate dateOfBirth, Gender gender) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.gender = gender;
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

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Gender getGender() {
    return gender;
  }

  /**
   * Setting the gender and asserting, that the gender is not <code>null</code>.
   */
  public void setGender(Gender gender) {
    assert this.gender != null;

    this.gender = gender;
  }
}
