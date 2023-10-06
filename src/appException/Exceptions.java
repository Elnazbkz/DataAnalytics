package appException;
public class Exceptions {
	public class EmailExistsException extends Exception {

	    public EmailExistsException() {
	        super("Email address already exists.");
	    }

	    public EmailExistsException(String message) {
	        super(message);
	    }
	}
}

