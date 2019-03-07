package test;

import com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen.SyntacticAnalyzerDriver;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SyntacticAnalyzerTest {
    private static final String FOLDER = "syntactic";

    @Test
    public void testAnalyzer() throws IOException {
        String fileName = FOLDER + File.separator +  "test_program.txt";
        boolean sucess = SyntacticAnalyzerDriver.parseFile(fileName);
        Assert.assertTrue(sucess);
    }

    @Test
    public void testErrorRecover() throws IOException {
        String fileName = FOLDER + File.separator +  "error_test.txt";
        boolean sucess = SyntacticAnalyzerDriver.parseFile(fileName);
    }
}
