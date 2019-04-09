package com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTabEntry;

import java.util.Arrays;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.CLASS_DEFINITION_MAP;
import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.FUNCTION_DEFINITION_MAP;

public class ComputeMemSizeVisitor extends Visitor{
    @Override
    public void visit(Node node)
    {
        if(node.nodeType == NodeType.prog)
            visitProgNode(node);
        else if(node.nodeType == NodeType.classDecl) {
            visitClassDeclNode(node);
        }
        else if(Arrays.asList(NodeType.StatTypes).contains(node.nodeType)){
            for (SymTabEntry entry : node.symTab.symList){
                entry.offset = node.symTab.size ;
                node.symTab.size -= entry.size;
            }
        }
        else if(node.nodeType == NodeType.funcDef) {
            visitFuncDefNode(node);
        }
        else if(node.nodeType == NodeType.varDecl){
            visitVarDeclNode(node);
        }
        else if(node.nodeType == NodeType.fParam){
            visitVarDeclNode(node);
        }
        else if(node.nodeType == NodeType.addOp ||
                node.nodeType == NodeType.multOp ||
                node.nodeType == NodeType.relExpr){
            visitAddOpNode(node);
        }
        else if(node.nodeType == NodeType.statBlock){
            visitStatBlockNode(node);
        }
        else if(node.nodeType == NodeType.funcDecl){
            visitFuncDeclNode(node);
        }
//        else if(node.nodeType == NodeType.type) {
//            node.symTabEntry.size = sizeOfTypeNod(node);
//        }
        else if(node.nodeType == NodeType.intNum ){
            node.symTabEntry.size = 4;
        }
        else if(node.nodeType == NodeType.floatNum){
            node.symTabEntry.size = 8;
        }
//        else if(node.nodeType == NodeType.assignStat ||
//                node.nodeType == NodeType.classList ||
//                node.nodeType == NodeType.dimList ||
//                node.nodeType == NodeType.funcDefList ||
//                node.nodeType == NodeType.putStat ||
//                node.nodeType == NodeType.statBlock ||
//                node.nodeType == NodeType.fParamList ||
//                node.nodeType == NodeType.returnStat ){
//
//            System.out.println(node.nodeType.toString());
//            for(Node child : node.getChildren())
//                child._accept(this);
//        }
    }

    private void visitFuncDeclNode(Node p_node) {
        if (FUNCTION_DEFINITION_MAP.containsKey(p_node.symTabEntry.name) &&
                FUNCTION_DEFINITION_MAP.get(p_node.symTabEntry.name).symTab.size != 0)
            p_node.symTabEntry.size = Math.abs(FUNCTION_DEFINITION_MAP.get(p_node.symTabEntry.name).symTab.size);
        else
            p_node.symTabEntry.size = 4;

    }

    private void visitMultOpNode(Node node) {
        node.symTabEntry.size = this.updateEntrySize(node);
    }

    private void visitAddOpNode(Node node) {
        node.symTabEntry.size = this.updateEntrySize(node);
    }


    private void visitFuncDefNode(Node node) {
        // stack frame contains the return value at the bottom of the stack
        node.symTab.size -= sizeOfTypeNod(node.getChildren().get(0));
        //then is the return addess is stored on the stack frame
//        node.symTab.size -= 4;
        for(SymTabEntry entry : node.symTab.symList){
            entry.offset = node.symTab.size ;
            node.symTab.size -= entry.size;
        }
    }

    private void visitClassDeclNode(Node node) {
        for(SymTabEntry entry : node.symTab.symList){
            entry.offset = node.symTab.size ;
            node.symTab.size -= entry.size;
        }
    }

    private void visitProgNode(Node node) {
        for (SymTabEntry entry : node.symTab.symList){
            entry.offset = node.symTab.size ;
            entry.size = entry.thisTable.size;
            node.symTab.size += entry.size;
        }
    }

    private void visitStatBlockNode(Node node) {
        for (SymTabEntry entry : node.symTab.symList){
            entry.offset = node.symTab.size ;
            node.symTab.size -= entry.size;
        }
    }

    private void visitVarDeclNode(Node node) {
        node.symTabEntry.size = updateEntrySize(node);
    }

    public int updateEntrySize(Node p_node) {
        if(p_node.symTabEntry.type.contains("integer"))
            p_node.symTabEntry.size = 4;
        else if(p_node.symTabEntry.type.contains("float"))
            p_node.symTabEntry.size = 8;
        else if(CLASS_DEFINITION_MAP.containsKey(p_node.symTabEntry.type.split(" ")[0]))
            p_node.symTabEntry.size = Math.abs(CLASS_DEFINITION_MAP.get(p_node.symTabEntry.type.split(" ")[0]).symTab.size);
        else if (FUNCTION_DEFINITION_MAP.containsKey(p_node.symTabEntry.name))
            p_node.symTabEntry.size = Math.abs(FUNCTION_DEFINITION_MAP.get(p_node.symTabEntry.name).symTab.size);

        if(!p_node.symTabEntry.dims.isEmpty()&& p_node.symTabEntry.size !=0){
            int dimSize = 0;
            dimSize = p_node.symTabEntry.size;
            for(Integer dim : p_node.symTabEntry.dims)
                dimSize *= dim;
            p_node.symTabEntry.size = dimSize;
        }
        return p_node.symTabEntry.size;
    }

    public int sizeOfTypeNod(Node p_node) {
        int size = 0;
        if(p_node.nodeType == NodeType.type && p_node.data.toString().equalsIgnoreCase("Integer"))
            size = 4;
        else if(p_node.nodeType == NodeType.type && p_node.data.toString().equalsIgnoreCase("Float"))
            size = 8;
        else if(p_node.nodeType == NodeType.type && p_node.data.toString().equalsIgnoreCase("id") )
            System.out.println("Need to be done !!!!!!!!!!");

        return size;
    }

}
