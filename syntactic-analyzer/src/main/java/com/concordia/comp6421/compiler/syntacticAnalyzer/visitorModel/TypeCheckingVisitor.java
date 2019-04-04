package com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;

import java.util.Arrays;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType.typeerror;

public class TypeCheckingVisitor extends Visitor
{
    @Override
    public void visit(Node node)
    {
        if(Arrays.asList(NodeType.binaryOpType).contains(node.nodeType))
            visitOp(node);

    }

    private void visitOp(Node node) {
        NodeType leftOperandType = node.getChildren().get(0).nodeType;
        NodeType rightOperandType = node.getChildren().get(1).nodeType;
        if(leftOperandType == rightOperandType)
            node.nodeType = leftOperandType;
        else {
            node.nodeType  = NodeType.typeerror;
            System.out.println("Type error detected between " + node.getChildren().get(0).data.toString() +
                                " and " + node.getChildren().get(1).data.toString());
        }
    }


}
