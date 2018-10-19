package org.cc.torganizer.core.exceptions;

/**
 * Exceptions diesen Typs treten immer dann auf, wenn ein oder mehrere Opponents
 * in einem Kontext verwendet werden sollen, in dem sie ung\u00fcltig sind. Z.B.
 * darf in einem Match nicht zweimal der selbe Opponent zugewiesen werden.
 */
public class IllegalOpponentException extends RuntimeException {

	private static final long serialVersionUID = -7148242357110165108L;

	/**
	 * Bequemlichkeitskonstruktor.
	 *
	 * @param message Message
	 */
	public IllegalOpponentException(String message) {
		super(message);
	}
}
