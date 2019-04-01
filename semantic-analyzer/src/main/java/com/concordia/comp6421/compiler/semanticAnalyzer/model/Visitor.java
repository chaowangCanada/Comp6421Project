package com.concordia.comp6421.compiler.semanticAnalyzer.model;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;

public abstract class Visitor {

    public abstract void visit(Node node);
}
