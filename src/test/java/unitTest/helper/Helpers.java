package unitTest.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;

public class Helpers {
    private Helpers() {
    }

    public static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper mapper() {
        return mapper;
    }

    public static String loadFile(String resourcePath) {
        try {
            return Resources.toString(Resources.getResource(resourcePath),
                    Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode toJson(String json) {
        try {
            return mapper.readValue(json, ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode loadJson(String resourcePath) {
        return toJson(loadFile(resourcePath));
    }


}
