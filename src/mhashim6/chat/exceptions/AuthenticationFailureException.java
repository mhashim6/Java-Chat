package mhashim6.chat.exceptions;

public class AuthenticationFailureException extends Exception {//TODO

	private static final long serialVersionUID = 815026662055731650L;

	@Override
	public String toString() {
		return "Failed to authenticate";
	}
}
