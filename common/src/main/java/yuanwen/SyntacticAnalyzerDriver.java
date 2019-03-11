package yuanwen;

import main.lexical.LexicalAnalyzer;

import java.io.File;
import java.io.IOException;

import static main.config.Default.GRAMMAR_FILE;
import static main.config.Default.TEST_ROOT_PATH;

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
        LexicalAnalyzer lexical = new LexicalAnalyzer(new File(TEST_ROOT_PATH, fileName));
        return syntacticAnalyzer.parse(lexical);
    }
}
