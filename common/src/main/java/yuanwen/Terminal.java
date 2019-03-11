package yuanwen;

import java.util.HashSet;
import java.util.Set;

public class Terminal extends Symbol {
    Terminal(String symbol) {
        super(symbol);
    }

    @Override
    boolean isTerminal() {
        return true;
    }

    @Override
    boolean isEpsilon() {
        return false;
    }

    @Override
    boolean isNonTerminal() {
        return false;
    }

    @Override
    Set<Symbol> getFirst() {
        Set<Symbol> tmp = new HashSet<>();
        tmp.add(this);
        return tmp;
    }
}
