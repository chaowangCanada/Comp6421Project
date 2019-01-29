import com.concordia.comp6421.compiler.lexicalAnalyzer.application.LexicalAnalyzer;
import com.concordia.comp6421.compiler.lexicalAnalyzer.application.LexicalAnalyzerDriver;
import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.TokenType;
import com.concordia.comp6421.compiler.lexicalAnalyzer.utils.CompilerFileReader;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class IntegrationTest
{
    private ClassLoader classLoader;
    private LexicalAnalyzerDriver driver;

    @Before
    public void executeBeforeEach(){
        classLoader = new CompilerFileReaderTest().getClass().getClassLoader();
        driver = new LexicalAnalyzerDriver();
    }

    @Test
    public void readLexicalCaseFile() throws Exception
    {
        String fileName = "LexicalCase.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        driver.run(file);
        List<Token> tokenList = driver.getTokenList();
        List<Token> errorList = driver.getErrorTokenList();
        assertNotEquals(tokenList.size(), 0 );
        assertNotEquals(errorList.size(), 0 );
    }

    @Test
    public void readLexicalCommentFile() throws Exception
    {
        String fileName = "LexicalComment.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        driver.run(file);
        List<Token> tokenList = driver.getTokenList();
        List<Token> errorList = driver.getErrorTokenList();
        assertNotEquals(tokenList.size(), 0 );
        assertEquals(errorList.size(), 0 );
    }

    @Test
    public void readLexicalIdentifierFile() throws Exception
    {
        String fileName = "LexicalIdentifier.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        driver.run(file);
        List<Token> tokenList = driver.getTokenList();
        List<Token> errorList = driver.getErrorTokenList();
        assertNotEquals(tokenList.size(), 0 );
        assertNotEquals(errorList.size(), 0 );
    }

    @Test
    public void readLexicalKeywordFile() throws Exception
    {
        String fileName = "LexicalKeyword.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        driver.run(file);
        List<Token> tokenList = driver.getTokenList();
        List<Token> errorList = driver.getErrorTokenList();
        assertNotEquals(tokenList.size(), 0 );
        assertEquals(errorList.size(), 0 );
    }

    @Test
    public void readLexicalNumberFile() throws Exception
    {
        String fileName = "LexicalNumber.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        driver.run(file);
        List<Token> tokenList = driver.getTokenList();
        List<Token> errorList = driver.getErrorTokenList();
        assertNotEquals(tokenList.size(), 0 );
        assertEquals(errorList.size(), 0 );
    }

    @Test
    public void readLexicalOptorAndDemterFile() throws Exception
    {
        String fileName = "LexicalOptorAndDemter.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        driver.run(file);
        List<Token> tokenList = driver.getTokenList();
        List<Token> errorList = driver.getErrorTokenList();
        assertNotEquals(tokenList.size(), 0 );
        assertEquals(errorList.size(), 0 );
    }

    @Test
    public void readLexicalValidProgramFile() throws Exception
    {
        String fileName = "LexicalValidProgram.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        driver.run(file);
        List<Token> tokenList = driver.getTokenList();
        List<Token> errorList = driver.getErrorTokenList();
        assertNotEquals(tokenList.size(), 0 );
        assertEquals(errorList.size(), 0 );
    }
}
