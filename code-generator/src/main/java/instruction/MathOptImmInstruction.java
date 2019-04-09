package instruction;


import model.Register;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public class MathOptImmInstruction extends Instruction{

    private String op;
    private Register dest;
    private Register src;
    private String k;

    public MathOptImmInstruction(String op, Register dest, Register src, int k) {
        this.op = op;
        this.dest = dest;
        this.src = src;
        this.k = Integer.toString(k);
    }

    public MathOptImmInstruction(String op, Register dest, Register src, String k) {
        this.op = op;
        this.dest = dest;
        this.src = src;
        this.k = k;
    }

    @Override
    protected String generateAssemblyCode() {
        // according to the moon doc
        //   math operation: opt_name  Ri, Rj, Rk => Ri <-- Rj opt_name Rk
        return op + "\t" + dest.alias + ", " + src.alias + ", " + k;
    }
}
