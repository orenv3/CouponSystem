package myExceptions;

public class MyCustomerException extends RuntimeException {

	/**
	 * Class that describes exceptions to throw for CustomerDAO
	 */
	private static final long serialVersionUID = 1L;

	public MyCustomerException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyCustomerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MyCustomerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
