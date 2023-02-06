package net.wiredclub.translation;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static net.wiredclub.translation.TranslationStatusCode.STATUS_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TranslationToolTest {

	public static final String TEST_TRANSLATIONS_DIRECTORY = "tools/src/test/resources/translations";

	// @Test
	void testMainWithValidArguments() {
		String[] args = {
				"-s", "en",
				"-t", "de",
				"-p", TEST_TRANSLATIONS_DIRECTORY,
				"-r", ".."
		};

		assertEquals(STATUS_OK, new TranslationTool().run(args));
	}

	@Test
	void testCreateDiffPatch() throws IOException, TranslationFileNotFoundException, TranslationJsonProcessingException {
		String sourceLanguage = "en";
		String sourceFile = TEST_TRANSLATIONS_DIRECTORY + "/" + sourceLanguage + "/main.json";

		FileHelper fileHelperMock = mock(FileHelper.class);
		when(fileHelperMock.readPreviousFileFromHistory("..", sourceFile, 1)).thenReturn("{}");
		when(fileHelperMock.readFile(sourceFile)).thenReturn("{ \"f1\" : \"v1\" }");

		JsonHelper jsonHelper = new JsonHelper();
		JsonNode sourceJson = jsonHelper.convertStringToJson(fileHelperMock.readFile(sourceFile));

		TranslationTool tt = new TranslationTool(jsonHelper, null, fileHelperMock, null);
		JsonNode patch = tt.createDiffPatch("..", sourceFile, sourceJson);

		assertNotNull(patch);
		assertEquals(1, patch.size());
		assertEquals("add", patch.get(0).get("op").asText());
		assertEquals("/f1", patch.get(0).get("path").asText());
		assertEquals("v1", patch.get(0).get("value").asText());
	}
}
