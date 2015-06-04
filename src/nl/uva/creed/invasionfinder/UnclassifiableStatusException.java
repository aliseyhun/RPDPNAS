package nl.uva.creed.invasionfinder;

public class UnclassifiableStatusException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6758674339911866841L;

	public UnclassifiableStatusException() {
		super();
	}

	public UnclassifiableStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnclassifiableStatusException(String message) {
		super(message);
	}

	public UnclassifiableStatusException(Throwable cause) {
		super(cause);
	}

}
