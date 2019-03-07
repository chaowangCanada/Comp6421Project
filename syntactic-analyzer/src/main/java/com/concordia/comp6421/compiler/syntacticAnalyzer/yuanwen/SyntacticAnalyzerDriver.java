package com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen;


import com.concordia.comp6421.compiler.syntacticAnalyzer.LexicalAnalyzer;
import java.io.IOException;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.GRAMMAR_FILE;

public class SyntacticAnalyzerDriver {
    private static SyntacticAnalyzer syntacticAnalyzer;

    static {
        try {
            GrammarReader grammarReader = new GrammarReader(GRAMMAR_FILE);
            grammarReader.process();
            syntacticAnalyzer = new SyntacticAnalyzer(grammarReader.getGrammar());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean parseFile(String fileName) throws IOException {
        LexicalAnalyzer lexical = new LexicalAnalyzer(fileName);
        return syntacticAnalyzer.parse(lexical);
    }
}
