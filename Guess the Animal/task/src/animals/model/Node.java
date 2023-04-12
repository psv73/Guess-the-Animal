package animals.model;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node implements Serializable {

    private final String nodeName;
    private String fact;

    private Node left;

    private Node right;

    public Node(String nodeName) {
        this.nodeName = nodeName;
        this.fact = null;
        this.left = null;
        this.right = null;
    }

    @JsonCreator
    public Node(@JsonProperty("nodeName") String nodeName,
                @JsonProperty("fact") String fact,
                @JsonProperty("left") Node left,
                @JsonProperty("right") Node right) {
        this.nodeName = nodeName;
        this.fact = fact;
        this.left = left;
        this.right = right;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }
}
