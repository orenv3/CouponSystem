package myExceptions;

public class MyCompanyExceptions extends RuntimeException {

	/**
	 * Class that describes exceptions to throw for CompanyDAO
	 */
	private static final long serialVersionUID = 1L;

	public MyCompanyExceptions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyCompanyExceptions(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MyCompanyExceptions(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
