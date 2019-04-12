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
        semanticAnalyzerDriver.run(file,false);
    }


    @Test
    public void testAnalyzer2() throws Exception
    {
        String fileName = "test_program_2.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file,false);
    }


    @Test
    public void testSynTabOffset() throws Exception
    {
        String fileName = "test_offset_1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file,false);
    }

    @Test
    public void testTypeCheckingAssignment() throws Exception
    {
        String fileName = "TypeCheckingAssignment.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file,true);
    }

    @Test
    public void testTypeCheckingExpression() throws Exception
    {
        String fileName = "TypeCheckingExpression.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file, true);
    }

    @Test
    public void testTypeCheckingReturn() throws Exception
    {
        String fileName = "TypeCheckingReturn.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file, true);
    }

    @Test
    public void testIncorrectFunctionParam() throws Exception
    {
        String fileName = "IncorrectFunctionParameter.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file, true);
    }

//    @Test
//    public void testIncorrectArrayDimension() throws Exception
//    {
//        String fileName = "IncorrectArrayDimension.txt";
//        File file = new File(classLoader.getResource(fileName).getFile());
//        semanticAnalyzerDriver.run(file);
//    }
    @Test
    public void testUndefinedFunctionVariable() throws Exception
    {
        String fileName = "UndefinedFunctionVariable.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file, true);
    }

    @Test
    public void testUndefinedClassMember() throws Exception
    {
        String fileName = "UndefinedClass.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        semanticAnalyzerDriver.run(file, true);
    }
}
