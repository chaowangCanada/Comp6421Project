import com.concordia.comp6421.compiler.syntacticAnalyzer.LexicalAnalyzer;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.TokenType;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class LexicalAnalyzerTest {


    @Test
    public void identifierTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("abc");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc");
        assertEquals(token.get().getLocation(), 1);

    }

    @Test
    public void identifierTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("abc1");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc1");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void identifierTest3(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("abc_1");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc_1");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void identifierTest4(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("abc1_");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc1_");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void identifierIntegerTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("1abc");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "1");
        assertEquals(token.get().getLocation(), 1);
        token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc");
        assertEquals(token.get().getLocation(), 2);
    }

    @Test
    public void errorIntegerIdentifierTest(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("_1abc");

        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.ERROR);
        assertEquals(token.get().getValue(), "Not supported Lexical Symbol _");
        assertEquals(token.get().getLocation(), 1);

        token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "1");
        assertEquals(token.get().getLocation(), 2);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc");
        assertEquals(token.get().getLocation(), 3);
    }

    @Test
    public void errorIdentifierIntegerTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("_abc1");

        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.ERROR);
        assertEquals(token.get().getValue(), "Not supported Lexical Symbol _");
        assertEquals(token.get().getLocation(), 1);

        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc1");
        assertEquals(token.get().getLocation(), 2);

    }

    @Test
    public void integerTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12345");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "12345");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void integerTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("0123");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "0");
        assertEquals(token.get().getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "123");
        assertEquals(token.get().getLocation(), 2);
    }

    @Test
    public void integerTest3(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12300");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "12300");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void integerFloatTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("01.23");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "0");
        assertEquals(token.get().getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT);
        assertEquals(token.get().getValue(), "1.23");
        assertEquals(token.get().getLocation(), 2);
    }

    @Test
    public void integerFloatTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("01.230");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "0");
        assertEquals(token.get().getLocation(), 1);

        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT);
        assertEquals(token.get().getValue(), "1.230");
        assertEquals(token.get().getLocation(), 2);
    }

    @Test
    public void floatIntegerTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("1.230");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT);
        assertEquals(token.get().getValue(), "1.230");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void floatTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e01");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT);
        assertEquals(token.get().getValue(), "12.34e01");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void floatTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e-123");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT);
        assertEquals(token.get().getValue(), "12.34e-123");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void floatTest3(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e+123");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT);
        assertEquals(token.get().getValue(), "12.34e+123");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void floatPanicModeTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e+abc123");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT);
        assertEquals(token.get().getValue(), "12.34e+0");
        assertEquals(token.get().getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc123");
        assertEquals(token.get().getLocation(), 8);
    }

}
