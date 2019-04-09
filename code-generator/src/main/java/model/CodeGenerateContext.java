package model;

import instruction.Instruction;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerateContext {

    public LabelGenerator labelGenerator;
    public RegisterManager registerManager;
    public List<Instruction> instructions;
    private String nextLabel;
    private String nextComment;

    public CodeGenerateContext() {
        labelGenerator = LabelGenerator.instance;
        registerManager = RegisterManager.geRegisterManager();
        instructions = new ArrayList<>();
    }

    public void appendInstruction(Instruction i) {
        if (nextLabel != null) {
            i.setLabel(nextLabel);
            nextLabel = null;
        }
        if (nextComment != null) {
            i.setComment(nextComment);
            nextComment = null;
        }
        instructions.add(i);
    }

    public void setNextLabel(String nextLabel) {
        this.nextLabel = nextLabel;
    }

    public void setNextComment(String nextComment) {
        this.nextComment = nextComment;
    }
}
