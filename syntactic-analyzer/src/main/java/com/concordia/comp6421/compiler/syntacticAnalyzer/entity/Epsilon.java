package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import java.util.Set;

public class Epsilon extends Symbol{

    public Epsilon(String symbol) {
        super("EPSILON");
    }

    @Override
    public Set<Symbol> getFirst() {
        return null;
    }
}
