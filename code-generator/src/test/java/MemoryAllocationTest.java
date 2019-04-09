import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Util;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class MemoryAllocationTest {
    private ClassLoader classLoader;
    private CodeGeneratorDriver codeGeneratorDriver;


    @Before
    public void executeBeforeEach() throws FileNotFoundException
    {
        classLoader = new MemoryAllocationTest().getClass().getClassLoader();
        codeGeneratorDriver = new CodeGeneratorDriver();
    }

    @Test
    public void testSynTabOffset() throws Exception
    {
        String fileName = "test_offset_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        codeGeneratorDriver.run(file);

        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
    }

    @Test
    public void testSynTabOffset2() throws Exception
    {
        String fileName = "test_offset_2.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        codeGeneratorDriver.run(file);

        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
    }

    @Test
    public void testSynTabOffset3() throws Exception
    {
        String fileName = "test_offset_3.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        codeGeneratorDriver.run(file);

        Util.printSymbolTable(codeGeneratorDriver.getCodeGenerator().getTree());
    }
}
