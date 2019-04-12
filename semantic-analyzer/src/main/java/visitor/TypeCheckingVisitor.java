package visitor;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTabEntry;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.Visitor;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType.*;

public class TypeCheckingVisitor extends Visitor
{

    public void visit(Node node) {
        switch (node.nodeType) {
            case prog:
                visitProg(node, node.symTab);
            case classDecl:
                visitClassDecl(node, node.symTab);
                break;
            case funcDef:
                visitFuncDef(node, node.symTab);
                break;
            case funcDecl:
                break;
            case addOp:
            case multOp:
                visitOp(node, node.symTab);
                break;
            case assignStat:
                visitAssignStat(node, node.symTab);
                break;
            case fCall:
                visitFuncCall(node, node.symTab);
                break;
            default:
                visitGeneral(node, node.symTab);
                break;
        }
    }

    private void visitProg(Node node, SymTab symTab) {
        for(SymTabEntry entry : symTab.symList){
            if(entry.thisTable != null){
                Set<SymTabEntry> entrySet = new HashSet<>(entry.thisTable.symList);
                if(entrySet.size() < entry.thisTable.symList.size())
                    errors.add("Duplicate definition in :  "
                            + entry.name);
            }
        }
    }

    private void visitFuncCall(Node node, SymTab symTab) {
        if(this.symTabMap.get(node.leftMostChild.data.toString()) == null){
            errors.add("Undefined fcall:  "
                    + node.getChildren().get(0).data.toString());
            return ;
        }

        symTab = this.symTabMap.get(node.leftMostChild.data.toString());
        node.symTabEntry = new SymTabEntry(symTab.symList.get(symTab.symList.size()-1));

        List<SymTabEntry> params = symTab.symList.stream().
                filter(i -> i.kind.equalsIgnoreCase("fParam")).collect(Collectors.toList());
        Node aParam = node.leftMostChild.rightSib;
        if(aParam.getChildren().size() != params.size())
            errors.add("Num of parameters does not match for fcall:  "
                    + node.getChildren().get(0).data.toString());

        for(Node aParamChild : aParam.getChildren()){
            String type = aParamChild.nodeType == NodeType.intNum ? "integer" : "float";
            if(!type.equalsIgnoreCase(params.get(0).type.split(" ")[0]))
                errors.add("Paramter type does not match for fcall:  "
                        + node.getChildren().get(0).data.toString());
            else
                params.remove(0);
        }
    }

    private void visitAssignStat(Node p_node, SymTab symTab) {

        if (p_node.getChildren().get(1).nodeType == NodeType.fCall &&
                p_node.getChildren().get(1).symTabEntry == null) {
            errors.add("Undefined fcall:  "
                    + p_node.getChildren().get(1).leftMostChild.data.toString());
            return;
        }
        symTab = updateSymTab(p_node);

        String leftOperandType = "";
        if (p_node.getChildren().get(0).nodeType == NodeType.var) {
            if (symTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString()) == null) {
                errors.add("Undefined var:  "
                        + p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
                return;
            }
            leftOperandType = symTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString()).type;
        } else
            leftOperandType = p_node.getChildren().get(0).symTabEntry.type;

        String rightOperandType = "";
        if (p_node.getChildren().get(1).nodeType == NodeType.var)
            rightOperandType = symTab.lookUp(p_node.getChildren().get(1).leftMostChild.leftMostChild.data.toString()).type;
        else
            rightOperandType = p_node.getChildren().get(1).symTabEntry.type;

        if (leftOperandType.split(" ")[0].equalsIgnoreCase(rightOperandType.split(" ")[0])) {
//            if(p_node.symTabEntry != null)
//                p_node.symTabEntry.type = leftOperandType;
        } else {
            errors.add("Assign type error:  "
                    + p_node.getChildren().get(0).data.toString() + " " + leftOperandType
                    + " and "
                    + p_node.getChildren().get(1).data.toString() + " " + rightOperandType);
        }

        //check if class has undefined members
        //  need to rework
        if (p_node.getChildren().get(0).nodeType == NodeType.var && symTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString()) != null) {
            SymTabEntry entry = symTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
            if(symTabMap.get(entry.type.split(" ")[0]) != null ){
                String className = entry.type.split(" ")[0];
                long libVarCount = symTabMap.get(entry.name).symList.stream().filter(x -> x.kind.equalsIgnoreCase("litvar")).count();
                if (libVarCount == 0)
                    errors.add("Undefined data member error in:  " + className);
            }
        }

        if (p_node.getChildren().get(1).nodeType == NodeType.var && symTab.lookUp(p_node.getChildren().get(1).leftMostChild.leftMostChild.data.toString()) != null) {
            SymTabEntry entry = symTab.lookUp(p_node.getChildren().get(1).leftMostChild.leftMostChild.data.toString());
            if(symTabMap.get(entry.type.split(" ")[0]) != null ){
                String className = entry.type.split(" ")[0];
                long libVarCount = symTabMap.get(className).symList.stream().filter(x -> x.kind.equalsIgnoreCase("litvar")).count();
                if (libVarCount == 0)
                    errors.add("Undefined data member error in:  " + className);
            }
        }
    }
    private SymTab updateSymTab(Node p_node){
        Node tmp;
        tmp = p_node;
        while((! Arrays.asList(NodeType.needTableType).contains(tmp.nodeType)) ||
                (tmp.nodeType == NodeType.statBlock && (tmp.parent.nodeType == NodeType.classDecl ||
                        tmp.parent.nodeType == NodeType.funcDef))){
            tmp=tmp.parent;
        }
        return tmp.symTab;
    }

    private void visitOp(Node p_node, SymTab symTab) {
        symTab = updateSymTab(p_node);

        String leftOperandType  = "";
        if(p_node.getChildren().get(0).nodeType == NodeType.var)
            leftOperandType  = symTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString()).type;
        else
            leftOperandType  = p_node.getChildren().get(0).symTabEntry.type;

        String rightOperandType  = "";
        if(p_node.getChildren().get(1).nodeType == NodeType.var)
            rightOperandType  = symTab.lookUp(p_node.getChildren().get(1).leftMostChild.leftMostChild.data.toString()).type;
        else
            rightOperandType  = p_node.getChildren().get(1).symTabEntry.type;

        if( leftOperandType.equalsIgnoreCase(rightOperandType) )
            p_node.symTabEntry.type = leftOperandType;
        else{
            errors.add("AddOp/MultiOp Node type error:  "
                    + p_node.getChildren().get(0).data.toString()
                    +  " and "
                    + p_node.getChildren().get(1).data.toString());
        }
    }

    private void visitFuncDef(Node node, SymTab symTab) {
        visitGeneral(node, symTab);
    }

    private void visitClassDecl(Node node, SymTab symTab) {
        for (SymTabEntry entry: symTab.symList) {
            if (entry.kind.equalsIgnoreCase("funcDecl") &&
                symTabMap.get(entry.name) == null) {
                errors.add("Find undefined function :  " + entry.name +
                        " in scope :" + symTab.name );
            }
        }
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
