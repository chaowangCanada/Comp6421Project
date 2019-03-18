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
        Node rightmostSib = this;
        while(this.rightSib != null) {
            rightmostSib = rightmostSib.rightSib;
        }

        if(y.leftMostSib != null) {
            rightmostSib.rightSib=y.leftMostSib;
            y.leftMostSib.leftMostSib=this.leftMostSib;
            y.leftMostSib.parent = this.parent;
            y.parent = this.parent;
        }
        else {
            rightmostSib.rightSib = y;
            y.leftMostSib=this.leftMostSib;
            y.parent = this.parent;
        }

        Node nodeTmp = y.rightSib;
        while (nodeTmp != null){
            nodeTmp.leftMostSib = this.leftMostSib;
            nodeTmp.parent = this.parent;
            nodeTmp = nodeTmp.rightSib;
        }
        return this;
    }

    public Node adoptChildren(Node y) {
        if(this.leftMostChild != null ){
            this.leftMostChild.makeSiblings(y);
        }
        else {
            this.leftMostChild = y.leftMostChild !=null ? y.leftMostChild : y;
            Node nodeTmp = y.leftMostChild !=null ? y.leftMostChild : y;
            while(nodeTmp != null) {
                nodeTmp.parent = this ;
                nodeTmp = nodeTmp.rightSib;
            }
        }
        return this;
    }

    public Node adoptChildren(Collection<Node> children) {
        children.forEach(this::adoptChildren);
        return this;
    }

}
