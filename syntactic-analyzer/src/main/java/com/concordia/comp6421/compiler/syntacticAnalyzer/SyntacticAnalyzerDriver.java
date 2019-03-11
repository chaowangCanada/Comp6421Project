package com.concordia.comp6421.compiler.syntacticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.TokenType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.CompilerFileReader;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.GRAMMAR_FILE;

public class SyntacticAnalyzerDriver {

    public final String error_file = "error.log";
    public final String token_file = "output.log";
    private SyntacticAnalyzer syntacticAnalyzer;
    private LexicalAnalyzer lexicalAnalyzer;

    public SyntacticAnalyzerDriver() throws FileNotFoundException {
        GrammarService grammarService = new GrammarService(GRAMMAR_FILE);
        syntacticAnalyzer = new SyntacticAnalyzer(grammarService.getGrammar());
        lexicalAnalyzer = new LexicalAnalyzer("");
    }

    public void run(File file) throws java.lang.Exception {
        List<String> output = CompilerFileReader.readAllLines(file);
        for(int lineNum = 1; lineNum <= output.size(); lineNum ++){
            String lineContent = output.get(lineNum-1);
            lexicalAnalyzer.setLineInfo(lineContent, lineNum);
            syntacticAnalyzer.parse(lexicalAnalyzer);
        }
    }

}
