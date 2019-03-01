package com.concordia.comp6421.compiler.syntacticAnalyzer.application;

import java.util.*;

import static main.config.Default.EPSILON;

public class NonTerminal extends Symbol {
    private List<Alpha> alphas;
    private Set<Symbol> first;
    private Set<Symbol> follow;
    private Set<NonTerminal> followRef;

    public static NonTerminal of(String symbol, List<Alpha> alphas) {
        return new NonTerminal(symbol, alphas);
    }

    NonTerminal(String symbol, Collection<Alpha> alphas) {
        super(symbol);
        this.alphas = new ArrayList<>();
        this.alphas.addAll(alphas);
    }

    NonTerminal(String symbol) {
        super(symbol);
        this.follow = new HashSet<>();
        this.followRef = new HashSet<>();
    }

    void setAlphas(List<Alpha> alphas) {
        this.alphas = alphas;
    }

    void generateFirst() {
        first = new HashSet<>();
        for (Alpha alpha : alphas) {
            List<Symbol> symbolSeq = alpha.symbolSeq;
            Set<Symbol> firstAlpha = new HashSet<>();
            boolean allEpsilon = true;
            for (Symbol currSym : symbolSeq) {
                List<Symbol> firstTmp = new ArrayList<>(currSym.getFirst());
                firstAlpha.addAll(firstTmp);
                if (!currSym.getFirst().contains(EPSILON)) {
                    allEpsilon = false;
                    break;
                }
            }
            firstAlpha.remove(EPSILON);
            if (allEpsilon) {
                firstAlpha.add(EPSILON);
            }
            alpha.addFirst(firstAlpha);
            first.addAll(firstAlpha);
        }
    }

    void buildFollow() {
        for (Alpha alpha : getAlphas()) {
            Queue<Symbol> sQueue = new ArrayDeque<>(alpha.symbolSeq);
            while (!sQueue.isEmpty()) {
                Symbol sym = sQueue.poll();
                if (sym.isNonTerminal()) {
                    NonTerminal ntTmp = NonTerminal.of("tmp", Collections.singletonList(Alpha.of(sQueue)));
                    ((NonTerminal) sym).addFollow(ntTmp.getFirst());
                    if (ntTmp.getFirst().contains(EPSILON) || sQueue.isEmpty()) {
                        ((NonTerminal) sym).addFollowRef(this);
                    }
                }
            }
        }
    }

    @Override
    boolean isTerminal() {
        return false;
    }

    @Override
    boolean isEpsilon() {
        return false;
    }

    @Override
    boolean isNonTerminal() {
        return true;
    }

    @Override
    Set<Symbol> getFirst() {
        if (first == null) {
            generateFirst();
        }
        return first;
    }

    Set<Symbol> getFollow() {
        return follow;
    }

    List<Alpha> getAlphas() {
        return alphas;
    }

    void addFollow(Collection<Symbol> symbols) {
        follow.addAll(symbols);
        follow.remove(EPSILON);
    }

    void addFollow(Symbol symbol) {
        follow.add(symbol);
    }

    void addFollowRef(NonTerminal nt) {
        if (nt != this) {
            followRef.add(nt);
        }
    }

    void unionFollowSets() {
        follow.addAll(extractFollows(this, new HashSet<>()));
    }

    private static Set<Symbol> extractFollows(NonTerminal nt, Set<Symbol> visited) {
        visited.add(nt);
        Set<Symbol> set = new HashSet<>(nt.follow);
        nt.followRef.stream().filter(e -> !visited.contains(e)).forEach(e -> set.addAll(extractFollows(e, visited)));
        return set;
    }
}
