package net.wiredclub.translation;

public class TranslationJsonProcessingException extends TranslationException {
	public TranslationJsonProcessingException(String message) {
		super(message, TranslationStatusCode.STATUS_JSON_INVALID);
	}
}
