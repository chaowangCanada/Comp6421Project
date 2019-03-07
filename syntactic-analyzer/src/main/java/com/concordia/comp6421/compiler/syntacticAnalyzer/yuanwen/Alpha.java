package com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Alpha {
    List<Symbol> symbolSeq;
    List<Symbol> first;

    public static Alpha of(Collection<Symbol> symbolSeq) {
        return new Alpha(symbolSeq);
    }

    Alpha(Collection<Symbol> symbolSeq) {
        this.symbolSeq = new ArrayList<>();
        this.symbolSeq.addAll(symbolSeq);
        this.first = new ArrayList<>();
    }

    void addFirst(Collection<Symbol> firsts) {
        first.addAll(firsts);
    }

    List<Symbol> getFirst() {
        return first;
    }
}
