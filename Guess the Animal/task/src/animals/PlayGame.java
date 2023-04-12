package animals;

import animals.model.Node;
import animals.utils.Utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PlayGame {

    private Node tree;
    private Scanner scanner;
    private final Utils utils;
    private final ResourceBundle appResources;

    public PlayGame(Node tree, Scanner scanner,
                    ResourceBundle appResources,
                    ResourceBundle patterns) {
        this.scanner = scanner;
        this.tree = tree;
        this.appResources = appResources;
        this.utils = new Utils(appResources, patterns);
    }

    public Node start() {

        Node currentNode;

/*        if (tree == null) {


            System.out.print("""
                    Wonderful! I've learned so much about animals!
                    Let's play a game!
                    """);
        } else {

        System.out.print("""
                I know a lot about animals.
                Let's play a game!
                """);*/

        currentNode = tree;
//        }
        System.out.println(appResources.getString("game.letsPlay"));

        do {
            System.out.println(appResources.getString("game.think"));
            System.out.println(appResources.getString("game.enter"));
            scanner = new Scanner(System.in);
            scanner.nextLine();

            Node parentNode = null;

            while (true) {
                if (currentNode.isLeaf()) {
                    guessAnimal(currentNode, parentNode);
                    break;
                } else {
                    System.out.println(currentNode.getNodeName());
                    String answer = utils.checkAnswer(scanner);

                    Node nextNode;

                    if ("y".equals(answer)) {
                        nextNode = currentNode.getLeft();
                    } else {
                        nextNode = currentNode.getRight();
                    }

                    parentNode = currentNode;
                    currentNode = nextNode;
                }
            }

            currentNode = tree;

            System.out.println(utils.getRandomString(
                    appResources.getString("game.again").split("\\f"))
            );
        } while (!"n".equals(utils.checkAnswer(scanner)));

        return tree;
    }

    private void giveUpNewAnimal(Scanner scanner, Node currentNode, Node previousNode) {
        System.out.println(appResources.getString("game.giveUp"));
        String animal = utils.checkFormat(scanner.nextLine().toLowerCase());
        Node newAnimal = new Node(animal);

        String fact = currentNode.getFact();

        String statement = utils.readFact(scanner, currentNode.getNodeName(), newAnimal.getNodeName());

        System.out.println(MessageFormat.format(appResources.getString("game.isCorrect"),
                utils.addArticle(newAnimal.getNodeName())));

        String answer = utils.checkAnswer(scanner);

        String question = utils.getQuestionAndPrintFact(statement, answer,
                currentNode, newAnimal);

        if (currentNode.isLeaf()) {
            Node newNode;

            if ("y".equals(answer)) {
                newNode = new Node(question, fact, newAnimal, currentNode);
            } else {
                newNode = new Node(question, fact, currentNode, newAnimal);
            }

            if (previousNode != null) {
                if (previousNode.getRight().equals(currentNode)) {
                    previousNode.setRight(newNode);
                } else {
                    previousNode.setLeft(newNode);
                }
            } else {
                tree = newNode;
            }
        }
    }

    private void guessAnimal(Node currentNode, Node previousNode) {

        // Is it cat?
        System.out.println(utils.capitalize(
                utils.applyPattern("guessAnimal",
                        utils.addArticle(currentNode.getNodeName())))
        );

        if ("y".equals(utils.checkAnswer(scanner))) {
            System.out.println(utils.getRandomString(appResources.getString("game.win")
                    .split("\\f")));
        } else {
            giveUpNewAnimal(scanner, currentNode, previousNode);
        }
    }
}
