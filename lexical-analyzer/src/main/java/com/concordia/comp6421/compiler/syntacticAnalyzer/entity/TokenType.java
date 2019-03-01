package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.EnumUtils;

import java.util.function.Function;

public enum TokenType {
    INT, INTEGER, FLOAT, IDENTIFIER, ERROR,

    EQEQ, LTGT, LT, GT,LEQ, GEQ,
    SEMICOLON, COMMA, DOT, COLON, COLONCOLON,

    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION,
    EQ, ANDAND, EXCLAMATION, OROR, EXP,

    LP, RP, LCP, RCP, LSP, RSP,
    SLASHSTAR,STARSLASH,SLASHSLASH,

    IF, THEN, ELSE, FOR, CLASS,
    READ, WRITE, RETURN, MAIN;

    private static final Function<String, TokenType> lookUpFunc =
            EnumUtils.lookupMap(TokenType.class, TokenType::toString);

    public static TokenType lookup(String s) {
        return lookUpFunc.apply(s);
    }
}
