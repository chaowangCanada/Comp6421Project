package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import java.util.*;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.*;

public class Grammar {
    private Map<String, Symbol> symbolMap;
    private List<NonTerminal>nonTerminals;
    private Map<TableKey, Rule> ruleTable;

    public Grammar() {
        symbolMap = new LinkedHashMap<>();
        symbolMap.put(DOLLAR.symbol, DOLLAR);
        nonTerminals = new ArrayList<>();
        ruleTable = new HashMap<>();
    }

    public Symbol getOrElseAdd(String s) {
        if (symbolMap.get(s) != null) {
            return symbolMap.get(s);
        } else {
            Symbol symbol = Symbol.getSymbol(s);
            symbolMap.put(s, symbol);
            if (!symbol.isTerminal() && !symbol.isEpsilon()){
                nonTerminals.add((NonTerminal) symbol);
            }
            return symbol;
        }
    }

    public void buildFirst(){
        nonTerminals.forEach(NonTerminal::getFirst);
    }

    public void buildFollow(){
        NonTerminal start = (NonTerminal) symbolMap.get(START);
        start.addFollow((Collection<Symbol>) DOLLAR);
        nonTerminals.forEach(NonTerminal::buildFollow);
        nonTerminals.forEach(NonTerminal::unionFollowSets);
    }

    public void buildTable() {
        for (NonTerminal nt : nonTerminals){
            for (Alpha alpha : nt.getAlphas()){
                Rule rule = Rule.of(nt, alpha);
                for(Symbol f1 : alpha.getFirst()){
                    if (f1.isTerminal()){
                        ruleTable.put(TableKey.of(nt.symbol, f1.symbol), rule);
                    }

                    if (f1.isEpsilon()){
                        for (Symbol f2 : nt.getFollow()){
                            ruleTable.put(TableKey.of(nt.symbol, f2.symbol), rule);
                        }
                    }
                }
            }
        }
    }

    public Symbol getStart(){
        return symbolMap.get(START);
    }

    public Rule getRule(String nt, String t){
        return ruleTable.get(TableKey.of(nt, t));
    }

    public Rule getRule(String nt, Token t){
        return ruleTable.get(TableKey.of(nt, t.getTokenType().toString()));
    }
}
