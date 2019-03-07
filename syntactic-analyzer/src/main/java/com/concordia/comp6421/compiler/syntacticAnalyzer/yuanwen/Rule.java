package com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen;

public class Rule {
    NonTerminal lhs;
    Alpha rhs;

    private Rule(NonTerminal lhs, Alpha rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public static Rule of(NonTerminal lhs, Alpha rhs){
        return new Rule(lhs, rhs);
    }


}
