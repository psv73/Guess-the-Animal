package animals.utils;

import animals.model.Node;

public class NodeUtils {
    public int amountOfNodes(Node node) {

        if (node == null) {
            return 0;
        }

        return 1 + amountOfNodes(node.getLeft()) + amountOfNodes(node.getRight());
    }

    public int amountOfAnimals(Node node) {
        if (node.isLeaf()) {
            return 1;
        }

        return amountOfAnimals(node.getLeft()) + amountOfAnimals(node.getRight());
    }

    public int amountOfStatements(Node node) {
        if (node.isLeaf()) {
            return 0;
        }

        return 1 + amountOfStatements(node.getLeft()) + amountOfStatements(node.getRight());
    }

    public int maxHeightOfTree(Node node) {
        if (node == null) {
            return -1;
        }

        return 1 + Math.max(maxHeightOfTree(node.getLeft()), maxHeightOfTree(node.getRight()));
    }

    public int minHeightOfTree(Node node) {
        if (node == null) {
            return -1;
        }

        return 1 + Math.min(maxHeightOfTree(node.getLeft()), maxHeightOfTree(node.getRight()));
    }
}
