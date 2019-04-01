package com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;

public abstract class Visitor {

    public abstract void visit(Node node);
}
