package com.concordia.comp6421.compiler.semanticAnalyzer.model;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;

public class SymTabCreationVisitor extends Visitor{

    @Override
    public void visit(Node node) {
        System.out.println("Visit Node: " + node.getNodeType().toString());

        
    }
}
