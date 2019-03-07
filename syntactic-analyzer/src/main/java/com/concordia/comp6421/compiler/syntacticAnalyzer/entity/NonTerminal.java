package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import java.util.Set;

public class NonTerminal extends Symbol{

    public NonTerminal(String symbol) {
        super(symbol);
    }

    @Override
    public Set<Symbol> getFirst() {
        return null;
    }
}
