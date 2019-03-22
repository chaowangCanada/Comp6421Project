package com.concordia.comp6421.compiler.syntacticAnalyzer.utils;


import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Epsilon;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Symbol;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Terminal;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Default {
    public static final String TEST_ROOT_PATH = "./src/test/resources/";

    public static final File GRAMMAR_FILE = new File("./src/main/config/grammar.txt");

    public static final File FIRST_SET = new File("./src/main/config/firstSet.txt");

    public static final File SECOND_SET = new File("./src/main/config/secondSet.txt");

    public static final Symbol EPSILON = Epsilon.get();

    public static final String START = "prog";

    public static final Symbol DOLLAR = Terminal.getSymbol("$");

    public static final String ACTION_PREFIX = "ACT_";

    public static Set<Symbol> FOLLOW_REF_SYMBOL_MAP = new HashSet<>();
}
