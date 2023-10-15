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
		
		public class PostException extends Exception {
			public PostException(String message) {
				super(message);
			}
		}
	}
}

