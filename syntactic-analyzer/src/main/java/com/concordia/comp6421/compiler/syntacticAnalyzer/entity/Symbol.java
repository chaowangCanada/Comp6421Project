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

    @Generated
    String symbol;

    public static Symbol getSymbol(String name){
        if (name.equalsIgnoreCase("EPSILON"))
            return new Epsilon();
        else if(TokenType.lookup(name) == null )
            return new Terminal(name);
        else
            return new NonTerminal(name);
    }

    public abstract Set<Symbol> getFirst();


}
