package net.wiredclub.translation;

public enum TranslationStatusCode {

	STATUS_OK(0),
	STATUS_HELP(1),
	STATUS_MISSING_ARGUMENTS(2),
	STATUS_INVALID_ARGUMENT(3),
	STATUS_FILE_NOT_FOUND(4),
	STATUS_JSON_INVALID(5),
	STATUS_TRANSLATION_FILE_INVALID(6),
	STATUS_BAD_AS_HELL(666);

	private final int exitCode;

	TranslationStatusCode(int exitCode) {
		this.exitCode = exitCode;
	}

	public int exitCode() {
		return exitCode;
	}
}
