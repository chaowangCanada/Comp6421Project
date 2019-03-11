package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.ToString;

import java.util.Set;

@ToString
@AllArgsConstructor
@EqualsAndHashCode
public abstract class Symbol{


    public String symbol;

    public static Symbol of(String name){
        if (name.equalsIgnoreCase("EPSILON"))
            return new Epsilon("EPSILON");
        else if(TokenType.lookup(name) == null )
            return new Terminal(name);
        else
            return new NonTerminal(name);
    }

    public abstract boolean isTerminal();

    public abstract boolean isEpsilon();

    public abstract boolean isNonTerminal();

    public abstract Set<Symbol> getFirst();

    public boolean matchToken(Token t){
        return symbol.equals(t.getTokenType().toString());
    }

}
