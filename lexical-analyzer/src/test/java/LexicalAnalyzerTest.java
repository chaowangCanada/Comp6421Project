import com.concordia.comp6421.compiler.lexicalAnalyzer.application.LexicalAnalyzer;
import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexicalAnalyzerTest {


    @Test
    public void identifierTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("abc");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.getValue(), "abc");
        assertEquals(token.getLocation(), 1);

    }

    @Test
    public void identifierTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("abc1");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.getValue(), "abc1");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void identifierTest3(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("abc_1");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.getValue(), "abc_1");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void identifierTest4(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("abc1_");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.getValue(), "abc1_");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void identifierIntegerTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("1abc");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.INTEGER);
        assertEquals(token.getValue(), "1");
        assertEquals(token.getLocation(), 1);
        token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.getValue(), "abc");
        assertEquals(token.getLocation(), 2);
    }

    @Test
    public void errorIntegerIdentifierTest(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("_1abc");

        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.ERROR);
        assertEquals(token.getValue(), "Not supported Lexical Symbol _");
        assertEquals(token.getLocation(), 1);

        token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.INTEGER);
        assertEquals(token.getValue(), "1");
        assertEquals(token.getLocation(), 2);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.getValue(), "abc");
        assertEquals(token.getLocation(), 3);
    }

    @Test
    public void errorIdentifierIntegerTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("_abc1");

        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.ERROR);
        assertEquals(token.getValue(), "Not supported Lexical Symbol _");
        assertEquals(token.getLocation(), 1);

        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.getValue(), "abc1");
        assertEquals(token.getLocation(), 2);

    }

    @Test
    public void integerTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12345");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.INTEGER);
        assertEquals(token.getValue(), "12345");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void integerTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("0123");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.INTEGER);
        assertEquals(token.getValue(), "0");
        assertEquals(token.getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.INTEGER);
        assertEquals(token.getValue(), "123");
        assertEquals(token.getLocation(), 2);
    }

    @Test
    public void integerTest3(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12300");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.INTEGER);
        assertEquals(token.getValue(), "12300");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void integerFloatTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("01.23");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.INTEGER);
        assertEquals(token.getValue(), "0");
        assertEquals(token.getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.FLOAT);
        assertEquals(token.getValue(), "1.23");
        assertEquals(token.getLocation(), 2);
    }

    @Test
    public void integerFloatTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("01.230");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.INTEGER);
        assertEquals(token.getValue(), "0");
        assertEquals(token.getLocation(), 1);

        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.FLOAT);
        assertEquals(token.getValue(), "1.230");
        assertEquals(token.getLocation(), 2);
    }

    @Test
    public void floatIntegerTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("1.230");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.FLOAT);
        assertEquals(token.getValue(), "1.230");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void floatTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e01");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.FLOAT);
        assertEquals(token.getValue(), "12.34e01");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void floatTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e-123");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.FLOAT);
        assertEquals(token.getValue(), "12.34e-123");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void floatTest3(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e+123");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.FLOAT);
        assertEquals(token.getValue(), "12.34e+123");
        assertEquals(token.getLocation(), 1);
    }

    @Test
    public void floatPanicModeTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e+abc123");
        Token token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.FLOAT);
        assertEquals(token.getValue(), "12.34e+0");
        assertEquals(token.getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.getValue(), "abc123");
        assertEquals(token.getLocation(), 8);
    }

}
