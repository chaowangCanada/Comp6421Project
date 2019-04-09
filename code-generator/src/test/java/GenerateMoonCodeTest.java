import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Util;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

public class GenerateMoonCodeTest {


    private ClassLoader classLoader;
    private CodeGeneratorDriver codeGeneratorDriver;

    @Before
    public void executeBeforeEach() throws FileNotFoundException
    {
        classLoader = new MemoryAllocationTest().getClass().getClassLoader();
        codeGeneratorDriver = new CodeGeneratorDriver();
    }

    @Test
    public void generateMoonCodeTest1() throws Exception
    {
        String fileName = "test_moon_code_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        codeGeneratorDriver.run(file);
        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
        System.out.println(codeGeneratorDriver.getCodeGenerator().context.instructions.stream().map(each -> each.toString()).collect(Collectors.joining("\n")));
    }

    @Test
    public void generateMoonCodeIfStatTest() throws Exception
    {
        String fileName = "test_if_statement_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        codeGeneratorDriver.run(file);
        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
        System.out.println(codeGeneratorDriver.getCodeGenerator().context.instructions.stream().map(each -> each.toString()).collect(Collectors.joining("\n")));
    }

    @Test
    public void generateMoonCodeForStatTest() throws Exception
    {
        String fileName = "test_for_statement_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        codeGeneratorDriver.run(file);
        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
        System.out.println(codeGeneratorDriver.getCodeGenerator().context.instructions.stream().map(each -> each.toString()).collect(Collectors.joining("\n")));
    }

    @Test
    public void generateMoonCodeReadWriteTest() throws Exception
    {
        String fileName = "test_read_write_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        codeGeneratorDriver.run(file);
        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
        System.out.println(codeGeneratorDriver.getCodeGenerator().context.instructions.stream().map(each -> each.toString()).collect(Collectors.joining("\n")));
    }

//    @Test
//    public void generateMoonCodeSimpleArrayTest() throws Exception
//    {
//        String fileName = "test_simple_array_1.txt";
//        File file = new File(classLoader.getResource(fileName).getFile());
//        codeGeneratorDriver.run(file);
//        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
//        System.out.println(codeGeneratorDriver.getCodeGenerator().context.instructions.stream().map(each -> each.toString()).collect(Collectors.joining("\n")));
//    }

    @Test
    public void generateMoonCodeFreeFunctionTest() throws Exception
    {
        String fileName = "test_free_function_assignment_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        codeGeneratorDriver.run(file);
        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
        System.out.println(codeGeneratorDriver.getCodeGenerator().context.instructions.stream().map(each -> each.toString()).collect(Collectors.joining("\n")));
    }


}

