package org.cc.torganizer.core.entities;

import static org.cc.torganizer.core.util.LangUtil.capitalize;

import java.util.Locale;
import java.util.StringTokenizer;


/**
 * System, nach dem gespielt werden soll.
 */
public enum System {

  /** Doppel-KO. */
  DOUBLE_ELIMINATION,

  /** Einfaches KO. */
  SINGLE_ELIMINATION,

  /** Gruppenspiele. */
  ROUND_ROBIN;
}
