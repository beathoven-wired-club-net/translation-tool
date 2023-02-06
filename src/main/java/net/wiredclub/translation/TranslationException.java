package net.wiredclub.translation;

public class TranslationException extends Exception {

	private final TranslationStatusCode statusCode;

	public TranslationException(TranslationStatusCode statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public TranslationException(String message, TranslationStatusCode statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public TranslationStatusCode statusCode() {
		return statusCode;
	}
}
