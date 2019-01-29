package com.concordia.comp6421.compiler.lexicalAnalyzer.application;

import java.io.File;

public class Application {

    public static String option = "";

    public static void main(String[] args) {
        File inFile = null;
        if (0 < args.length) {
            LexicalAnalyzerDriver driver = new LexicalAnalyzerDriver();
            try {
                inFile = new File(args[0]);
                option = args[1];
                driver.run(inFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(0);
        }

    }
}