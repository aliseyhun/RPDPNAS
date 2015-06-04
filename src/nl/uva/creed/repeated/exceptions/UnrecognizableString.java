/**
 * 
 */
package nl.uva.creed.repeated.exceptions;

/**
 * @author jgarcia
 *
 */
public class UnrecognizableString extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UnrecognizableString() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnrecognizableString(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnrecognizableString(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnrecognizableString(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
