package animals;

import animals.model.Node;
import animals.utils.FileUtils;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Node tree = null;
        String fileName = "";
        String language = System.getProperty("user.language", "");

        if (!"eo".equals(language)) {
            language = "";
        }

//        String language = getParameterValue("user.language=(\\w+)\\b", args);
        String type = getParameterValue("-type (\\w+)\\b", args);

        ResourceBundle appResource = ResourceBundle.getBundle("messages",
                new Locale(language));
        ResourceBundle patterns = ResourceBundle.getBundle("patterns",
                new Locale(language));

        if (!type.isEmpty()) {
            fileName = String.format("animals%s.%s", language.isEmpty() ? "" : "_" + language, type);
            tree = FileUtils.loadFile(type, fileName);
        }

//        Arrays.stream(args).forEach(System.out::println);

        tree = (new Menu(tree, appResource, patterns)).start();

        if (!type.isEmpty()) {
            FileUtils.saveFile(type, fileName, tree);
        }
    }

    private static String argsToString(String[] args) {
        StringBuilder sb = new StringBuilder();

        for (String str : args) {
            sb.append(" ").append(str);
        }
        return sb.toString();
    }

    private static String getParameterValue(String pattern, String[] args) {
        String val = "";
        String arg = argsToString(args);

        Pattern typePattern = Pattern.compile(pattern);
        Matcher typeMatcher = typePattern.matcher(arg);

        if (typeMatcher.find()) {
            val = typeMatcher.group(1);
        }

        return val;
    }
}

