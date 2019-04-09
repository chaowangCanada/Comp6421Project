package com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTabEntry;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.CLASS_DEFINITION_MAP;
import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.FUNCTION_DEFINITION_MAP;
import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.VARIABLE_DEFINITION_MAP;


public class SymTabCreationVisitor extends Visitor{


    @Override
    public void visit(Node node)
    {
        if(node.nodeType == NodeType.prog)
            visitProgNode(node);
        else if (Arrays.asList(NodeType.StatTypes).contains(node.nodeType)) {
            visitAllStatNode(node);
        }
        else if(node.nodeType == NodeType.intNum || node.nodeType == NodeType.floatNum) {
            visitIntFloatNode(node);
        }
        else if(node.nodeType == NodeType.addOp || node.nodeType == NodeType.multOp ||
                node.nodeType == NodeType.relExpr) {
            visitOpStatNode(node);
        }
        else if(Arrays.asList(NodeType.ListPatternNodeType).contains(node.nodeType)) {
            if (node.nodeType == NodeType.statBlock && node.parent != null &&node.parent.nodeType == NodeType.prog)
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

    private void visitIntFloatNode(Node node) {
        if(node.nodeType == NodeType.intNum)
            node.symTabEntry = new SymTabEntry("t"+ Default.tmpVarCount++ ,"litvar", "integer", null);
        else if(node.nodeType == NodeType.floatNum)
            node.symTabEntry = new SymTabEntry("t"+ Default.tmpVarCount++ ,"litvar", "float", null);
    }

    private void visitAllStatNode(Node node) {
        node.symTab = new SymTab(" stat ");

//        if(node.nodeType == NodeType.type.forStat){
//            createAddVarEntry(node.leftMostChild.rightSib);
//        }

        for(Node child : node.getChildren()) {
            if(child.symTab != null) {
                node.symTab.symList.addAll(child.symTab.symList);
            }
            if(child.symTabEntry != null){
                node.symTab.addEntry(child.symTabEntry);
            }
        }
    }

    private void visitOpStatNode(Node node) {
        node.symTab = new SymTab(" Op ");

        for(Node child : node.getChildren()) {
            if(child.symTabEntry != null)
                node.symTab.addEntry(child.symTabEntry);
            if(child.symTab != null) {
                node.symTab.symList.addAll(child.symTab.symList);
            }
        }
        String type = "";
        if(node.getChildren().get(0).nodeType == NodeType.var){
            if(! VARIABLE_DEFINITION_MAP.containsKey(node.getChildren().get(0).getChildren().get(0).getChildren().get(0).data.toString()))
                type  = createAddVarEntry(node);
            else
                type = VARIABLE_DEFINITION_MAP.get(node.getChildren().get(0).getChildren().get(0).getChildren().get(0).data.toString())
                    .getChildren().get(0).data.toString();
        }
        else
            type = node.getChildren().get(0).nodeType == NodeType.intNum ? "integer" : "float";

        node.symTabEntry = new SymTabEntry("t"+Default.tmpVarCount++,"tmpvar",
                type, node.symTab);
    }

    private String createAddVarEntry(Node node) {
        String type = node.getChildren().get(1).nodeType == NodeType.intNum ? "integer" : "float";
        String kind = "var";
        String name = node.getChildren().get(0).getChildren().get(0).getChildren().get(0).data.toString();
        node.leftMostChild.symTabEntry = new SymTabEntry(name, kind, type, null);
        node.leftMostChild.symTabEntry.size = type.equalsIgnoreCase("integer") ? 4: 8;
        node.symTab.addEntry(node.leftMostChild.symTabEntry);
        VARIABLE_DEFINITION_MAP.put(name, node.parent);
        return type;
    }

    private void visitTypeListIdListPattern(Node node) {
        StringBuilder name = new StringBuilder();
        StringBuilder type = new StringBuilder();
        node.symTab = new SymTab("");
        node.symTabEntry = new SymTabEntry(name.toString(), "function" ,type.toString(), node.symTab);
        String functionName = "";
        for (Node child : node.getChildren()) {
            if(child.nodeType == NodeType.type) {
                type.append(child.data.toString() + " : ");
            }
            else if(child.nodeType == NodeType.scopeSpec){
                name.append(child.data.toString() + " : ");
            }
            else if(child.nodeType == NodeType.id) {
                name.append(child.data.toString());
                functionName = child.data.toString();
            }
            else if(child.nodeType == NodeType.scopeSpec) {
                type.append(child.data.toString() );
            }
            else if(child.nodeType == NodeType.fParamList) {
                node.symTab.symList.addAll(child.symTab.symList);
                for (Node fparam : child.getChildren()) {
                    type.append(getfParamString(fparam));
                }
            }
            else if(child.nodeType == NodeType.statBlock) {
                node.symTab.symList.addAll(child.symTab.symList);
            }
        }
        node.symTabEntry.name = name.toString();
        node.symTabEntry.type = type.toString();
        node.symTab.name = name.toString();
        if(node.nodeType == NodeType.funcDef)
            FUNCTION_DEFINITION_MAP.put(functionName, node);
    }

    // add statblock vardecl to funcDef
    public void addListPatternNode(Node node) {
        for(Node stat : node.getChildren()) {
            if(stat.symTabEntry != null)
                node.symTab.addEntry(stat.symTabEntry);
        }
    }

    private void visitIdListPattern(Node node) {
        String className = "";

        for(Node child : node.getChildren()) {
            if(child.nodeType == NodeType.id) {
                className = child.data.toString();
                node.symTab = new SymTab(className);
            }
            else if(child.symTab != null ) {
                child.symTab.symList.forEach(node.symTab::addEntry);
            }
            else if(child.symTabEntry != null)
                node.symTab.addEntry(child.symTabEntry);
        }
        node.symTabEntry = new SymTabEntry(className, "class" ,"", node.symTab);

        if(node.nodeType == NodeType.classDecl)
            CLASS_DEFINITION_MAP.put(node.symTab.name, node);
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
                node.symTab.addEntry(new SymTabEntry(table.name, "function" ,"" , table));
            }
        }
    }
    //statblock
    public void visitListPatternNode(Node node, String tableName) {
        node.symTab = new SymTab(tableName);

        for(Node stat : node.getChildren()) {
            if(stat.symTab != null)
                node.symTab.symList.addAll(stat.symTab.symList);
            if(stat.symTabEntry != null)
                node.symTab.addEntry(stat.symTabEntry);
        }
    }

    // Like varDecl
    public void visitTypeIdListPattern(Node node) {
        String kind = node.nodeType.toString();
        String name = node.getChildren().get(1).data.toString();
        StringBuilder type = new StringBuilder(node.getChildren().get(0).data.toString() + " :: ");
        node.symTabEntry = new SymTabEntry(name, kind, type.toString(), null);
        if(node.getChildren().size() > 2 ) {
            for(Node listChild : node.getChildren().get(2).getChildren()) {
                if(listChild.nodeType == NodeType.fParam){
                    type.append(" ").append(getfParamString(listChild));
                }
                else{
                    type.append(" ").append(listChild.data.toString());
                    node.symTabEntry.dims.add(Integer.valueOf(listChild.data.toString()));
                }
            }
        }
        node.symTabEntry.type = type.toString();
        if(node.nodeType == NodeType.varDecl)
            VARIABLE_DEFINITION_MAP.put(name, node);
    }

    public String getfParamString(Node fParamNode) {
        String type = fParamNode.getChildren().get(0).data.toString() + " : ";
        if(fParamNode.getChildren().size() > 2 ){
            type += fParamNode.getChildren().get(2).getChildren().stream().map(n -> n.data.toString()).collect(Collectors.joining(", "));
            fParamNode.parent.parent.symTabEntry.dims.addAll(
                    fParamNode.getChildren().get(2).getChildren().stream().map(n -> Integer.valueOf(n.data.toString())).collect(Collectors.toList()));
        }
        return type;
    }


}
