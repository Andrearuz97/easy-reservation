package Capstone.easyreservation.exception;

@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}

}