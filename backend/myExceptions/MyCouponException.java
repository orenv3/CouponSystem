package myExceptions;

public class MyCouponException extends RuntimeException {

	/**
	 * Class that describes exceptions to throw for CouponDAO
	 */
	private static final long serialVersionUID = 1L;

	public MyCouponException() {
		super();
	}

	public MyCouponException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyCouponException(String message) {
		super(message);

	}

	@Override
	public synchronized Throwable getCause() {
		// TODO Auto-generated method stub
		return super.getCause();
	}

	@Override
	public synchronized Throwable initCause(Throwable arg0) {
		// TODO Auto-generated method stub
		return super.initCause(arg0);
	}

}
