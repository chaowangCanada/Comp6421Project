package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;

import java.util.Set;

public class Action extends Symbol{
    NodeType nodeType;

    public Action(String symbol) {
        super(symbol);
        this.nodeType = NodeType.valueOf(symbol);
    }

    @Override
    public Set<Symbol> getFirst() {
        return null;
    }
}
