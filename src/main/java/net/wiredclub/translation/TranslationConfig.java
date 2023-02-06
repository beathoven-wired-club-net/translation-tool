package net.wiredclub.translation;

import java.util.Collections;
import java.util.Set;

public class TranslationConfig {

	private final String sourceLanguage;
	private final Set<String> targetLanguages;
	private final String translationsDirectory;
	private final String repositoryDirectory;

	TranslationConfig(String sourceLanguage, Set<String> targetLanguages, String translationsDirectory,
	                  String repositoryDirectory) {
		this.sourceLanguage = sourceLanguage;
		this.targetLanguages = Collections.unmodifiableSet(targetLanguages);
		this.translationsDirectory = translationsDirectory;
		this.repositoryDirectory = repositoryDirectory;
	}

	public String sourceLanguage() {
		return sourceLanguage;
	}

	public Set<String> targetLanguages() {
		return targetLanguages;
	}

	public String translationsDirectory() {
		return translationsDirectory;
	}

	public String repositoryDirectory() {
		return repositoryDirectory;
	}

	public String sourceFileName() {
		return translationsDirectory() + "/" + sourceLanguage() + "/main.json";
	}

	public String targetFileName(String targetLanguage) {
		return translationsDirectory() + "/" + targetLanguage + "/main.json";
	}
}
