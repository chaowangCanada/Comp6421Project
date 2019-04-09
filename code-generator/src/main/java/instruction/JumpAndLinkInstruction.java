package instruction;


import com.concordia.comp6421.compiler.common.CompilerException;
import model.Register;

/**
 * Created by ERIC_LAI on 2017-04-03.
 */
public class JumpAndLinkInstruction extends Instruction {

    public enum Kind {
        Jump,
        JumpRegister,
        JumpLink,
        JumpLinkRegister,
    }

    private Kind kind;
    private String jumpLabel;
    private Register destReg;
    private Register returnReg;

    public JumpAndLinkInstruction(String jumpLabel) {
        this.kind = Kind.Jump;
        this.jumpLabel = jumpLabel;
    }

    public JumpAndLinkInstruction(Register destReg) {
        this.kind = Kind.JumpRegister;
        this.destReg = destReg;
    }

    public JumpAndLinkInstruction(Register returnReg, String jumpLabel) {
        this.kind = Kind.JumpLink;
        this.jumpLabel = jumpLabel;
        this.returnReg = returnReg;
    }

    public JumpAndLinkInstruction(Register returnReg, Register destReg) {
        this.kind = Kind.JumpLinkRegister;
        this.destReg = destReg;
        this.returnReg = returnReg;
    }

    @Override
    protected String generateAssemblyCode() throws CompilerException {
        if (kind == Kind.Jump) {
            return "j" + "\t" + jumpLabel;
        } else if (kind == Kind.JumpRegister) {
            return "jr" + "\t" + destReg.alias;
        } else if (kind == Kind.JumpLink) {
            return "jl" + "\t" + returnReg.alias + ", " + jumpLabel;
        } else if (kind == Kind.JumpLinkRegister) {
            return "jlr" + "\t" + returnReg.alias + ", " + destReg.alias;
        }
        throw new CompilerException("No such jump or jump and link instruction!");
    }
}
