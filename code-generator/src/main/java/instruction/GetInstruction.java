package instruction;


import model.Register;

/**
 * Created by ERIC_LAI on 2017-04-07.
 */
public class GetInstruction extends Instruction {

    private Register register;

    public GetInstruction(Register register) {
        this.register = register;
    }

    @Override
    protected String generateAssemblyCode() {
        return "getc" + "\t" + register.alias;
    }
}
