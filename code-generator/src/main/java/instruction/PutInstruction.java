package instruction;


import model.Register;

/**
 * Created by ERIC_LAI on 2017-04-07.
 */
public class PutInstruction extends Instruction {

    private Register register;

    public PutInstruction(Register register) {
        this.register = register;
    }

    @Override
    protected String generateAssemblyCode() {
        return "putc" + "\t" + register.alias;
    }
}
