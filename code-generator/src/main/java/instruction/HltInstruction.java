package instruction;

/**
 * Created by ERIC_LAI on 2017-04-03.
 */
public class HltInstruction extends Instruction {
    @Override
    protected String generateAssemblyCode() {
        return "hlt";
    }
}
