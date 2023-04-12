package animals;

import animals.model.Node;
import animals.utils.NodeUtils;
import animals.utils.Utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Menu {

    private Node tree;
    private Scanner scanner = new Scanner(System.in);
    private final Utils utils;
    private final ResourceBundle appResources;
    private final ResourceBundle patterns;
    private static final NodeUtils nodeUtils = new NodeUtils();


    public Menu(Node tree, ResourceBundle appResources, ResourceBundle patterns) {
        this.tree = tree;
        this.appResources = appResources;
        this.patterns = patterns;
        this.utils = new Utils(appResources, patterns);
    }

    public Node start() {
        utils.greeting();

        if (tree == null) {
            System.out.println();
            System.out.println(appResources.getString("animal.wantLearn"));
            System.out.println(appResources.getString("animal.askFavorite"));

            String animal = scanner.nextLine();
            animal = utils.checkFormat(animal.toLowerCase());

            tree = new Node(animal);

        }
        System.out.printf("\n%s\n\n", appResources.getString("welcome"));

        int menuId;
        boolean exit = false;

        do {
            menuId = mainMenu();
            switch (menuId) {
                case 1 -> tree = new PlayGame(tree, scanner, appResources, patterns).start();
                case 2 -> {
                    System.out.println(appResources.getString("tree.list.animals"));
                    getAllAnimals(tree, new ArrayList<>()).stream()
                            .sorted()
                            .forEach(e -> System.out.println(" - " + e));
                    System.out.println();
                }
                case 3 -> searchAnimal(tree);
                case 4 -> {
                    calculateStatistic(tree);
                    System.out.println();
                }
                case 5 -> {
                    System.out.println();
                    printKnowledgeTree(tree, 0, false, new ArrayList<>());
                    System.out.println();
                }
                case 0 -> exit = true;
                default -> System.out.println(MessageFormat.format(
                        appResources.getString("menu.property.error"),
                        "5")
                );
            }
        } while (!exit);

        utils.sayGoodbye();
        return tree;
    }

    private void printKnowledgeTree(Node node, int depth, boolean right, List<String> prefix) {
        if (node == null) {
            return;
        }

        if (depth > 0) {
            for (int i = 0; i < depth; i++) {
                System.out.print(prefix.get(i));
            }
        }

        if (right) {
            System.out.print("└ ");
            prefix.set(depth, " ");
        } else {
            if (depth == 0) {
                System.out.print(" └ ");
                prefix.add("  ");
            } else {
                System.out.print("├ ");
            }
        }

        System.out.println(node.isLeaf() ?
                utils.addArticle(node.getNodeName()) : node.getNodeName());

        depth++;
        prefix.add("|");

        printKnowledgeTree(node.getLeft(), depth, false, prefix);
        printKnowledgeTree(node.getRight(), depth, true, prefix);
    }

    private void calculateStatistic(Node tree) {
        System.out.println(appResources.getString("tree.stats.title") + "\n");
        System.out.println(MessageFormat.format(appResources.getString("tree.stats.root"), tree
                .getLeft()
                .getFact()
                .replaceAll("\\.", "")));
        System.out.println(MessageFormat.format(appResources.getString("tree.stats.nodes"),
                nodeUtils.amountOfNodes(tree)));
        System.out.println(MessageFormat.format(appResources.getString("tree.stats.animals"),
                nodeUtils.amountOfAnimals(tree)));
        System.out.println(MessageFormat.format(appResources.getString("tree.stats.statements"),
                nodeUtils.amountOfStatements(tree)));
        System.out.println(MessageFormat.format(appResources.getString("tree.stats.height"),
                nodeUtils.maxHeightOfTree(tree)));
        System.out.println(MessageFormat.format(appResources.getString("tree.stats.minimum"),
                nodeUtils.minHeightOfTree(tree)));
        System.out.println(MessageFormat.format(appResources.getString("tree.stats.average"),
                (double) nodeUtils.amountOfNodes(tree) / nodeUtils.amountOfAnimals(tree)));
    }

    private void searchAnimal(Node tree) {
        System.out.println(appResources.getString("animal.prompt"));
        scanner = new Scanner(System.in);
        String animal = scanner.nextLine();
        animal = utils.checkFormat(animal.toLowerCase());

        Node node = ifAnimalExist(tree, animal);

        if (node != null) {
            System.out.println(MessageFormat.format(appResources.getString("tree.search.facts"), animal));
            findFacts(tree, new String[1000], 0, node);
        } else {
            System.out.println(MessageFormat.format(appResources.getString("tree.search.noFacts"), animal));
        }
        System.out.println();
    }

    private void findFacts(Node node, String[] path, int pathLen, Node animal) {
        if (node == null) {
            return;
        }

        if (node.getFact() != null) {
            path[pathLen] = node.getFact();
            pathLen++;
        }

        if (node.isLeaf() && animal.equals(node)) {
            printPath(path, pathLen);
        } else {
            findFacts(node.getRight(), path, pathLen, animal);
            findFacts(node.getLeft(), path, pathLen, animal);
        }
    }

    private void printPath(String[] path, int length) {

        for (int i = 0; i < length; i++) {
            System.out.println(" - " + path[i]);
        }
    }

    private Node ifAnimalExist(Node node, String animal) {

        if (node == null) {
            return null;
        }

        if (node.getNodeName().equals(animal)) {
            return node;
        }

        Node n1 = ifAnimalExist(node.getLeft(), animal);

        if (n1 != null) {
            return n1;
        }

        return ifAnimalExist(node.getRight(), animal);
    }

    private List<String> getAllAnimals(Node node, List<String> list) {
        if (node == null) {
            return null;
        }

        if (node.isLeaf()) {
            list.add(node.getNodeName());
        }

        getAllAnimals(node.getLeft(), list);
        getAllAnimals(node.getRight(), list);
        return list;
    }

    private int mainMenu() {
        System.out.println(appResources.getString("menu.property.title") + "\n");
        System.out.println("1. " + appResources.getString("menu.entry.play"));
        System.out.println("2. " + appResources.getString("menu.entry.list"));
        System.out.println("3. " + appResources.getString("menu.entry.search"));
        System.out.println("4. " + appResources.getString("menu.entry.statistics"));
        System.out.println("5. " + appResources.getString("menu.entry.print"));
        System.out.println("0. " + appResources.getString("menu.property.exit"));
        return scanner.nextInt();
    }
}
