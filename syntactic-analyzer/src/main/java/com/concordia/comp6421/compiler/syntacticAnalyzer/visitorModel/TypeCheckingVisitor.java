package com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTabEntry;

import java.util.Arrays;
import java.util.Optional;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType.*;

public class TypeCheckingVisitor extends Visitor
{

    public void visit(Node node) {
        switch (node.nodeType) {
            case prog:
                visitProg(node, node.symTab);
                break;
            case classDecl:
                visitClassDecl(node, node.symTab);
                break;
            case funcDef:
                visitFuncDef(node, node.symTab);
                break;
            case funcDecl:
                break;
            default:
                visitGeneral(node, node.symTab);
                break;
        }
    }

    private void visitProg(Node node, SymTab symTab) {
        Node child = node.leftMostChild;
        while (child != null) {
            if (child.nodeType == NodeType.statBlock) {
                visitGeneral(child, null);
            } else {
                visit(child);
            }
            child = child.rightSib;
        }
    }

    private void visitFuncDef(Node node, SymTab symTab) {
        Node child = node.leftMostChild;
        while (child.rightSib != null) {
            child = child.rightSib;
        }
        visitGeneral(child, symTab);
    }

    private void visitClassDecl(Node node, SymTab symTab) {
        Node child = node.leftMostChild;
        while (child.rightSib != null) {
            child = child.rightSib;
        }
        visitGeneral(child, symTab);
    }

    private void visitGeneral(Node node, SymTab symTab) {
        if (node.nodeType == NodeType.id) {
//            checkIdDef(node, symTab, "id");
        }

        if (node.nodeType == NodeType.type
                && !node.data.toString().equals("integer")
                && !node.data.toString().equals("float")) {
//            checkIdDef(node, symTab, "type");
        }

        if (node.nodeType == NodeType.fCall) {
//            checkFCallClass(node, symTab);
        }

        if (node.nodeType == NodeType.returnStat) {
            checkReturnType(node, symTab);
        }

        Node child = node.leftMostChild;
        while (child != null) {
            visit(child);
            child = child.rightSib;
        }
    }

    private void checkReturnType(Node node, SymTab local) {
        local = local == null ? Visitor.symTabMap.get("program") : local;
        String returnType = getAParamType(node);
        if ((returnType.equals("intNum") && !local.symList.get(0).type.equals("integer") || (returnType.equals("floatNum") && local.symList.get(0).type.equals("float")))){
            Visitor.errors.add("Invalid return type for function " + local.name);
            return;
        }

        Optional<String> validType = local.symList.stream().filter(e -> e.name.equals(returnType)).map(e -> e.type).findFirst();
        if (validType.isPresent() && !local.symList.get(0).type.equals(validType.get())) {
            Visitor.errors.add("Invalid return type for function " + local.name);
        }
    }

//    private void checkFCallClass(Node node, SymTab local) {
//        local = local == null ? Visitor.symTabMap.get("program") : local;
//        Node callClass = node.getLeftMostSib();
//        String fName = node.leftMostChild.data.toString();
//        SymTabEntry symTabEntry = null;
//        SymTab classTab = null;
//        if (callClass.nodeType == NodeType.dataMember) {
//            Optional<String> className = local.symList.stream().filter(e -> e.name.equals(callClass.leftMostChild.data.toString())).map(e -> e.type).findFirst();
//            if (className.isPresent()) {
//                classTab = Visitor.symTabMap.get(className.get());
//            }
//        } else {
//            classTab = Visitor.symTabMap.get("Global");
//        }
//
//        if (classTab != null) {
//            symTabEntry = classTab.symList.stream().filter(e -> e.name.equals(fName)).findFirst().orElse(null);
//        }
//
//        if (symTabEntry != null) {
//            String inputType = getAParamType(node.leftMostChild.rightSib);
//            Optional<String> requied = local.symList.stream().filter(e -> e.kind == SymTab.Kind.parameter).map(e -> e.type).findFirst();
//            if ((inputType == null && requied.isPresent()) || (inputType != null && !requied.isPresent())) {
//                Visitor.errors.add("Invalid input type for function " + fName);
//            }
//
//            if (inputType == null) {
//                return;
//            }
//
//            if (inputType.equals("intNum") && local.symList.stream().noneMatch(e -> e.type.equals("integer")) ||
//                    inputType.equals("intNum") && local.symList.stream().noneMatch(e -> e.type.equals("float"))) {
//                Visitor.errors.add("Invalid input type for function " + fName);
//                return;
//            }
//
//            Optional<String> validType = local.symList.stream().filter(e -> e.name.equals(inputType)).map(e -> e.type).findFirst();
//            if (validType.isPresent() && local.symList.stream().noneMatch(e -> e.type.equals(validType.get()))) {
//                Visitor.errors.add("Invalid input type for function " + fName);
//            }
//        } else {
//            Visitor.errors.add("Undefined member " + fName);
//        }
//    }

//    private void checkIdDef(Node node, SymTab symTab, String type) {
//        if (symTab != null) {
//            if (symTab.symList.stream().noneMatch(e -> e.name.equals(node.data.toString()))) {
//                Visitor.errors.add(String.format("Undefined %s %s in %s %s", type, node.data.toString(), symTab.kind, symTab.name));
//            }
//        } else {
//            if (Visitor.symTabMap.values().stream()
//                    .flatMap(s -> s.symList.stream()).noneMatch(e -> e.name.equals(node.data.toString()))) {
//                Visitor.errors.add(String.format("Undefined %s %s in %s", type, node.data.toString(), "program"));
//            }
//        }
//    }

    private String getAParamType(Node node) {
        if (node == null) {
            return null;
        } else if (node.nodeType == NodeType.id) {
            return node.data.toString();
        } else if (node.nodeType == NodeType.intNum || node.nodeType == NodeType.floatNum) {
            return node.nodeType.toString();
        } else {
            return getAParamType(node.leftMostChild);
        }
    }

    private String getId(Node node) {
        String id = "";
        Node child = node.leftMostChild;
        while (child.nodeType == NodeType.id) {
            if (child.nodeType == NodeType.id) {
                id = child.data.toString();
                break;
            }
            child = child.rightSib;
        }
        return id;
    }

}
