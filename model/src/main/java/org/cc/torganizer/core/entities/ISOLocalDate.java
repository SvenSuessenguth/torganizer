package org.cc.torganizer.core.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * {@link LocalDate} unterst�tzt keine marshalling/unmarshalling von XML auf Objekt und zur�ck. 
 * Daher hier eine eigene Implementierung, die intern {@link LocalDate} verwendet.
 * @author u000349
 *
 */
@XmlRootElement(name="LocalDate")
@XmlAccessorType(XmlAccessType.FIELD)
public class ISOLocalDate {

  private Integer year;
  private Integer month;
  private Integer day;

  public ISOLocalDate() {
  }

  public ISOLocalDate(String isoString) {
    LocalDate parsed = LocalDate.parse(isoString, DateTimeFormatter.ISO_DATE);
    year = parsed.getYear();
    month = parsed.getMonthValue();
    day = parsed.getDayOfMonth();
  }

  public static ISOLocalDate of(int year, int month, int day) {
    ISOLocalDate isoLocalDate = new ISOLocalDate();
    isoLocalDate.year = year;
    isoLocalDate.month = month;
    isoLocalDate.day = day;

    return isoLocalDate;
  }

  public static ISOLocalDate fromString(String isoString) {
    LocalDate parsed = LocalDate.parse(isoString, DateTimeFormatter.ISO_DATE);
    ISOLocalDate isoLocalDate = new ISOLocalDate();
    isoLocalDate.year = parsed.getYear();
    isoLocalDate.month = parsed.getMonthValue();
    isoLocalDate.day = parsed.getDayOfMonth();

    return isoLocalDate;
  }

  public LocalDate toLocalDate() {
    return LocalDate.of(year, month, day);
  }

  public boolean isBefore(ISOLocalDate other) {
    return toLocalDate().isBefore(other.toLocalDate());
  }

  public boolean isAfter(ISOLocalDate other) {
    return toLocalDate().isAfter(other.toLocalDate());
  }

  public Integer getYear() {
    return year;
  }

  public Integer getMonth() {
    return month;
  }

  public Integer getDay() {
    return day;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((day == null) ? 0 : day.hashCode());
    result = prime * result + ((month == null) ? 0 : month.hashCode());
    result = prime * result + ((year == null) ? 0 : year.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ISOLocalDate other = (ISOLocalDate) obj;
    if (day == null) {
      if (other.day != null)
        return false;
    } else if (!day.equals(other.day))
      return false;
    if (month == null) {
      if (other.month != null)
        return false;
    } else if (!month.equals(other.month))
      return false;
    if (year == null) {
      if (other.year != null)
        return false;
    } else if (!year.equals(other.year))
      return false;
    return true;
  }

}
