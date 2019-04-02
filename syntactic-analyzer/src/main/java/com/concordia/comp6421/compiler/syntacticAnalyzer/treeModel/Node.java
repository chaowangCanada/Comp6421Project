package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.Visitor;

import java.util.*;

public class Node {


    public NodeType nodeType;
    public Object data = null;
    public Node leftMostChild;
    public Node rightSib;
    public Node leftMostSib;
    public Node parent;

    //attribute for SymbolTable
    public SymTab symTab;
    public SymTabEntry symTabEntry;
    public boolean isAtomic = false;

    public Node(NodeType type, Object data) {
        this.nodeType = type;
        this.data = data;
    }

    public Node(NodeType type, Object data, boolean isAtomic) {
        this.nodeType = type;
        this.data = data;
        this.isAtomic = isAtomic;
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

    public static Node makeNode(NodeType type, String value, boolean isAtomic){
        if(type == NodeType.intNum){
            return new Node(type, Integer.valueOf(value), isAtomic);
        }
        else if(type == NodeType.floatNum) {
            return new Node(type, Float.valueOf(value), isAtomic);
        }
        else {
            return new Node(type,value, isAtomic);
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

    public List<Node> getChildren(){
        List<Node> children = new ArrayList<>();
        if(this.leftMostChild == null )
            return null;
        else {
            children.add(this.leftMostChild);
            Node tmp = this.leftMostChild;
            while(tmp.rightSib != null) {
                tmp = tmp.rightSib;
                children.add(tmp);
            }
        }
        return children;
    }

    public void accept(Visitor visitor) {
        if(this.isAtomic)
            visitor.visit(this);
        else {
            for (Node child : this.getChildren()){
                child.accept(visitor);
            }
            visitor.visit(this);
        }
    }

    public void printTable() {
        if(this.symTab == null || this.symTab.symList.size() == 0)
            return ;

        System.out.println("=====================================================================");
        System.out.println("Symbol table :  " + this.symTab.name);
        System.out.println(" | name     |     kind     |   type   | offset |   link    |");
        for (SymTabEntry row : this.symTab.symList) {
            System.out.println(" | " + row.name + " | " + row.kind + " | " + row.type + " | " + row.offset + " | " + (row.thisTable == null ? "null" : row.thisTable.size));
        }
        System.out.println("=====================================================================");
    }


}
