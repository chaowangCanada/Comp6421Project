package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Node {

    @Getter
    @Setter
    private NodeType nodeType;
    @Getter
    @Setter
    private Object data = null;

    public Node leftMostChild;
    public Node rightSib;
    public Node leftMostSib;
    public Node parent;

    public Node(NodeType type, Object data) {
        this.nodeType = type;
        this.data = data;
    }

    public Node() {
    }

    public static Node makeNode(NodeType type, String value){
        if(type == NodeType.intNum){
            return new Node(type, Integer.valueOf(value));
        }
        else if(type == NodeType.floatNum) {
            return new Node(type, Float.valueOf(value));
        }
        else {
            return new Node(type,value);
        }
    }

    public static Node makeFamiliy(NodeType type, String value, Node left, Node right){
        left.makeSiblings(right);
        Node parent = Node.makeNode(type,value);
        parent.adoptChildren(left);
        return parent;
    }

    public Node makeSiblings(Node y) {
        Node xsibs = this;
        while(xsibs.rightSib != null) {
            xsibs = xsibs.rightSib;
        }

        Node ysibs = y.leftMostSib == null ? y : y.leftMostSib;
        xsibs.rightSib = ysibs;

        ysibs.leftMostSib = xsibs.leftMostSib;
        ysibs.parent = xsibs.parent;

        while (ysibs.rightSib != null ) {
            ysibs = ysibs.rightSib;
            ysibs.leftMostSib = xsibs.leftMostSib;
            ysibs.parent = xsibs.parent;
        }
        return this;
    }

    public Node adoptChildren(Node y) {
        if(this.leftMostChild != null ){
            this.leftMostChild.makeSiblings(y);
        }
        else {
            Node ysibs = y.leftMostSib !=null ? y.leftMostChild : y;
            this.leftMostChild = ysibs;
            while(ysibs != null) {
                ysibs.parent = this ;
                ysibs = ysibs.rightSib;
            }
        }
        return this;
    }

    public Node adoptChildren(Collection<Node> children) {
        children.forEach(this::adoptChildren);
        return this;
    }

}
