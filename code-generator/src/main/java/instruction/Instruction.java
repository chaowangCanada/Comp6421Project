package instruction;

import com.concordia.comp6421.compiler.common.CompilerException;

public abstract class Instruction {

    protected String label;
    protected String comment;

    public Instruction() {
        this.label = "";
        this.comment = "";
    }

    protected abstract String generateAssemblyCode() throws CompilerException;

    public String getAssemblyCode() throws CompilerException {
        return this.label + "\t" + generateAssemblyCode() + "\t% " + this.comment;
    }

    public Instruction setLabel(String label) {
        this.label = label;
        return this;
    }

    public Instruction setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public String toString() {
        try {
            return getAssemblyCode();
        } catch (CompilerException e) {
            e.printStackTrace();
        }
        return "error";
    }
}