package animals.utils;

import animals.model.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static Node loadFile(String format, String fileName) {
        Node tree = null;
        ObjectMapper objectMapper = getObjectMapper(format);

        try {
            File file = new File(fileName);

            if (file.isFile()) {
                tree = objectMapper.readValue(file, Node.class);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tree;
    }

    public static void saveFile(String format, String fileName, Node tree) {
        ObjectMapper objectMapper = getObjectMapper(format);

        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), tree);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static ObjectMapper getObjectMapper(String format) {
        ObjectMapper objectMapper = null;

        switch (format) {
            case "json" -> objectMapper = new JsonMapper();
            case "xml" -> objectMapper = new XmlMapper();
            case "yaml" -> objectMapper = new YAMLMapper();
        }
        return objectMapper;
    }
}
