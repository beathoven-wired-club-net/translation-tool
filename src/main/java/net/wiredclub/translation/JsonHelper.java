package net.wiredclub.translation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashSet;
import java.util.Set;

public class JsonHelper {

	// private static final Logger LOG = LoggerFactory.getLogger(JsonHelper.class);

	private final ObjectMapper objectMapper;
	private final DefaultPrettyPrinter printer;

	public JsonHelper() {
		this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

		printer = new DefaultPrettyPrinter();
		DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("\t", DefaultIndenter.SYS_LF);
		printer.indentObjectsWith(indenter);
		printer.indentArraysWith(indenter);
	}

	public JsonNode convertStringToJson(String json) throws TranslationJsonProcessingException {
		try {
			return objectMapper.readTree(json);
		} catch (JsonProcessingException e) {
			throw new TranslationJsonProcessingException(e.getMessage());
		}
	}

	public String convertJsonToString(JsonNode jsonNode) throws TranslationJsonProcessingException {
		try {
			String content = objectMapper.writer(printer).writeValueAsString(jsonNode);
			// remove spaces between '"' and ':' or '{', jacksons output is '"key" : "value"'.
			// we want it '"key": "value"' and '"key": {'. This is a simple approach but could be also dangerous.
			return content.replace("\" : \"", "\": \"").replace("\" : {", "\": {");
		} catch (JsonProcessingException e) {
			throw new TranslationJsonProcessingException(e.getMessage());
		}
	}

	public ArrayNode createNewTranslationPatch() {
		return objectMapper.createArrayNode();
	}

	public ObjectNode createPatchOperationAdd(String path, JsonNode value) {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("op", "add");
		objectNode.put("path", path);
		objectNode.set("value", value);

		return objectNode;
	}

	public ObjectNode createPatchOperationRemove(String path) {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("op", "remove");
		objectNode.put("path", path);

		return objectNode;
	}

	public ObjectNode createPatchOperationReplace(String path, String value) {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("op", "replace");
		objectNode.put("path", path);
		objectNode.put("value", value);

		return objectNode;
	}

	public Set<String> extractLanguages(JsonNode json) {
		Set<String> languages = new HashSet<>();
		if (json.isArray()) {
			ArrayNode languagesArray = (ArrayNode) json;
			for (int i = 0; i < languagesArray.size(); i++) {
				languages.add(languagesArray.get(i).get("language").asText());
			}
		}
		return languages;
	}

	public String extractTranslation(JsonNode json, String defaultText) {
		JsonNode translations = json.get("translations");
		String translation = defaultText;
		if (translations.isArray() && translations.size() == 1) {
			translation = translations.get(0).get("text").asText();
		}
		return translation;
	}
}
