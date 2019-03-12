package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Symbol getOrAdd(String s) {
        if (symbolMap.get(s) != null) {
            return symbolMap.get(s);
        } else {
            Symbol symbol = Symbol.getSymbol(s);
            symbolMap.put(s, symbol);
            if ( !(symbol instanceof Terminal)  && !(symbol instanceof Epsilon)){
                nonTerminals.add((NonTerminal) symbol);
            }
            return symbol;
        }
    }

    public void buildFirst(){
//        buildSets(FIRST_SET);

        nonTerminals.forEach(NonTerminal::getFirst);
    }


    private void buildSets(File setFile){
        BufferedReader br = null;
        String line;
        try
        {
            br = new BufferedReader(new FileReader(setFile));

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String a = line.split("->")[0].trim();
                String rhs = line.split("->")[1].trim();
                for(Symbol symbol : nonTerminals) {
                    if(symbol.toString().equalsIgnoreCase(a)){
                        Set<Symbol> first = new HashSet<>();
                        String[] tmpArr = rhs.split(", ");
                        for (String str : tmpArr)
                            first.add(new NonTerminal(str));
                        ((NonTerminal)symbol).setFirst(first);
                    }
                }
            }

            if(br != null)
                br.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void buildFollow(){
        NonTerminal start = (NonTerminal) symbolMap.get(START);
        start.addFollow(DOLLAR);
        nonTerminals.forEach(NonTerminal::buildFollow);
        nonTerminals.forEach(NonTerminal::unionFollowSets);
//        buildSets(SECOND_SET);
    }

    public void buildTable() {
        for (NonTerminal nt : nonTerminals){
            for (Alpha alpha : nt.getAlphas()){
                Rule rule = Rule.of(nt, alpha);
                for(Symbol f1 : alpha.getFirst()){
                    if (f1 instanceof Terminal){
                        ruleTable.put(TableKey.of(nt.symbol, f1.symbol), rule);
                    }

                    if (f1 instanceof Epsilon){
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
