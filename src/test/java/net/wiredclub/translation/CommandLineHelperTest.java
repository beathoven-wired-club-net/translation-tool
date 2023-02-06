package net.wiredclub.translation;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static net.wiredclub.translation.TranslationStatusCode.STATUS_INVALID_ARGUMENT;
import static net.wiredclub.translation.TranslationToolTest.TEST_TRANSLATIONS_DIRECTORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandLineHelperTest {

	@Test
	void testGetTranslationConfigWithoutArguments() throws TranslationException, IOException {
		String[] noArgs = {};

		Set<String> targetLanguages = new HashSet<>() {{
			add("de");
			add("en");
			add("fr");
			add("nl");
		}};

		FileHelper fileHelperMock = mock(FileHelper.class);
		when(fileHelperMock.discoverLanguageDirectories(any())).thenReturn(targetLanguages);

		CommandLineHelper commandLineHelper = new CommandLineHelper(new DeepLHelper(), fileHelperMock);
		TranslationConfig translationConfig = commandLineHelper.getTranslationConfig(noArgs);

		assertNotNull(translationConfig);
		assertEquals("en", translationConfig.sourceLanguage());
		assertEquals(Set.of("de", "fr", "nl"), translationConfig.targetLanguages());
		assertEquals("frontend/public/translations", translationConfig.translationsDirectory());
		assertEquals(".", translationConfig.repositoryDirectory());
	}

	@Test
	void testGetTranslationConfigWithValidArguments() throws TranslationException, IOException {
		String[] args = {
				"-s", "en",
				"-t", "de",
				"-p", TEST_TRANSLATIONS_DIRECTORY,
				"-r", ".."
		};

		TranslationConfig translationConfig = new CommandLineHelper().getTranslationConfig(args);

		assertNotNull(translationConfig);
		assertEquals("en", translationConfig.sourceLanguage());
		assertEquals(Set.of("de"), translationConfig.targetLanguages());
		assertEquals(TEST_TRANSLATIONS_DIRECTORY, translationConfig.translationsDirectory());
		assertEquals("..", translationConfig.repositoryDirectory());
	}

	@Test
	void testGetTranslationConfigWithShortSyntax() throws TranslationException, IOException {
		String[] args = {
				"-s", "en"
		};

		Set<String> targetLanguages = new HashSet<>() {{
			add("de");
			add("en");
			add("fr");
			add("nl");
		}};

		FileHelper fileHelperMock = mock(FileHelper.class);
		when(fileHelperMock.discoverLanguageDirectories(any())).thenReturn(targetLanguages);

		CommandLineHelper commandLineHelper = new CommandLineHelper(new DeepLHelper(), fileHelperMock);
		TranslationConfig translationConfig = commandLineHelper.getTranslationConfig(args);

		assertNotNull(translationConfig);
		assertEquals("en", translationConfig.sourceLanguage());
		assertEquals(Set.of("de", "fr", "nl"), translationConfig.targetLanguages());
		assertEquals("frontend/public/translations", translationConfig.translationsDirectory());
		assertEquals(".", translationConfig.repositoryDirectory());
	}

	@Test
	void testGetTranslationConfigWithInvalidArguments() throws ParseException, IOException, TranslationException {
		String[] invalidArgs = {"-x"};

		CommandLineHelper commandLineHelperMock = mock(CommandLineHelper.class);
		when(commandLineHelperMock.getTranslationConfig(invalidArgs)).thenCallRealMethod();
		doThrow(ParseException.class).when(commandLineHelperMock).parseArguments(any(), eq(invalidArgs));

		TranslationException thrownException =
				assertThrows(TranslationException.class, () -> commandLineHelperMock.getTranslationConfig(invalidArgs));

		assertEquals("de.hype.hypeio.translation.TranslationException", thrownException.getClass().getName());
		assertEquals(STATUS_INVALID_ARGUMENT, thrownException.statusCode());

		verify(commandLineHelperMock, times(1)).displayHelp(any());
	}

	@Test
	void testParseArgumentsWithInvalidSourceLanguage() {
		String[] invalidArgs = {
				"-s", "wrong language", // this is also the directory where translations live
				"-t", "de",
				"-p", TEST_TRANSLATIONS_DIRECTORY
		};

		CommandLineHelper commandLineHelper = new CommandLineHelper();

		ParseException thrownException = assertThrows(ParseException.class,
				() -> commandLineHelper.parseArguments(commandLineHelper.defineOptions(), invalidArgs));

		assertEquals("org.apache.commons.cli.ParseException", thrownException.getClass().getName());
	}

	@Test
	void testParseArgumentsWithInvalidTargetLanguage() {
		String[] invalidArgs = {
				"-s", "en",
				"-t", "wrong language", // this is also the directory where translations live
				"-p", TEST_TRANSLATIONS_DIRECTORY
		};

		CommandLineHelper commandLineHelper = new CommandLineHelper();

		ParseException thrownException = assertThrows(ParseException.class,
				() -> commandLineHelper.parseArguments(commandLineHelper.defineOptions(), invalidArgs));

		assertEquals("org.apache.commons.cli.ParseException", thrownException.getClass().getName());
	}
}
