package com.concordia.comp6421.compiler.syntacticAnalyzer.entity;

import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.EnumUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public enum TokenType {
    INT, IDENTIFIER, ERROR, INT_NUM, FLOAT_NUM,

    EQEQ, NEQ, LT, GT,LEQ, GEQ,
    SEMICOLON, COMMA, DOT, COLON, SR,

    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION,
    EQ, AND, EXCLAMATION, OR, EXP, NOT,

    LP, RP, LCP, RCP, LSP, RSP,
    SLASHSTAR,STARSLASH,SLASHSLASH,

    IF, THEN, ELSE, FOR, CLASS, INTEGER, FLOAT,
    READ, WRITE, RETURN, MAIN,

    INVALID_IDENTIFIER, INVALID_NUMBER;

    public static final Set<TokenType> KEY_WORDS =
            new HashSet<>(Arrays.asList(
                    IF, THEN, ELSE, FOR, CLASS, INTEGER, FLOAT, READ, WRITE, RETURN, MAIN));

    private static final Function<String, TokenType> lookUpFunc =
            EnumUtils.lookupMap(TokenType.class, TokenType::toString);

    public static TokenType lookup(String s) {
        return lookUpFunc.apply(s);
    }
}
