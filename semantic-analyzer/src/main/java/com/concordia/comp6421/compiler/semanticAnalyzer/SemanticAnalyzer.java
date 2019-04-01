package com.concordia.comp6421.compiler.semanticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Util;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.SymTabCreationVisitor;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.TypeCheckingVisitor;
import lombok.Getter;
import lombok.Setter;

public class SemanticAnalyzer {

    @Setter
    @Getter
    private Node tree;

    public SymTabCreationVisitor symTabCreationVisitor;

    public TypeCheckingVisitor typeCheckingVisitor;

    public SemanticAnalyzer() {
        symTabCreationVisitor = new SymTabCreationVisitor();
        typeCheckingVisitor = new TypeCheckingVisitor();
    }

    public void validate(Node incomeTree) {
        tree = incomeTree;
        tree.accept(symTabCreationVisitor);
        tree.accept(typeCheckingVisitor);
    }



}
