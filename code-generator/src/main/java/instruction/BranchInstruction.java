package instruction;

import com.concordia.comp6421.compiler.common.CompilerException;
import model.Register;

/**
 * Created by ERIC_LAI on 2017-04-06.
 */
public class BranchInstruction extends Instruction {

    public enum Kind {
        IfZero,
        IfNonZero,
    }

    private Kind kind;
    private Register value;
    private String k;

    public BranchInstruction(Kind kind, Register value, String k) {
        this.kind = kind;
        this.value = value;
        this.k = k;
    }

    @Override
    protected String generateAssemblyCode() throws CompilerException {
        if (kind == Kind.IfZero) {
            return "bz" + "\t" + value.alias + ", " + k;
        } else if (kind == Kind.IfNonZero) {
            return "bnz" + "\t" + value.alias + ", " + k;
        } else throw new CompilerException("Unexpected kind command of BranchInstruction");
    }
}
