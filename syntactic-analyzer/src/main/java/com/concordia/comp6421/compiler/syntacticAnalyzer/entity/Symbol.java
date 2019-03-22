package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.ToString;

import java.util.Set;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.ACTION_PREFIX;

@AllArgsConstructor
@EqualsAndHashCode
public abstract class Symbol{

    public String symbol;

    public static Symbol getSymbol(String name){
        if (name.equalsIgnoreCase("EPSILON"))
            return new Epsilon("EPSILON");
        else if(TokenType.lookup(name) != null )
            return new Terminal(name);
        else if (name.contains(ACTION_PREFIX))
            return new Action(name.replaceAll(ACTION_PREFIX, ""));
        else
            return new NonTerminal(name);
    }

    public abstract Set<Symbol> getFirst();

    public boolean matchToken(Token t){
        return this.symbol.equalsIgnoreCase(t.getTokenType().toString());
    }

    @Override
    public String toString(){
        return symbol;
    }

}
