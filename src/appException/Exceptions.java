package appException;
public class Exceptions {
	public class EmailExistsException extends Exception {
	    public EmailExistsException(String message) {
	        super(message);
	    }
	}
	
	public class UserLoginInvalid extends Exception {
	    public UserLoginInvalid(String message) {
	        super(message);
	    }
	}
	
	public class FailedUpdateException extends Exception {
		public FailedUpdateException(String message) {
			super(message);
		}
	}
	
	public class PostIDInvalid extends Exception {
		public PostIDInvalid(String message) {
			super(message);
		}
	}
}

