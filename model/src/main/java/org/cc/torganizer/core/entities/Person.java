package org.cc.torganizer.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.cc.torganizer.core.exceptions.IllegalDateOfBirthException;

import java.time.LocalDate;

import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

/**
 * A person represents a man or a woman with core data.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person extends Entity {
  @Builder.Default
  private String firstName = "firstName";
  @Builder.Default
  private String lastName = "lastName";
  @Builder.Default
  private LocalDate dateOfBirth = LocalDate.now().minusYears(20);
  @Builder.Default
  private Gender gender = UNKNOWN;


  public Person(Long id) {
    setId(id);
  }

  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
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
    this.gender = gender == null ? UNKNOWN : gender;
  }

  /**
   * Calculating the age of the person in years. If no dato of birth is given, the age is NULL.
   */
  public Integer getAge() {
    if (dateOfBirth == null) {
      return null;
    }

    var ageInYears = LocalDate.now().getYear() - dateOfBirth.getYear();

    if (ageInYears < 1) {
      throw new IllegalDateOfBirthException("Age of " + this + " must be greate than 0");
    }

    return ageInYears;
  }

  /**
   * Checking, if the person have the given gender.
   */
  public boolean fitsGender(Gender gender) {
    // no statement can be made, so return true
    if (gender == null || UNKNOWN == gender || this.gender == UNKNOWN) {
      return true;
    }

    return gender == this.gender;
  }
}
