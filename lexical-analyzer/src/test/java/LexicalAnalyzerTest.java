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
    public void identifierTest5(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("integer");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INTEGER);
        assertEquals(token.get().getValue(), "integer");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void identifierINT_NUMTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("1abc");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INT_NUM);
        assertEquals(token.get().getValue(), "1");
        assertEquals(token.get().getLocation(), 1);
        token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc");
        assertEquals(token.get().getLocation(), 2);
    }

    @Test
    public void errorINT_NUMIdentifierTest(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("_1abc");

        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.ERROR);
        assertEquals(token.get().getValue(), "Not supported Lexical Symbol _");
        assertEquals(token.get().getLocation(), 1);

        token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INT_NUM);
        assertEquals(token.get().getValue(), "1");
        assertEquals(token.get().getLocation(), 2);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc");
        assertEquals(token.get().getLocation(), 3);
    }

    @Test
    public void errorIdentifierINT_NUMTest2(){
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
    public void INT_NUMTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12345");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INT_NUM);
        assertEquals(token.get().getValue(), "12345");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void INT_NUMTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("0123");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INT_NUM);
        assertEquals(token.get().getValue(), "0");
        assertEquals(token.get().getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INT_NUM);
        assertEquals(token.get().getValue(), "123");
        assertEquals(token.get().getLocation(), 2);
    }

    @Test
    public void INT_NUMTest3(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12300");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INT_NUM);
        assertEquals(token.get().getValue(), "12300");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void INT_NUMFLOAT_NUMTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("01.23");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INT_NUM);
        assertEquals(token.get().getValue(), "0");
        assertEquals(token.get().getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT_NUM);
        assertEquals(token.get().getValue(), "1.23");
        assertEquals(token.get().getLocation(), 2);
    }

    @Test
    public void INT_NUMFLOAT_NUMTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("01.230");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.INT_NUM);
        assertEquals(token.get().getValue(), "0");
        assertEquals(token.get().getLocation(), 1);

        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT_NUM);
        assertEquals(token.get().getValue(), "1.230");
        assertEquals(token.get().getLocation(), 2);
    }

    @Test
    public void FLOAT_NUMINT_NUMTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("1.230");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT_NUM);
        assertEquals(token.get().getValue(), "1.230");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void FLOAT_NUMTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e01");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT_NUM);
        assertEquals(token.get().getValue(), "12.34e01");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void FLOAT_NUMTest2(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e-123");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT_NUM);
        assertEquals(token.get().getValue(), "12.34e-123");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void FLOAT_NUMTest3(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e+123");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT_NUM);
        assertEquals(token.get().getValue(), "12.34e+123");
        assertEquals(token.get().getLocation(), 1);
    }

    @Test
    public void FLOAT_NUMPanicModeTest1(){
        LexicalAnalyzer analyzer = new LexicalAnalyzer("12.34e+abc123");
        Optional<Token> token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.FLOAT_NUM);
        assertEquals(token.get().getValue(), "12.34e+0");
        assertEquals(token.get().getLocation(), 1);
        if (analyzer.hasNext())
            token = analyzer.nextToken();
        assertEquals(token.get().getTokenType(), TokenType.IDENTIFIER);
        assertEquals(token.get().getValue(), "abc123");
        assertEquals(token.get().getLocation(), 8);
    }

}
