package model;

import com.concordia.comp6421.compiler.common.CompilerException;

import java.util.HashMap;
import java.util.Map;

public enum  MathOpt {

    ADD("+", "add", "addi", true){
        @Override
        public int operate(int a, int b){
            return a + b;
        }
    },
    SUBTRACT("-", "sub", "subi", false){
        @Override
        public int operate(int a, int b){
            return a - b;
        }
    },
    OR("or", "or", "ori", true){
        @Override
        public int operate(int a, int b){
            return a | b;
        }
    },

    MULTIPLY("*", "mul", "muli", true){
        @Override
        public int operate(int a, int b){
            return a * b;
        }
    },
    DIVIDE("/", "div", "divi", false){
        @Override
        public int operate(int a, int b){
            return a / b;
        }
    },
    AND("and", "and", "andi", true){
        @Override
        public int operate(int a, int b){
            return a & b;
        }
    },

    EQUALS("==", "ceq", "ceqi", true){
        @Override
        public int operate(int a, int b) {
            return a == b ? ~0 : 0;
        }
    },

    NOT_EQUALS("<>", "cne", "cnei", true){
        @Override
        public int operate(int a, int b) {
            return a != b ? ~0 : 0;
        }
    },

    LESS_THAN("<", "clt", "clti", false){
        @Override
        public int operate(int a, int b) {
            return a < b ? ~0 : 0;
        }
    },

    LESS_THAN_EQUALS("<=", "cle", "clei", false){
        @Override
        public int operate(int a, int b) {
            return a <= b ? ~0 : 0;
        }
    },

    GREATER_THAN(">", "cgt", "cgti", false){
        @Override
        public int operate(int a, int b) {
            return a > b ? ~0 : 0;
        }
    },

    GREATER_THAN_EQUALS(">=", "cge", "cgei", false){
        @Override
        public int operate(int a, int b) {
            return a >= b ? ~0 : 0;
        }
    },
    ;

    public final static Map<String, MathOpt> operators = new HashMap<>(values().length);

    static {
        for (MathOpt op : values()) {
            operators.put(op.symbol, op);
        }
    }

    public final String symbol;
    public final String opcode;
    public final String immediateOpcode;
    public boolean commulative;

    MathOpt(String symbol, String opcode, String immediateOpcode, boolean commulative) {
        this.symbol = symbol;
        this.opcode = opcode;
        this.immediateOpcode = immediateOpcode;
        this.commulative = commulative;
    }

    public abstract int operate(int a, int b);

    public static MathOpt fromToken(String tokenValue) throws CompilerException {
        MathOpt op = operators.get(tokenValue);
        if (op != null) return op;
        else throw new CompilerException("No such operation" + tokenValue + " defined");
    }

}

