package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.Visitor;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Stream;

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

    public static Node makeFamily(NodeType op, String value, Node kid1, Node... kids) {
        Stream.of(kids).forEach(kid1::makeSiblings);
        return Node.makeNode(op, value).adoptChildren(kid1);
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
            if(this.getChildren() != null){
                for (Node child : this.getChildren()){
                    child.accept(visitor);
                }
            }
            visitor.visit(this);
        }
    }

    public void _accept(Visitor visitor){
        visitor.visit(this);
    }

    public String printTable() {
        if(this.symTab == null || this.symTab.symList.size() == 0)
            return "";
        String frame = String.join("", Collections.nCopies(126, "#")) + "\n";
        String line = "#" + String.join("", Collections.nCopies(125, "-")) + "#\n";
        String headFormat = "#Symbol table: %-50s|%-60s#\n";
        String rowFormat = "#%-20s|%-20s|%-20s|%-20s|%-20s|%-20s#\n";
        StringBuilder sb = new StringBuilder();
        sb.append(frame);
        sb.append(String.format(headFormat, symTab.name, "size: " + Math.abs(symTab.size)));
        sb.append(line);
        sb.append(String.format(rowFormat, "name", "kind", "type", "size", "offset" ,"link"));
        sb.append(line);
        for (SymTabEntry row : this.symTab.symList) {
            sb.append(String.format(rowFormat, row.name, row.kind, row.type, Math.abs(row.size), Math.abs(row.offset), (row.thisTable == null ? "null" : row.thisTable.name)));
            sb.append(line);
        }
        sb.append(frame);
        sb.append("\n");
        System.out.println(sb);
        return sb.toString();
    }

    public Node getLeftMostSib() {
        if (parent != null) {
            return parent.leftMostChild;
        } else {
            return null;
        }
    }
}
