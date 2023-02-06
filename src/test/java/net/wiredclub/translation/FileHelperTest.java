package net.wiredclub.translation;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileHelperTest {

	// private static final Logger LOG = LoggerFactory.getLogger(FileHelperTest.class);

	@Test
	void testReadPreviousFileFromHistory() throws IOException, TranslationFileNotFoundException {
		String fileName = "frontend/public/translations/en/main.json";
		String file = new FileHelper().readPreviousFileFromHistory("../..", fileName, 1);

		assertNotNull(file);
		// LOG.info("{}: {}", fileName, file);
	}

	@Test
	void testReadPreviousFileFromHistoryAndFileNotFoundException() {
		String fileName = "not found";

		TranslationFileNotFoundException thrownException = assertThrows(TranslationFileNotFoundException.class,
				() -> new FileHelper().readPreviousFileFromHistory("../..", fileName, 1));

		assertEquals("de.hype.hypeio.translation.TranslationFileNotFoundException", thrownException.getClass().getName());
		assertEquals("not found", thrownException.getMessage());
	}
}
