package net.wiredclub.translation;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeepLHelperTest {

	private static final Logger LOG = LoggerFactory.getLogger(DeepLHelperTest.class);

	private final DeepLHelper deepLHelper = new DeepLHelper();

	@Test
	void testUsage() throws IOException, TranslationJsonProcessingException {
		DeepLHelper.DeepLUsage usage = deepLHelper.usage();

		assertNotNull(usage);
		LOG.info("Character count: {}", usage.characterCount());
		LOG.info("Character limit: {}", usage.characterLimit());
	}

	@Test
	void testSourceLanguages() throws IOException, TranslationJsonProcessingException {
		List<String> sourceLanguages = deepLHelper.sourceLanguages();

		assertNotNull(sourceLanguages);
		LOG.info("Supported source languages: {}", sourceLanguages);
		assertTrue(sourceLanguages.contains("en"), "EN must be part of source languages.");
		assertTrue(sourceLanguages.contains("de"), "DE must be part of source languages.");
		assertTrue(sourceLanguages.contains("fr"), "FR must be part of source languages.");
	}

	@Test
	void testTargetLanguages() throws IOException, TranslationJsonProcessingException {
		List<String> targetLanguages = deepLHelper.targetLanguages();

		assertNotNull(targetLanguages);
		LOG.info("Supported target languages: {}", targetLanguages);
		assertTrue(targetLanguages.contains("en-gb"), "EN-GB must be part of target languages.");
		assertTrue(targetLanguages.contains("de"), "DE must be part of target languages.");
		assertTrue(targetLanguages.contains("fr"), "FR must be part of target languages.");
	}

	@Test
	void testTranslate() throws IOException, TranslationJsonProcessingException {
		String translatedText = deepLHelper.translate("hello", "en", "de");

		assertNotNull(translatedText);
		assertEquals("hallo", translatedText);
	}

	@Test
	void testWrapTextToTranslate() {
		assertEquals("outside",
				deepLHelper.wrapTextToTranslate("outside"));

		assertEquals("outside <donut>{{inside}}</donut>",
				deepLHelper.wrapTextToTranslate("outside {{inside}}"));

		assertEquals("outside <donut>{{inside}}</donut> and <donut>{{ inside spaces }}</donut>",
				deepLHelper.wrapTextToTranslate("outside {{inside}} and {{ inside spaces }}"));

		assertEquals("outside <donut>{{- and many things inside</br> }}</donut>",
				deepLHelper.wrapTextToTranslate("outside {{- and many things inside</br> }}"));
	}

	@Test
	void testWrapAndUnwrapTranslation() {
		String text = "outside {{inside}} and {{- and <b>many things</b> inside </br> }}.";

		String wrappedText = deepLHelper.wrapTextToTranslate(text);
		LOG.info(wrappedText);
		String unwrappedText = deepLHelper.unwrapTranslation(wrappedText);
		LOG.info(unwrappedText);

		assertEquals(text, unwrappedText);
	}
}
