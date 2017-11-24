package org.cc.torganizer.core.entities;

import static org.cc.torganizer.core.util.LangUtil.capitalize;

import java.util.Locale;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * System, nach dem gespielt werden soll.
 */
@XmlRootElement(name = "System")
@XmlAccessorType(XmlAccessType.FIELD)
public enum System {

  /** Doppel-KO. */
  DOUBLE_ELIMINATION,

  /** Einfaches KO. */
  SINGLE_ELIMINATION,

  /** Gruppenspiele. */
  ROUND_ROBIN;
  /**
   * Name wird formatiert, wobei die jeweils ersten Buchstaben (aller erster und
   * die Buchstaben nach einem Unterstrich) gro\u00df geschrieben werden und alle
   * anderen klein.
   *
   * @return Name des Enums.
   */
  public String getFormattedName() {
    StringTokenizer st = new StringTokenizer(this.name(), "_");
    StringBuilder formattedName = new StringBuilder();

    while (st.hasMoreTokens()) {
      String s = st.nextToken();
      s = s.toLowerCase(Locale.getDefault());
      s = capitalize(s);
      formattedName.append(s);
    }

    return formattedName.toString();
  }
}
