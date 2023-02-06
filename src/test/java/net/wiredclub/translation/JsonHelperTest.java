package net.wiredclub.translation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonHelperTest {

	// private static final Logger LOG = LoggerFactory.getLogger(JsonHelperTest.class);

	@Test
	void testCreateDiffPatchWithInvalidFiles() {
		TranslationJsonProcessingException thrownException = assertThrows(TranslationJsonProcessingException.class,
				() -> new JsonHelper().convertStringToJson("kein json"));

		assertEquals("de.hype.hypeio.translation.TranslationJsonProcessingException",
				thrownException.getClass().getName());
		// LOG.info(thrownException.getMessage());
	}
}
