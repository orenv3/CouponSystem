package myExceptions;

public class MyShutddownException extends RuntimeException {

	public MyShutddownException() {
		super();
	}

	public MyShutddownException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MyShutddownException(String arg0) {
		super(arg0);
	}

}
