package com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Visitor {

    public static Map<String, SymTab> symTabMap = new HashMap<>();
    public static List<String> errors = new ArrayList<>();

    public abstract void visit(Node node);

    public abstract void visit(Node node, SymTab symTab);

}
