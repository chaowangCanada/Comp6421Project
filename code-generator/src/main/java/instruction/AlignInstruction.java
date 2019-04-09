package instruction;

/**
 * Created by ERIC_LAI on 2017-03-31.
 */
public class AlignInstruction extends Instruction{

    @Override
    protected String generateAssemblyCode() {
        return "align";
    }
}
