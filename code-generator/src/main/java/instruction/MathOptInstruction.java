package instruction;


import model.Register;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public class MathOptInstruction extends Instruction{

    private String op;
    private Register dest;
    private Register first;
    private Register second;

    public MathOptInstruction(String op, Register dest, Register first, Register second) {
        this.op = op;
        this.dest = dest;
        this.first = first;
        this.second = second;
    }

//    public MathOptInstruction(String op, RegisterValue dest, RegisterValue first, RegisterValue second) {
//        this.op = op;
//        this.dest = dest.getRegister();
//        this.first = first.getRegister();
//        this.second = second.getRegister();
//    }

    @Override
    protected String generateAssemblyCode() {
        // according to the moon doc
        //   math operation: opt_name  Ri, Rj, Rk => Ri <-- Rj opt_name Rk
        return op + '\t' + dest.alias + ", " + first.alias + ", " + second.alias;
    }
}
