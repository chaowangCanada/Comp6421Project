package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import java.util.HashSet;
import java.util.Set;

public class Terminal extends Symbol {

    public Terminal(String symbol) {
        super(symbol);
    }

    @Override
    public Set<Symbol> getFirst() {
        HashSet<Symbol> tmp = new HashSet<>();
        tmp.add(this);
        return tmp;
    }

}
