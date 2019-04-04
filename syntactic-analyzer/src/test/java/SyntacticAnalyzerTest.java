import com.concordia.comp6421.compiler.syntacticAnalyzer.SyntacticAnalyzerDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class SyntacticAnalyzerTest {
//    private static final String FOLDER = ".\\src\\test\\resources\\syntactic";
    private ClassLoader classLoader;
    private SyntacticAnalyzerDriver syntacticAnalyzerDriver;


    @Before
    public void executeBeforeEach() throws FileNotFoundException
    {
        classLoader = new SyntacticAnalyzerTest().getClass().getClassLoader();
        syntacticAnalyzerDriver = new SyntacticAnalyzerDriver();
    }

    @Test
    public void testAnalyzer1() throws Exception
    {
        String fileName = "test_program_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        syntacticAnalyzerDriver.run(file);
    }

    @Test
    public void testAnalyzer2() throws Exception
    {
        String fileName = "test_program_2.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        syntacticAnalyzerDriver.run(file);
    }

    @Test
    public void testAnalyzer3() throws Exception
    {
        String fileName = "test_program_3.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        syntacticAnalyzerDriver.run(file);
    }

//    @Test
//    public void testAnalyzer4() throws Exception
//    {
//        String fileName = "test_program_4.txt";
//        File file = new File(classLoader.getResource(fileName).getFile());
//        syntacticAnalyzerDriver.run(file);
//    }
    @Test
    public void testAnalyzer5() throws Exception
    {
        String fileName = "test_program_5.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        syntacticAnalyzerDriver.run(file);
    }

    @Test
    public void testErrorRecover() throws Exception
    {
        String fileName = "error_test.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        syntacticAnalyzerDriver.run(file);
    }

    @Test
    public void testErrorRecover2() throws Exception
    {
        String fileName = "error_test2.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        syntacticAnalyzerDriver.run(file);
    }

    @Test
    public void testTree() throws Exception
    {
        String fileName = "test_tree.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        syntacticAnalyzerDriver.run(file);
    }

//    @Test
//    public void testAnalyzer6() throws Exception
//    {
//        String fileName = "test_program_6.txt";
//        File file = new File(classLoader.getResource(fileName).getFile());
//        syntacticAnalyzerDriver.run(file);
//    }

}
