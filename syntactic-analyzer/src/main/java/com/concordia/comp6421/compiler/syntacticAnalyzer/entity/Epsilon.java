package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import java.util.Set;

public class Epsilon extends Symbol{
    private static Epsilon epsilon = new Epsilon("EPSILON");

    public Epsilon(String symbol) {
        super("EPSILON");
    }

    public static Symbol get() {
        return epsilon;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isEpsilon() {
        return true;
    }

    @Override
    public boolean isNonTerminal() {
        return false;
    }

    @Override
    public Set<Symbol> getFirst() {
        return null;
    }
}
