package com.concordia.comp6421.compiler.lexicalAnalyzer.application;

import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.Token;
import com.concordia.comp6421.compiler.lexicalAnalyzer.entity.TokenType;
import com.concordia.comp6421.compiler.lexicalAnalyzer.utils.CompilerFileReader;
import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzerDriver {

    public final String error_file = "error.log";
    public final String token_file = "output.log";
    public final String token_AtoCC_file = "output.atocc";
    @Getter
    private List<Token> tokenList;
    @Getter
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
            for (Token token : errorTokenList){
                fileErrorWriter.write(token.toString());
                fileErrorWriter.write(System.lineSeparator());
            }

            if(Application.option.equalsIgnoreCase("AtoCC")){
                fileTokenWriter = new FileWriter(token_AtoCC_file);
                for (Token token : tokenList){
                    fileTokenWriter.write(token.getTokenType().name());
                    fileTokenWriter.write(" ");
                }
            }
            else {
                fileTokenWriter = new FileWriter(token_file);
                for (Token token : tokenList){
                    fileTokenWriter.write(token.toString());
                    fileTokenWriter.write(System.lineSeparator());
                }
            }
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
        for(int lineNum = 1; lineNum <= output.size(); lineNum ++){
            String lineContent = output.get(lineNum-1);
            lexicalAnalyzer.setLineInfo(lineContent, lineNum);
            while(lexicalAnalyzer.hasNext()){
                Token token = lexicalAnalyzer.nextToken();
                if(token != null) {
                    if(token.getTokenType() == TokenType.ERROR)
                        errorTokenList.add(token);
                    else if(token != null)
                        tokenList.add(token);
                }
            }
        }
        writeFiles();
    }
}
