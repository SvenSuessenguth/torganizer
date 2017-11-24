package org.cc.torganizer.core.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Restriktion auf Teilnahme an einem Modus wegen der Altersbeschraenkung.
 */
@XmlRootElement(name = "AgeRestriction")
@XmlAccessorType(XmlAccessType.NONE)
public class AgeRestriction extends Restriction{

	/**
	 * liegt zeitlich NACH dem minDateOfBirth (hat h\u00f6heren Jahres-, Monats-
	 * oder Tageswert).
	 */
	private LocalDate maxDateOfBirth;

	/**
	 * liegt zeitlich VOR dem maxDateOfBirth (hat niedrigeren Jahres-, Monats- oder
	 * Tageswert).
	 */
	private LocalDate minDateOfBirth;

	/**
	 * Default.
	 */
	public AgeRestriction() {
		// gem. Bean-Spec.
	}

	public LocalDate getMaxDateOfBirth() {
		return maxDateOfBirth;
	}

	public void setMaxDateOfBirth(LocalDate newMaxDateOfBirth) {
		this.maxDateOfBirth = newMaxDateOfBirth;
	}

	@XmlAttribute
	public String getMaxDateOfBirthISO() {
		return maxDateOfBirth == null ? "" : maxDateOfBirth.format(DateTimeFormatter.ISO_DATE);
	}

	public void setMaxDateOfBirthISO(String maxDateOfBirthISO) {
		maxDateOfBirth = maxDateOfBirthISO == null ? null
				: LocalDate.parse(maxDateOfBirthISO, DateTimeFormatter.ISO_DATE);
	}

	public LocalDate getMinDateOfBirth() {
		return minDateOfBirth;
	}

	public void setMinDateOfBirth(LocalDate newMinDateOfBirth) {
		this.minDateOfBirth = newMinDateOfBirth;
	}

	@XmlAttribute
	public String getMinDateOfBirthISO() {
		return minDateOfBirth == null ? "" : minDateOfBirth.format(DateTimeFormatter.ISO_DATE);
	}

	public void setMinDateOfBirthISO(String minDateOfBirthISO) {
		minDateOfBirth = minDateOfBirthISO == null ? null
				: LocalDate.parse(minDateOfBirthISO, DateTimeFormatter.ISO_DATE);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isRestricted(Opponent opponent) {
		// wenn es eine Ober- bzw. eine Untergrenze fuer das Geburtsdatum
		// dann muss diese auch eingehalten werden

		// restricted is true, if one of the opponents players is restricted
		boolean restricted = false;

		for (Player player : opponent.getPlayers()) {
			LocalDate dateOfBirth = player.getPerson().getDateOfBirth();

			// no dateOfBirth is not restricted
			if (dateOfBirth == null) {
				return false;
			}

			if (maxDateOfBirth != null && maxDateOfBirth.isBefore(dateOfBirth)) {
				restricted = true;
			}
			if (minDateOfBirth != null && minDateOfBirth.isAfter(dateOfBirth)) {
				restricted = true;
			}
		}
		return restricted;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "AgeRestriction with maxDateOfBirth='" + maxDateOfBirth + "' and minDateOfBirth='" + minDateOfBirth
				+ "'";
	}
}
