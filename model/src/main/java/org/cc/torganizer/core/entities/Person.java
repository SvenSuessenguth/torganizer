package org.cc.torganizer.core.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A person represents an man or a woman with core data.
 * 
 * @author admin (latest modification by $Author: u000349 $) $Rev: 1365 $
 *         $LastChangedDate: 2017-10-13 08:53:18 +0200 (Fr, 13 Okt 2017) $
 * @version $Id: $
 */
@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.NONE)
public class Person {
  
  @XmlAttribute
  private Long id;

  @XmlAttribute
  private String firstName;

  @XmlAttribute
  private String lastName;

  private LocalDate dateOfBirth;

  @XmlAttribute
  private Gender gender = Gender.UNKNOWN;

  /**
   * Default Constructor.
   */
  public Person() {
    // gem. Bean-Spec.
  }

  /**
   * Bequemlichkeitskonstruktor.
   * 
   * @param newFirstName
   *            Vorname der Person
   * @param newLastName
   *            Nachname der Person
   */
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
  
  @XmlAttribute
  public String getDateOfBirthISO() {
    return dateOfBirth!=null?dateOfBirth.format(DateTimeFormatter.ISO_DATE):"";
  }
  public void setDateOfBirthISO(String dateOfBirthISO) {
    if(dateOfBirthISO==null) {
      dateOfBirth = null;
    }
    
    dateOfBirth = LocalDate.parse(dateOfBirthISO, DateTimeFormatter.ISO_DATE);
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender newGender) {
    this.gender = newGender;
  }

  /** {@inheritDoc} */
  public Long getId() {
    return id;
  }

  public void setId(Long newId) {
    this.id = newId;
  }
}
