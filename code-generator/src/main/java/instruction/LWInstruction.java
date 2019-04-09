package instruction;


import model.Register;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public class LWInstruction extends Instruction {

    private final Register destAddr;
    private final Register value;
    private final int offset;

//    public LWInstruction(RegisterValue destVal, RegisterValue srcValue, StaticValue offset) {
//        this.destAddr = destVal.getRegister();
//        this.value = srcValue.getRegister();
//        this.offset = offset.intValue();
//    }

    public LWInstruction(Register destAddr, Register value, int offset) {
        this.destAddr = destAddr;
        this.value = value;
        this.offset = offset;
    }

    @Override
    protected String generateAssemblyCode() {
        // according to the moon doc
        //   load word: lw  Ri, K(Rj) => Ri <-- M_32[Rj + K]
        return "lw" + "\t" + destAddr.alias + ", " + offset + "(" + value.alias + ")";
    }
}
