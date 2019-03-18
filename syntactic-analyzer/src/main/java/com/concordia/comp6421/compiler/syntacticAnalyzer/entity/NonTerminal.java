package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.*;

public class NonTerminal extends Symbol{

    @Setter
    @Getter
    private List<Alpha> alphas;

    @Setter
    @Getter
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

    void addFollow(Symbol symbol) {
        follow.add(symbol);
    }


    public void generateFirst() {
        if(first == null)
            first = new HashSet<>();

        for (Alpha alpha : alphas) {
            List<Symbol> alphaSeq = alpha.symbolSeq;
            Set<Symbol> firstAlpha = new HashSet<>();
            boolean allHasEpsilon = true;
            for (Symbol eachAlphaSym : alphaSeq) {
                Set<Symbol> eachAlphaSymbolsFirst = new HashSet<>(eachAlphaSym.getFirst());
                if(!eachAlphaSymbolsFirst.contains(EPSILON)) {
                    allHasEpsilon = false;
                }
                else {
                    eachAlphaSymbolsFirst.remove(EPSILON);
                }
                firstAlpha.addAll(eachAlphaSymbolsFirst);
                if(!allHasEpsilon)
                    break;
            }
            if(allHasEpsilon) {
                firstAlpha.add(EPSILON);
            }
            alpha.addFirst(firstAlpha);
            first.addAll(firstAlpha);
        }
    }

//    void unionFollowSets() {
//        follow.addAll(extractFollows(this, new HashSet<>()));
//    }

    void unionFollowSets() {
        FOLLOW_REF_SYMBOL_MAP.add(this);
        Set<Symbol> set = new HashSet<>(this.follow);
        for (NonTerminal nonTerminal : this.followRef) {
            set.addAll(addEachNtFollowSet(nonTerminal));
        }
        follow.addAll(set);
    }

    public Set<Symbol> addEachNtFollowSet(NonTerminal nt){
        FOLLOW_REF_SYMBOL_MAP.add(nt);
        Set<Symbol> set = new HashSet<>(nt.follow);
        for (NonTerminal nonTerminal : this.followRef) {
            if(!FOLLOW_REF_SYMBOL_MAP.contains(nonTerminal)){
                FOLLOW_REF_SYMBOL_MAP.add(nonTerminal);
                set.addAll(nonTerminal.follow);
                set.addAll(addEachNtFollowSet(nonTerminal));
            }
        }
        return set;
    }

    void addFollowRef(NonTerminal nt) {
        if (nt != this) {
            followRef.add(nt);

        }
    }

    void generateFollow() {
        if (this.symbol.equals(START)) {
            this.follow.add(DOLLAR);
        }

        for (Alpha alpha : getAlphas()) {
            Queue<Symbol> sQueue = new ArrayDeque<>(alpha.symbolSeq);
            while (!sQueue.isEmpty()) {
                Symbol symbol = sQueue.poll();
                if (symbol instanceof NonTerminal) {
                    NonTerminal ntTmp = new NonTerminal("tmp", Collections.singletonList(Alpha.of(sQueue)));
                    ((NonTerminal) symbol).addFollow(ntTmp);
//                    NonTerminal ntTmp = (NonTerminal) sQueue.peek();
                    ((NonTerminal) symbol).addFollow(ntTmp.getFirst());
                    if (ntTmp.getFirst().contains(EPSILON)) {
                        ((NonTerminal) symbol).addFollowRef(this);
                    }
                }
            }
        }
    }
}
