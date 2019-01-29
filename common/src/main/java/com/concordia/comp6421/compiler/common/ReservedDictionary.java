package com.concordia.comp6421.compiler.common;

import java.util.Arrays;
import java.util.List;

public interface ReservedDictionary {

    List<String> KEY_WORDS = Arrays.asList(
            "if", "then", "else", "for", "class",
            "int", "float", "read", "write",
            "return", "main"
    );

    List<String> OPERATORS = Arrays.asList(
            "==", "<>", "<", ">", "<=", ">=",
            "+", "-", "*", "/", "=", "&&", "!", "||"
    );

    List<String> PUNCTUATION = Arrays.asList(
            ";", ",", ".", ":", ":",
            "(", ")", "{", "}", "[", "]"
    );

    List<String> COMMENTS = Arrays.asList(
            "/*", "*/", "//"
    );

    List<Character> WAIT_FURTHER = Arrays.asList(
        '=', '<', '>', ':'
        // these pairs are removed due to comment is regex replaced All , '/', '*'
    );
}
