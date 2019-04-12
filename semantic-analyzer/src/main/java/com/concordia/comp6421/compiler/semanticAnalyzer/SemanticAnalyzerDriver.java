package com.concordia.comp6421.compiler.semanticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.SyntacticAnalyzerDriver;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Util;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;

public class SemanticAnalyzerDriver {
    public final String type_check_error_file = "type_error.log";
    public final String table_output_file = "table_output.log";
    private SyntacticAnalyzerDriver syntacticAnalyzerDriver;
    public SemanticAnalyzer semanticAnalyzer;


    public SemanticAnalyzerDriver() throws FileNotFoundException {
        syntacticAnalyzerDriver = new SyntacticAnalyzerDriver();
        semanticAnalyzer = new SemanticAnalyzer();
    }
    public void run(File inFile, boolean hasCheck) throws Exception {
        syntacticAnalyzerDriver.run(inFile);
        semanticAnalyzer.validate(syntacticAnalyzerDriver.getSyntacticAnalyzer().getTree(), hasCheck);
        Util.printLevelOrder(semanticAnalyzer.tree);
        Util.printSymbolTable(semanticAnalyzer.tree);

        Util.log(table_output_file, Util.tableBuilder);
        Util.log(type_check_error_file, semanticAnalyzer.typeCheckingVisitor.errors);
        semanticAnalyzer.typeCheckingVisitor.errors.forEach(x -> System.out.println(x));
    }
}
