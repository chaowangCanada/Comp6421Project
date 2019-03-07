package com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen;

import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.TokenType;

import java.util.Objects;
import java.util.Set;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.EPSILON;

public abstract class Symbol {
    String symbol;

    public static Symbol of(String symbol) {
        if (symbol.equals(EPSILON.symbol)){
            return EPSILON;
        } else if (TokenType.lookup(symbol) != null) {
            return new Terminal(symbol);
        } else {
            return new NonTerminal(symbol);
        }
    }

    Symbol(String symbol) {
        this.symbol = symbol;
    }
    abstract boolean isTerminal();

    abstract boolean isEpsilon();

    abstract boolean isNonTerminal();

    abstract Set<Symbol> getFirst();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol symbol1 = (Symbol) o;
        return Objects.equals(symbol, symbol1.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString(){
        return symbol;
    }

    public boolean matchToken(Token t){
        return symbol.equals(t.getTokenType().toString());
    }
}
