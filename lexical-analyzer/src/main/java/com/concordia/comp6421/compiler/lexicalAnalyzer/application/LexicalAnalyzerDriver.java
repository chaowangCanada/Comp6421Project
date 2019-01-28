package com.concordia.comp6421.compiler.lexicalAnalyzer.application;

import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.TokenType;
import com.concordia.comp6421.compiler.lexicalAnalyzer.utils.CompilerFileReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzerDriver {

    public final String error_file = "error.log";
    public final String token_file = "output.log";
    private List<Token> tokenList;
    private List<Token> errorTokenList;
    private LexicalAnalyzer lexicalAnalyzer;

    public LexicalAnalyzerDriver(){
        tokenList = new ArrayList<>();
        errorTokenList = new ArrayList<>();
        lexicalAnalyzer = new LexicalAnalyzer("");
    }

    private void writeFiles(){
        FileWriter fileErrorWriter = null;
        FileWriter fileTokenWriter = null;
        try {
            fileErrorWriter = new FileWriter(error_file);
            for (Token token : errorTokenList)
                fileErrorWriter.write(token.toString());

            fileTokenWriter = new FileWriter(token_file);
            for (Token token : tokenList)
                fileTokenWriter.write(token.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fileErrorWriter != null) {
                    fileErrorWriter.flush();
                    fileErrorWriter.close();
                }
                if (fileTokenWriter != null) {
                    fileTokenWriter.flush();
                    fileTokenWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run(File file) throws java.lang.Exception {
        List<String> output = CompilerFileReader.readAllLines(file);
        for(String lineContent : output){
            lexicalAnalyzer.setLineContent(lineContent);
            while(lexicalAnalyzer.hasNext()){
                Token token = lexicalAnalyzer.nextToken();
                if(token.getTokenType() == TokenType.ERROR)
                    errorTokenList.add(token);
                else
                    tokenList.add(token);
            }
        }
        writeFiles();
    }
}
