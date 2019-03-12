package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import java.util.HashSet;
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
    public Set<Symbol> getFirst(){
        Set<Symbol> tmp = new HashSet<>();
        tmp.add(this);
        return tmp;
    }
}
