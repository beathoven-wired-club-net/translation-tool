package net.wiredclub.translation;

public class TranslationFileNotFoundException extends TranslationException {
	public TranslationFileNotFoundException(String message) {
		super(message, TranslationStatusCode.STATUS_FILE_NOT_FOUND);
	}
}
