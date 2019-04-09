package com.concordia.comp6421.compiler.syntacticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.TokenType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.CompilerFileReader;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Util;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.GRAMMAR_FILE;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class SyntacticAnalyzerDriver {

    public static final String error_file = "error.log";
    public static final String derivation_file = "output.log";
    public static final String astfile = "ASTtree.log";



    @Getter
    private SyntacticAnalyzer syntacticAnalyzer;
    private LexicalAnalyzer lexicalAnalyzer;

    public SyntacticAnalyzerDriver() throws FileNotFoundException {
        GrammarService grammarService = new GrammarService(GRAMMAR_FILE);
        syntacticAnalyzer = new SyntacticAnalyzer(grammarService.getGrammar());
        lexicalAnalyzer = new LexicalAnalyzer("");
    }

    public void run(File file) throws java.lang.Exception {
        List<String> output = CompilerFileReader.readAllLines(file, true);
        for(int lineNum = 1; lineNum <= output.size(); lineNum ++){
            String lineContent = output.get(lineNum-1);
            lexicalAnalyzer.setLineInfo(lineContent, lineNum);
            syntacticAnalyzer.parse(lexicalAnalyzer);
        }
        Util.log(derivation_file, syntacticAnalyzer.derivation, " ", true);
        Util.log(error_file, syntacticAnalyzer.errors);
        Util.log(astfile, Util.stringBuilder);
    }

}
