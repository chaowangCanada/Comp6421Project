package com.concordia.comp6421.compiler.syntacticAnalyzer.Exception;

public class NoSuchLexElemException extends Exception {

    public NoSuchLexElemException(String lex){
        super(lex);
    }

    @Override
    public String toString() {
        return "There is no such " + this.getMessage() + " lexical element in language";
    }
}
