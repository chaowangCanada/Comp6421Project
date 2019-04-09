
import com.concordia.comp6421.compiler.syntacticAnalyzer.SyntacticAnalyzerDriver;
import lombok.Getter;
import visitor.CodeGenerator;

import java.io.File;
import java.io.FileNotFoundException;

public class CodeGeneratorDriver {
    public final String error_file = "error.log";
    public final String output_file = "output.log";
    private SyntacticAnalyzerDriver syntacticAnalyzerDriver;
    private CodeGenerator codeGenerator;

    public CodeGenerator getCodeGenerator() {
        return  codeGenerator;
    }

    public CodeGeneratorDriver() throws FileNotFoundException {
        syntacticAnalyzerDriver = new SyntacticAnalyzerDriver();
        codeGenerator = new CodeGenerator();
    }
    public void run(File inFile) throws Exception {
        syntacticAnalyzerDriver.run(inFile);
        codeGenerator.buildTables(syntacticAnalyzerDriver.getSyntacticAnalyzer().getTree());
        codeGenerator.generate();
    }
}
