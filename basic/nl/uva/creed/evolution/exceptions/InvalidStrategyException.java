/**
 * 
 */
package nl.uva.creed.evolution.exceptions;

/**
 * @author jgarcia
 *
 */
public class InvalidStrategyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3348877066402005532L;

	/**
	 * 
	 */
	public InvalidStrategyException() {
	}

	/**
	 * @param message
	 */
	public InvalidStrategyException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InvalidStrategyException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidStrategyException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
