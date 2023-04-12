package animals.utils;

import animals.model.Node;
import animals.model.Verb;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {

    private final Random random = new Random();
    private final ResourceBundle appResources;
    private final ResourceBundle patterns;
    private final Map<String, Pattern> patternMap;
    public Utils(ResourceBundle appResources, ResourceBundle patterns) {
        this.appResources = appResources;
        this.patterns = patterns;
        patternMap = patterns.keySet().stream()
                .filter(key -> !key.endsWith(".replace"))
                .collect(Collectors.toUnmodifiableMap(key -> key,
                        key -> Pattern.compile(patterns.getString(key),
                                Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE))
                );
    }

    public String checkAnswer(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            String answer = input.replaceAll("[.!]$", "");

            if (answer.matches(patterns.getString("positiveAnswer.isCorrect"))) {
                return "y";
            } else if (answer.matches(patterns.getString("negativeAnswer.isCorrect"))) {
                return "n";
            } else {
                askAnswerAgain();
            }
        }
    }

    void askAnswerAgain() {
        String[] askAgain = appResources.getString("ask.again").split("\\f");
        System.out.println(getRandomString(askAgain));
    }

    public void sayGoodbye() {
        String[] farewell = appResources.getString("farewell").split("\\f");
        System.out.println(getRandomString(farewell));
    }

    public String checkFormat(String animal) {

        if (animal.trim().matches(".+unicorn")) {
            return animal.trim();
        }

        if (animal.matches(patterns.getString("animal.isCorrect"))) {
            return animal;
        }

        return applyPattern("animal", animal);
    }

    public String addArticle(String animal) {
        if (animal.trim().matches(".+unicorn")) {
            return animal.trim();
        }

        return applyPattern("animal", animal);
    }

    public void greeting() {
        LocalTime time = LocalTime.now();
        String greeting;

        if (time.isAfter(LocalTime.parse(appResources.getString("morning.time.after"))) &&
                time.isBefore(LocalTime.parse(appResources.getString("morning.time.before")))) {
            greeting = appResources.getString("greeting.morning");
        } else if (time.isAfter(LocalTime.parse(appResources.getString("afternoon.time.after"))) &&
                time.isBefore(LocalTime.parse(appResources.getString("afternoon.time.before")))) {
            greeting = appResources.getString("greeting.afternoon");
        } else if (time.isAfter(LocalTime.parse(appResources.getString("evening.time.after"))) &&
                time.isBefore(LocalTime.parse(appResources.getString("evening.time.before")))) {
            greeting = appResources.getString("greeting.evening");
        } else if (time.isAfter(LocalTime.parse(appResources.getString("early.time.after"))) &&
                time.isBefore(LocalTime.parse(appResources.getString("early.time.before")))) {
            greeting = appResources.getString("greeting.early");
        } else {
            greeting = appResources.getString("greeting.night");
        }

        System.out.println(greeting);
    }

    public boolean checkStatement(String statement) {
        Pattern pattern = Pattern.compile(patterns.getString("statement.isCorrect"),
                Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(statement);

        return matcher.matches();
    }

    public void printExampleStatement() {
        System.out.println(appResources.getString("statement.error"));
    }

    public String readFact(Scanner scanner, String animal1, String animal2) {
        String statement;

        do {
            System.out.println(MessageFormat.format(appResources.getString("statement.prompt"),
                            addArticle(animal1.toLowerCase()), addArticle(animal2.toLowerCase())));

            statement = scanner.nextLine();

            if (!checkStatement(statement)) {
                printExampleStatement();
            } else {
                break;
            }
        } while (true);

        return statement;
    }

    public String getQuestionAndPrintFact(String statement, String answer, Node animal, Node animal2) {
        String question;

        System.out.println(appResources.getString("game.learned"));

        String fullFact1;
        String fullFact2;

        // Fact for save in tree
        fullFact1 = capitalize(applyPattern("n".equals(answer) ? "statement" : "negative", statement));
        fullFact2 = capitalize(applyPattern("n".equals(answer) ? "negative" : "statement", statement));

        animal.setFact(fullFact1 + ".");
        animal2.setFact(fullFact2 + ".");

        //Output facts about animals
        String anim1 = applyPattern("definite", applyPattern("animal", animal.getNodeName()));
        String anim2 = applyPattern("definite", applyPattern("animal", animal2.getNodeName()));

        String out1 = applyPattern("animalFact", fullFact1.toLowerCase());
        String out2 = applyPattern("animalFact", fullFact2.toLowerCase());

        System.out.printf("  - " + out1 + ".\n", capitalize(anim1));
        System.out.printf("  - " + out2 + ".\n", capitalize(anim2));

        System.out.printf(capitalize(applyPattern("animalFact", statement)), animal2.getNodeName());

        System.out.println(appResources.getString("game.distinguish"));

        question = capitalize(applyPattern("question", statement.toLowerCase()));

        System.out.println("  - " + question);
        System.out.println(getRandomString(appResources
                .getString("game.thanks")
                .split("\\f")));
        System.out.println();

        return question;
    }

    public String getRandomString(String[] array) {
        return array[random.nextInt(array.length)];
    }

    public String applyPattern(String rule, String str) {
        for (int i = 1; ; i++) {
            var key = rule + "." + i;
            var pattern = patternMap.get(key + ".pattern");

            if (pattern == null) {
                return str;
            }

            var matcher = pattern.matcher(str);

            if (matcher.matches()) {
                return matcher.replaceFirst(patterns.getString(key + ".replace"));
            }
        }
    }

    public String capitalize(String str) {

        return str.substring(0, 1).toUpperCase() +
                str.substring(1);
    }
}
