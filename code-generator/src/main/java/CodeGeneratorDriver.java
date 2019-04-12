
import com.concordia.comp6421.compiler.syntacticAnalyzer.SyntacticAnalyzerDriver;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Util;
import lombok.Getter;
import visitor.CodeGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CodeGeneratorDriver {
    public final String table_file = "table.log";
    public final String output_file = "moon.log";
    private SyntacticAnalyzerDriver syntacticAnalyzerDriver;
    private CodeGenerator codeGenerator;

    public CodeGenerator getCodeGenerator() {
        return  codeGenerator;
    }

    public CodeGeneratorDriver() throws FileNotFoundException {
        syntacticAnalyzerDriver = new SyntacticAnalyzerDriver();
        codeGenerator = new CodeGenerator();
    }
    public void run(File inFile, boolean hasCodeGen) throws Exception {
        syntacticAnalyzerDriver.run(inFile);
        codeGenerator.buildTables(syntacticAnalyzerDriver.getSyntacticAnalyzer().getTree());
        if(hasCodeGen)
            codeGenerator.generate();
        Util.printSymbolTable(codeGenerator.getTree());
        Util.log(table_file, Util.tableBuilder);
        Util.log(output_file, codeGenerator.context.instructions.stream().map(Object::toString).collect(Collectors.toList()));
    }
}
