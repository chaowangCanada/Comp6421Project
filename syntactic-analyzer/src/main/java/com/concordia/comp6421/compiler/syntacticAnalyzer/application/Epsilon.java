package com.concordia.comp6421.compiler.syntacticAnalyzer.application;

import java.util.HashSet;
import java.util.Set;

public class Epsilon extends Symbol{
    private static Epsilon epsilon = new Epsilon();

    private Epsilon() {
        super("EPSILON");
    }

    public static Symbol get() {
        return epsilon;
    }

    @Override
    boolean isTerminal() {
        return false;
    }

    @Override
    boolean isEpsilon() {
        return true;
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
