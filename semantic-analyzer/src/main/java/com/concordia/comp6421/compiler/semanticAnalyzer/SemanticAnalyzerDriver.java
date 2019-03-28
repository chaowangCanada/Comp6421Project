package com.concordia.comp6421.compiler.semanticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.SyntacticAnalyzerDriver;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;

public class SemanticAnalyzerDriver {
    public final String error_file = "error.log";
    public final String output_file = "output.log";
    private SyntacticAnalyzerDriver syntacticAnalyzerDriver;
    @Getter
    private SemanticAnalyzer semanticAnalyzer;


    public SemanticAnalyzerDriver() throws FileNotFoundException {
        syntacticAnalyzerDriver = new SyntacticAnalyzerDriver();
        semanticAnalyzer = new SemanticAnalyzer();
    }
    public void run(File inFile) throws Exception {
        syntacticAnalyzerDriver.run(inFile);
        semanticAnalyzer.setTree(syntacticAnalyzerDriver.getSyntacticAnalyzer().getTree());
    }
}
