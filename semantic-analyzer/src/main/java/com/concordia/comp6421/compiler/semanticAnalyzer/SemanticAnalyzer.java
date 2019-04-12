package com.concordia.comp6421.compiler.semanticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.ComputeMemSizeVisitor;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.SymTabCreationVisitor;
import visitor.TypeCheckingVisitor;
import lombok.Getter;
import lombok.Setter;

public class SemanticAnalyzer {

    public Node tree;

    public SymTabCreationVisitor symTabCreationVisitor;

    public TypeCheckingVisitor typeCheckingVisitor;

    public ComputeMemSizeVisitor computeMemSizeVisitor;

    public SemanticAnalyzer() {
        symTabCreationVisitor = new SymTabCreationVisitor();
        typeCheckingVisitor = new TypeCheckingVisitor();
        computeMemSizeVisitor = new ComputeMemSizeVisitor();
    }

    public void validate(Node incomeTree, boolean hasCheck) {
        tree = incomeTree;
        tree.accept(symTabCreationVisitor);
        tree.accept(computeMemSizeVisitor);
        if(hasCheck)
            tree.accept(typeCheckingVisitor);
    }

}
