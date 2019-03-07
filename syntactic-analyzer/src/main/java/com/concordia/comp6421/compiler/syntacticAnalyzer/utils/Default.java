package com.concordia.comp6421.compiler.syntacticAnalyzer.utils;

import com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen.Epsilon;
import com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen.Symbol;
import com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen.Terminal;

import java.io.File;

public class Default {
    public static final String TEST_ROOT_PATH = "./src/test/resources/";

    public static final File GRAMMAR_FILE = new File("./src/main/config/grammar.txt");

    public static final Symbol EPSILON = Epsilon.get();


    public static final String START = "prog";

    public static final Symbol DOLLAR = Terminal.of ("$");
}
