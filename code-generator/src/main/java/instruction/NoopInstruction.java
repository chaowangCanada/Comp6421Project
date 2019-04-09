package instruction;

/**
 * Created by ERIC_LAI on 2017-04-06.
 */
public class NoopInstruction extends Instruction {

    @Override
    protected String generateAssemblyCode() {
        return "nop";
    }
}
