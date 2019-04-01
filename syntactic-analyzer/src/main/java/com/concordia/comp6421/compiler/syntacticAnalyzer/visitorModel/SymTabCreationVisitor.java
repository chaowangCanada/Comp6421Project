package com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTabEntry;

import java.util.Arrays;
import java.util.stream.Collectors;


public class SymTabCreationVisitor extends Visitor{


    @Override
    public void visit(Node node)
    {
        if(node.nodeType == NodeType.prog)
            visitProgNode(node);
        else if(Arrays.asList(NodeType.listPatternNodeType).contains(node.nodeType)) {
            if (node.nodeType == NodeType.statBlock)
                visitListPatternNode(node, "program");
            else
                visitListPatternNode(node, node.nodeType.toString());
        }
        else if(Arrays.asList(NodeType.idListPattenNodeType).contains(node.nodeType))
            visitListPatternNode(node, node.nodeType.toString());
        else if(Arrays.asList(NodeType.typeIdListPattenNodeType).contains(node.nodeType))
            visitTypeIdListPattern(node);





//        switch (node.nodeType)
//        {
//            case id:
//                break;
//            case intNum:
//                break;
//            case floatNum:
//                break;
//            case prog:
//                visitProgNode(node);
//                break;
//            case relOp:
//                break;
//            case assignOp:
//                break;
//            case op:
//                break;
//            case fParam:
//                break;
//            case fParamList:
//                break;
//            case dimList:
//                break;
//            case type:
//                break;
//            case funcDecl:
//                break;
//            case funcDef:
//                break;
//            case indexList:
//                break;
//            case fCall:
//                break;
//            case not:
//                break;
//            case sign:
//                break;
//            case multOp:
//                break;
//            case var:
//                break;
//            case addOp:
//                break;
//            case dataMember:
//                break;
//            case aParams:
//                break;
//            case statBlock:
//                visitStatementBlockNode(node);
//                break;
//            case assignStat:
//                break;
//            case classList:
//                break;
//            case funcDefList:
//                break;
//            case classDecl:
//                break;
//            case inherList:
//                break;
//            case membList:
//                break;
//            case scopeSpec:
//                break;
//            case varDecl:
//                visitVarDeclNode(node);
//                break;
//        }
    }

    public void visitProgNode(Node node) {
        node.symTab = new SymTab("global", null);
        for (Node child : node.getChildren()) {
            if(child.nodeType == NodeType.classList) {
                for (Node clas : child.getChildren())
                    node.symTab.addEntry(clas.symTabEntry);
            }
            else if(child.nodeType == NodeType.funcDefList) {
                for (Node func : child.getChildren())
                    node.symTab.addEntry(func.symTabEntry);
            }
            else if(child.nodeType == NodeType.statBlock) {
                SymTab table = child.symTab;
                table.name = "program";
                node.symTab.addEntry(new SymTabEntry(table.name, "function" ,"",0 , table));
            }
        }
    }

    public void visitListPatternNode(Node node, String tableName) {
        node.symTab = new SymTab(tableName, node.symTab);

        for(Node stat : node.getChildren()) {
            if(stat.symTabEntry != null)
                node.symTab.addEntry(stat.symTabEntry);
//                stat.symTabEntry = new SymTabEntry(stat.leftMostChild.rightSib.toString(),
//                        stat.data.toString() ,stat.leftMostChild.nodeType.toString(),0 , null);
        }
    }

    public void visitTypeIdListPattern(Node node) {
        String kind = node.nodeType.toString();
        String type = node.getChildren().get(0).data.toString() + " : ";
        if(node.getChildren().size() > 2 )
            type += node.getChildren().get(2).getChildren().stream().map(n -> n.data.toString()).collect(Collectors.joining(" "));
        String name = node.getChildren().get(1).data.toString();
        node.symTabEntry = new SymTabEntry(name, kind, type, 0, null);

    }


}
