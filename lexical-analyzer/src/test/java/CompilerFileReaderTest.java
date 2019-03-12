import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.CompilerFileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import static org.junit.Assert.*;

public class CompilerFileReaderTest {

    private ClassLoader classLoader;

    @Before
    public void executeBeforeEach(){
        classLoader = new CompilerFileReaderTest().getClass().getClassLoader();
    }

    @Test
    public void readFileGiveSomeString()
    {
        String fileName = "ReaderTest1.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        List<String> list = CompilerFileReader.readAllLines(file, false);
        assertEquals(list.size() , 2);
    }

    @Test
    public void readFileGiveOnelineComment()
    {
        String fileName = "ReaderTest2.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        List<String> list = CompilerFileReader.readAllLines(file, false);
        assertEquals(list.size() , 3);
    }

    @Test
    public void readFileGiveManyLinesComment()
    {
        String fileName = "ReaderTest3.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        List<String> list = CompilerFileReader.readAllLines(file, false);
        assertEquals(list.size() , 3);
    }
}
