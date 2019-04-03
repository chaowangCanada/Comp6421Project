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
        else if(Arrays.asList(NodeType.ListPatternNodeType).contains(node.nodeType)) {
            if (node.nodeType == NodeType.statBlock)
                visitListPatternNode(node, "program");
            else
                visitListPatternNode(node, node.nodeType.toString());
        }
        else if(Arrays.asList(NodeType.TypeIdListPattenNodeType).contains(node.nodeType))
            visitTypeIdListPattern(node);
        else if(Arrays.asList(NodeType.IdListPattenNodeType).contains(node.nodeType))
            visitIdListPattern(node);
        else if(Arrays.asList(NodeType.TypeListIdListPatternNodeType).contains(node.nodeType))
            visitTypeListIdListPattern(node);
    }

    private void visitTypeListIdListPattern(Node node) {
        String funcName = node.getChildren().get(0).data.toString();
        node.symTab = new SymTab(funcName);

        node.symTab = new SymTab("");
        for (Node child : node.getChildren()) {
            if(child.nodeType == NodeType.type) {
                funcName += child.data.toString();
            }
            else if(child.nodeType == NodeType.id) {
                funcName += " : " + child.data.toString();
            }
            else if(child.nodeType == NodeType.scopeSpec) {
                funcName += " : " + child.data.toString();
            }
            else if(child.nodeType == NodeType.fParamList) {
                for (Node fparam : child.getChildren())
                    node.symTab.addEntry(fparam.symTabEntry);
            }
            else if(child.nodeType == NodeType.statBlock) {
                SymTab table = child.symTab;
                table.name = "statement";
                node.symTab.addEntry(new SymTabEntry(table.name, "function" ,"",0 , table));
            }
        }
        node.symTabEntry = new SymTabEntry(funcName, "function" ,"",0 , node.symTab);
    }

    private void visitIdListPattern(Node node) {
        String className = "";

        for(Node child : node.getChildren()) {
            if(child.nodeType == NodeType.id) {
                className = child.data.toString();
                node.symTab = new SymTab(className);
            }
            else if(child.symTabEntry != null)
                node.symTab.addEntry(child.symTabEntry);
            else if(child.symTab != null ) {
                child.symTab.symList.forEach(node.symTab::addEntry);
            }
        }
        node.symTabEntry = new SymTabEntry(className, "class" ,"",0 , node.symTab);
    }

    public void visitProgNode(Node node) {
        node.symTab = new SymTab("global");
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
        node.symTab = new SymTab(tableName);

        for(Node stat : node.getChildren()) {
            if(stat.symTabEntry != null)
                node.symTab.addEntry(stat.symTabEntry);
        }
    }

    // Like varDecl
    public void visitTypeIdListPattern(Node node) {
        String kind = node.nodeType.toString();
        StringBuilder type = new StringBuilder(node.getChildren().get(0).data.toString() + " : ");
        if(node.getChildren().size() > 2 ) {
            for(Node listChild : node.getChildren().get(2).getChildren()) {
                if(listChild.nodeType == NodeType.fParam)
                    type.append(" ").append(getfParamString(listChild));
                else
                    type.append(" ").append(listChild.data.toString());
            }
        }
        String name = node.getChildren().get(1).data.toString();
        node.symTabEntry = new SymTabEntry(name, kind, type.toString(), 0, null);
    }

    public String getfParamString(Node node) {
        String type = node.getChildren().get(0).data.toString() + " : ";
        if(node.getChildren().size() > 2 )
            type += node.getChildren().get(2).getChildren().stream().map(n -> n.data.toString()).collect(Collectors.joining(", "));
        return type;
    }


}
