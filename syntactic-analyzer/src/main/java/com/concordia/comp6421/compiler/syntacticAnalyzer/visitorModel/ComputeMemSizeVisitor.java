package com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.MOON_DATA_CODE;

public class ComputeMemSizeVisitor extends Visitor{
    @Override
    public void visit(Node node) {
        if(node.nodeType == NodeType.varDecl)
            visitVarDeclNode(node);
    }

    private void visitVarDeclNode(Node node) {
        if (node.getChildren().get(0).nodeType == NodeType.type) {
            if(node.getChildren().get(0).data.toString().equalsIgnoreCase("integer")){
                MOON_DATA_CODE.append("     % space for varible").append(node.getChildren().get(1).data.toString()).append(System.lineSeparator());
                MOON_DATA_CODE.append(node.getChildren().get(1).data.toString()).append( "     res 4").append(System.lineSeparator());
                
            }

        }
    }
}
