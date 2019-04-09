package visitor;

import com.concordia.comp6421.compiler.common.CompilerException;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTabEntry;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.Visitor;
import instruction.*;
import javafx.util.Pair;
import model.CodeGenerateContext;
import model.LabelGenerator;
import model.MathOpt;
import model.Register;
import instruction.BranchInstruction.*;

import java.awt.*;
import java.util.Arrays;

import static model.MathOpt.fromToken;

public class StackBasedCodeGenerationVisitor extends Visitor {

    private CodeGenerateContext context;
    private SymTab currentSymTab= null;

    public StackBasedCodeGenerationVisitor(CodeGenerateContext context) {
        this.context = context;
    }

    @Override
    public void visit(Node p_node) {
        try {
//            if (p_node.nodeType == NodeType.varDecl)
//                visitVarDeclNode(p_node);
//            else
            if (p_node.nodeType == NodeType.statBlock && p_node.parent.nodeType == NodeType.prog)
                visitMainNode(p_node);
            else if (p_node.nodeType == NodeType.intNum || p_node.nodeType == NodeType.floatNum)
                visitNumNode(p_node);
            else if (p_node.nodeType == NodeType.assignStat)
                visitAssignStatNode(p_node);
            else if(p_node.nodeType == NodeType.addOp || p_node.nodeType == NodeType.multOp
                    || p_node.nodeType == NodeType.relExpr)
                visitOpNode(p_node);
            else if (p_node.nodeType == NodeType.putStat )
                visitPutStatNode(p_node);
            else if (p_node.nodeType == NodeType.getStat)
                visitGetStatNode(p_node);
            else if (p_node.nodeType == NodeType.ifStat)
                visitIfStatNode(p_node);
            else if(p_node.nodeType == NodeType.forStat)
                visitForStatNode(p_node);
            else if(p_node.nodeType == NodeType.funcDef)
                visitFuncDefNode(p_node);
            else if(p_node.nodeType == NodeType.returnStat)
                visitReturnStat(p_node);
            else if(p_node.nodeType == NodeType.fCall)
                visitFcallNode(p_node);
            else {
                propogateChild(p_node);
            }
        } catch (CompilerException e) {
        e.printStackTrace();
        }
    }

    private void visitFcallNode(Node p_node) throws CompilerException {
        updateSymTab(p_node);
        propogateChild(p_node);

        Register localReg = context.registerManager.getAvailableRegister();

        SymTab funcTab = LabelGenerator.Table_Definition_Map.get(new Pair<>(p_node.leftMostChild.data.toString(), NodeType.funcDef));
        int indexofparam = 0;

        for(Node param : p_node.getChildren().get(1).getChildren()) {
            context.appendInstruction(new LWInstruction(localReg, Register.FRAME_POINTER, currentSymTab.lookUp(param.leftMostChild.leftMostChild.data.toString()).offset));
            int offsetofparam = currentSymTab.size + funcTab.symList.get(indexofparam).offset;
            context.appendInstruction(new SWInstruction(offsetofparam, Register.FRAME_POINTER, localReg).setComment("add parameter"));
            indexofparam ++;
        }

        context.appendInstruction(new SWInstruction(
                0 - 4, Register.STACK_POINTER, Register.FRAME_POINTER
        ).setComment("store the previous frame pointer"));
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode, Register.FRAME_POINTER, Register.STACK_POINTER, 0 - 4
        ).setComment("update the frame pointer"));

        // call jump and link instruction
        String label = LabelGenerator.function_pointer_table.get(p_node.leftMostChild.data.toString());
        context.appendInstruction(new JumpAndLinkInstruction(Register.RETURN, LabelGenerator.function_pointer_table.get(p_node.data.toString()))
                .setComment("store return address and jump to " +  LabelGenerator.function_pointer_table.get(p_node.data.toString())));
    }



    private void visitReturnStat(Node p_node) throws CompilerException {
        updateSymTab(p_node);
        propogateChild(p_node);

        Register localReg = context.registerManager.getAvailableRegister();

        SymTabEntry tmpEntry;
        String name = p_node.getChildren().get(0).symTabEntry == null ? p_node.leftMostChild.leftMostChild.leftMostChild.data.toString() :
                p_node.getChildren().get(0).symTabEntry.name;
        tmpEntry = currentSymTab.lookUp(name);
        context.appendInstruction(new LWInstruction(localReg, Register.FRAME_POINTER, tmpEntry.offset));


        context.appendInstruction( new MathOptInstruction(
                MathOpt.ADD.opcode, Register.RETURN, Register.ZERO, localReg).setComment("return value is a register value, get its value"));

        context.registerManager.freeRegister(localReg);

    }

    private void visitFuncDefNode(Node p_node) throws CompilerException {
        updateSymTab(p_node);

        // start branch, jump code
        int labelId = LabelGenerator.instance.getUniqueLabel();
        String funcLabel = "f1149672465" + labelId;

        LabelGenerator.function_pointer_table.put(currentSymTab.name.split(" ")[0], funcLabel);

        context.appendInstruction(new SWInstruction(4, Register.FRAME_POINTER, Register.RETURN)
                .setComment("store return address").setLabel(funcLabel));

        propogateChild(p_node);

        // get the return address
        Register tmp = context.registerManager.getAvailableRegister();
        context.appendInstruction(new LWInstruction(tmp, Register.FRAME_POINTER, 4)
                .setComment("get return address"));

        // reset the stack pointer and frame pointer
        context.appendInstruction(new LWInstruction(Register.FRAME_POINTER, Register.FRAME_POINTER, 0)
                .setComment("load the previous frame pointer address"));
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode, Register.STACK_POINTER,
                Register.STACK_POINTER, currentSymTab.size)
                .setComment("reset the stack pointer"));

        // jump back to the calling place
        context.appendInstruction(new JumpAndLinkInstruction(tmp));
        context.registerManager.freeRegister(tmp);

    }

    private void visitForStatNode(Node p_node) throws CompilerException {
        updateSymTab(p_node);

        // start branch, jump code
        int labelId = LabelGenerator.instance.getUniqueLabel();
        String loopTopLabel = "loop_top_" + labelId;
        String loopEndLabel = "loop_end_" + labelId;

        // generate initialization code for forloop statement
        context.setNextComment("for loop initialization");
        p_node.getChildren().get(1)._accept(this);

        // generate the condition statement code, every time loop begin from here
        context.setNextLabel(loopTopLabel);
        p_node.getChildren().get(2)._accept(this);

        Register tmp = context.registerManager.getAvailableRegister();
        context.appendInstruction(new BranchInstruction(BranchInstruction.Kind.IfZero, tmp, loopEndLabel)
                                            .setComment("break out of loop"));
        if (!tmp.reserved)
            context.registerManager.freeRegister(tmp);

        // generate the operation code (usually increment or decrement operation)
        p_node.getChildren().get(3)._accept(this);
        propogateChild(p_node.getChildren().get(4));

        // jump back to the condition position
        context.appendInstruction(new JumpAndLinkInstruction(loopTopLabel));
        // jump out of the loop code
        context.appendInstruction(new NoopInstruction().setLabel(loopEndLabel));
    }

    private void visitIfStatNode(Node p_node) throws CompilerException {
        updateSymTab(p_node);

        if(p_node.getChildren().get(0) != null)
            p_node.getChildren().get(0)._accept(this);

        Register localReg = context.registerManager.getAvailableRegister();
        SymTabEntry tmpEntry;
        if(p_node.leftMostChild.nodeType == NodeType.var)
            tmpEntry  = currentSymTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
        else
            tmpEntry = currentSymTab.lookUp(p_node.leftMostChild.symTabEntry.name);

        context.appendInstruction(new LWInstruction(localReg, Register.FRAME_POINTER, tmpEntry.offset));

        // start branch, jump code
        int labelId = LabelGenerator.instance.getUniqueLabel();
        String elseLabel  = "else_"  + labelId;
        String endifLabel = "endif_" + labelId;

        // zero means false, so if the condition is false, jump to the else block
        context.appendInstruction(new BranchInstruction(
                p_node.data.toString().equalsIgnoreCase("==") ? Kind.IfZero : Kind.IfNonZero ,
                localReg, elseLabel).setComment("if statement"));

        context.registerManager.freeRegister(localReg);
        // if the condition being satisfied, program will execute the then block
        // instead of jumping to the else block
        propogateChild(p_node.getChildren().get(1));
//        if(p_node.getChildren().size() >1 )
//            p_node.getChildren().get(1)._accept(this);

        // if the program execute here, need to jump out of the if statement, since the following
        // code is the else block which do not need to be executed
        context.appendInstruction(new JumpAndLinkInstruction(endifLabel)
                .setComment("jump out of the else block"));

        // else block code is always following the then block code
        context.setNextLabel(elseLabel);
        propogateChild(p_node.getChildren().get(2));
//        if(p_node.getChildren().size() >2 )
//           p_node.getChildren().get(2)._accept(this);

        // end of the if statement
        context.appendInstruction(new NoopInstruction().setLabel(endifLabel)
                .setComment("end of the if statement"));
    }

    private void visitGetStatNode(Node p_node) throws CompilerException {
        propogateChild(p_node);
        updateSymTab(p_node);

        Register localReg = context.registerManager.getAvailableRegister();
        context.appendInstruction(new GetInstruction(localReg));

        SymTabEntry tmpEntry;
        if(p_node.leftMostChild.nodeType == NodeType.var)
            tmpEntry  = currentSymTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
        else
            tmpEntry = currentSymTab.lookUp(p_node.leftMostChild.symTabEntry.name);


        context.appendInstruction(new SWInstruction(tmpEntry.offset, Register.FRAME_POINTER, localReg));

        context.registerManager.freeRegister(localReg);


    }

    private void visitPutStatNode(Node p_node) throws CompilerException{
        propogateChild(p_node);
        updateSymTab(p_node);

        Register localReg = context.registerManager.getAvailableRegister();
        SymTabEntry tmpEntry;

        if(p_node.leftMostChild.nodeType == NodeType.var)
            tmpEntry  = currentSymTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
        else
            tmpEntry = currentSymTab.lookUp(p_node.leftMostChild.symTabEntry.name);

        context.appendInstruction(new LWInstruction(localReg, Register.FRAME_POINTER, tmpEntry.offset));
        context.appendInstruction(new PutInstruction(localReg));

        context.registerManager.freeRegister(localReg);
    }

    private void visitOpNode(Node p_node) throws CompilerException {
        propogateChild(p_node);
        updateSymTab(p_node);

        Register localRegLw1 = context.registerManager.getAvailableRegister();
        Register localRegLw2 = context.registerManager.getAvailableRegister();
        Register localRegAdd = context.registerManager.getAvailableRegister();

        SymTabEntry entry;
        if(p_node.leftMostChild.nodeType == NodeType.var)
            entry  = currentSymTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
        else
            entry = currentSymTab.lookUp(p_node.leftMostChild.symTabEntry.name);

        context.appendInstruction(new LWInstruction(localRegLw1, Register.FRAME_POINTER, entry.offset));

        if(p_node.getChildren().get(1).nodeType == NodeType.var)
            entry  = currentSymTab.lookUp(p_node.getChildren().get(1).leftMostChild.leftMostChild.data.toString());
        else
            entry = currentSymTab.lookUp(p_node.getChildren().get(1).symTabEntry.name);

        context.appendInstruction(new LWInstruction(localRegLw2, Register.FRAME_POINTER, entry.offset));

        context.appendInstruction(new MathOptInstruction(fromToken(p_node.data.toString()).opcode,
                localRegAdd, localRegLw1, localRegLw2));

        entry  = currentSymTab.lookUp(p_node.symTabEntry.name);
        context.appendInstruction(new SWInstruction(entry.offset, Register.FRAME_POINTER, localRegAdd));

        context.registerManager.freeRegister(localRegAdd);
        context.registerManager.freeRegister(localRegLw1);
        context.registerManager.freeRegister(localRegLw2);
    }

    private void visitRelOpNode(Node p_node) throws CompilerException {
        propogateChild(p_node);
        updateSymTab(p_node);

        Register localRegLw1 = context.registerManager.getAvailableRegister();
        Register localRegLw2 = context.registerManager.getAvailableRegister();

        SymTabEntry entry;
        if(p_node.leftMostChild.nodeType == NodeType.var)
            entry  = currentSymTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
        else
            entry = currentSymTab.lookUp(p_node.leftMostChild.symTabEntry.name);

        context.appendInstruction(new LWInstruction(localRegLw1, Register.FRAME_POINTER, entry.offset));

        if(p_node.getChildren().get(1).nodeType == NodeType.var)
            entry  = currentSymTab.lookUp(p_node.getChildren().get(1).leftMostChild.leftMostChild.data.toString());
        else
            entry = currentSymTab.lookUp(p_node.getChildren().get(1).symTabEntry.name);

        context.appendInstruction(new MathOptInstruction(fromToken(p_node.data.toString()).opcode,
                localRegLw1, localRegLw1, localRegLw2));

        context.registerManager.freeRegister(localRegLw1);
        context.registerManager.freeRegister(localRegLw2);
    }

    private void visitAssignStatNode(Node p_node) throws CompilerException {
        propogateChild(p_node);
        updateSymTab(p_node);

        Register localReg = context.registerManager.getAvailableRegister();

        SymTabEntry tmpEntry;
        tmpEntry = currentSymTab.lookUp(p_node.getChildren().get(1).symTabEntry.name);
        context.appendInstruction(new LWInstruction(localReg, Register.FRAME_POINTER, tmpEntry.offset));

        if(p_node.leftMostChild.nodeType == NodeType.var) {
            tmpEntry  = currentSymTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
//            while(tmpEntry == null ){
//                updateSymTab(p_node);
//                tmpEntry  = currentSymTab.lookUp(p_node.leftMostChild.leftMostChild.leftMostChild.data.toString());
//            }
        }
        else {
            String name = p_node.leftMostChild.symTabEntry == null ? p_node.leftMostChild.data.toString()
                    : p_node.leftMostChild.symTabEntry.name;
            tmpEntry = currentSymTab.lookUp(name);
        }

        context.appendInstruction(new SWInstruction(tmpEntry.offset, Register.FRAME_POINTER, localReg));

        context.registerManager.freeRegister(localReg);

    }

    private void propogateChild(Node p_node){
        if(p_node.getChildren() != null) {
            for (Node child : p_node.getChildren() )
                child._accept(this);
        }
    }

    private void updateSymTab(Node p_node){
        Node tmp;
        tmp = p_node;
        while((! Arrays.asList(NodeType.needTableType).contains(tmp.nodeType)) ||
                (tmp.nodeType == NodeType.statBlock && (tmp.parent.nodeType != NodeType.prog &&
                        tmp.parent.nodeType != NodeType.classDecl &&
                        tmp.parent.nodeType != NodeType.funcDef))){
            tmp=tmp.parent;
        }
        this.currentSymTab = tmp.symTab;
    }

    private void visitNumNode(Node p_node) throws CompilerException {
        propogateChild(p_node);

        Register localReg = context.registerManager.getAvailableRegister();
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode,
                localReg,
                Register.ZERO,
                p_node.data.toString()
        ));

        context.appendInstruction(new SWInstruction(
                p_node.symTabEntry.offset,
                Register.FRAME_POINTER,
                localReg));
        context.registerManager.freeRegister(localReg);
    }

    private void visitMainNode(Node p_node) {
        context.appendInstruction(new EntryInstruction().setComment("======program entry======"));
        context.appendInstruction(new AlignInstruction().setComment("following instruction align"));
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode,
                Register.STACK_POINTER,
                Register.ZERO,
                "topaddr"
        ).setComment("initialize the stack pointer"));

        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode,
                Register.FRAME_POINTER,
                Register.ZERO,
                "topaddr"
        ).setComment("initialize the frame pointer"));

        // move stack pointer to the top of the stack
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.SUBTRACT.immediateOpcode,
                Register.STACK_POINTER,
                Register.STACK_POINTER,
                Math.abs(p_node.symTab.size)
        ).setComment("set the stack pointer to the top position of the stack"));

        for (Node child : p_node.getChildren())
            child._accept(this);

        context.appendInstruction(new HltInstruction().setComment("======end of program======"));
    }
//
//    private void visitVarDeclNode(Node node) {
//        if (node.getChildren().get(0).nodeType == NodeType.type) {
//            if(node.getChildren().get(0).data.toString().equalsIgnoreCase("integer")){
//                MOON_DATA_CODE.append("     % space for varible").append(node.getChildren().get(1).data.toString()).append(System.lineSeparator());
//                MOON_DATA_CODE.append(node.getChildren().get(1).data.toString()).append( "     res 4").append(System.lineSeparator());
//
//            }
//
//        }
//    }
}
