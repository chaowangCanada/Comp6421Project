import com.concordia.comp6421.compiler.semanticAnalyzer.SemanticAnalyzerDriver;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Util;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class SemanticAnalyzerTest {
    private ClassLoader classLoader;
    private SemanticAnalyzerDriver semanticAnalyzerDriver;


    @Before
    public void executeBeforeEach() throws FileNotFoundException
    {
        classLoader = new SemanticAnalyzerTest().getClass().getClassLoader();
        semanticAnalyzerDriver = new SemanticAnalyzerDriver();
    }

    @Test
    public void testAnalyzer1() throws Exception
    {
        String fileName = "test_program_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file);

        Util.printLevelOrder(semanticAnalyzerDriver.getSemanticAnalyzer().getTree());

        Util.printSymbolTable(semanticAnalyzerDriver.getSemanticAnalyzer().getTree());
    }


    @Test
    public void testAnalyzer2() throws Exception
    {
        String fileName = "test_program_2.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file);

        Util.printLevelOrder(semanticAnalyzerDriver.getSemanticAnalyzer().getTree());

        Util.printSymbolTable(semanticAnalyzerDriver.getSemanticAnalyzer().getTree());
    }

}
