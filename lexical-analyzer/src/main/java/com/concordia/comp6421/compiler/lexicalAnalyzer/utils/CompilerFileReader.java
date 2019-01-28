package com.concordia.comp6421.compiler.lexicalAnalyzer.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class CompilerFileReader {

    private BufferedReader br;

    public CompilerFileReader(String path) {
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all source code from file without '\t', '\r', '\n' and comment
     * @param file
     * @return
     */
    public static List<String> readAllLines(File file){
        List<String> outputList = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            //replace // type of comment
            content = content.replaceAll("//.*?\\r\\n","");
            // replace /*  */ type of comment
            content = content.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
            // replace all line separator by $
            content = content.replaceAll("(\\t|\\r?\\n)+", "\\$");
            // remove all tabs and whitespaces
            content = content.replaceAll("\\s+","")
                                .replaceAll("\t","");
            outputList = new ArrayList<>(Arrays.asList(content.split("\\$")));
        } catch (IOException e) {
            System.err.println("Cannot find content from file: "+ file.toPath());
            e.printStackTrace();
        }
        return outputList;
    }
    
    public String readLine(){
        if(br == null)
            return null;
        String line = null;
        try{
            if((line = br.readLine()) != null){
                line = line.replaceAll("\\s+","")
                        .replaceAll("\t","");
            }
        } catch (IOException e) {
            System.err.println("Cannot find next line");
            e.printStackTrace();
        }
        return line;
    }

    public void close() throws IOException{
        br.close();
    }

}
