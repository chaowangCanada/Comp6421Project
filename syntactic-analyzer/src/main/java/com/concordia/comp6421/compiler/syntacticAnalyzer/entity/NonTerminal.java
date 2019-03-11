package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.*;

public class NonTerminal extends Symbol{

    @Setter
    @Getter
    private List<Alpha> alphas;

    private Set<Symbol> first;

    @Setter
    @Getter
    private Set<Symbol> follow;

    @Setter
    @Getter
    private Set<NonTerminal> followRef;

    public NonTerminal(String name, Collection<Alpha> alphas) {
        super(name);
        this.alphas = new ArrayList<>();
        this.alphas.addAll(alphas);
    }

    public NonTerminal(String name){
        super(name);
        this.follow = new HashSet<>();
        this.followRef = new HashSet<>();
    }

    @Override
    public Set<Symbol> getFirst() {
        if (first == null) {
            generateFirst();
        }
        return first;
    }

    void addFollow(Collection<Symbol> symbols) {
        follow.addAll(symbols);
        follow.remove(EPSILON);
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

    public static NonTerminal of(String symbol, List<Alpha> alphas) {
        return new NonTerminal(symbol, alphas);
    }

    public void generateFirst() {
        if(first != null)
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

    void unionFollowSets() {
        follow.addAll(extractFollows(this, new HashSet<>()));
    }

    void addFollowRef(NonTerminal nt) {
        if (nt != this) {
            followRef.add(nt);
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

    private static Set<Symbol> extractFollows(NonTerminal nt, Set<Symbol> visited) {
        visited.add(nt);
        Set<Symbol> set = new HashSet<>(nt.follow);
        nt.followRef.stream().filter(e -> !visited.contains(e)).forEach(e -> set.addAll(extractFollows(e, visited)));
        return set;
    }

}
