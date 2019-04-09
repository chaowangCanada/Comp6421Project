package com.concordia.comp6421.compiler.syntacticAnalyzer.utils;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class Util {

    public static StringBuilder stringBuilder = new StringBuilder();
    public static StringBuilder tableBuilder = new StringBuilder();


    public static void printSymbolTable(Node aNode) {
        if (aNode == null || aNode.getChildren() == null) {
            return ;
        }

        if (Arrays.asList(NodeType.needTableType).contains(aNode.nodeType))
            tableBuilder.append(aNode.printTable());

        for(Node child : aNode.getChildren()) {
            printSymbolTable(child);
        }
    }

    public static void printLevelOrder(Node root)
    {
        stringBuilder = new StringBuilder();
        int h = Util.height(root);
        stringBuilder.append("tree height :"  + h ).append(System.lineSeparator());
        System.out.println("tree height :"  + h );
        int i;
        for (i=1; i<=h; i++)
        {
            printGivenLevel(root, i);
            System.out.println();
            stringBuilder.append(System.lineSeparator());
        }
    }

    static void printGivenLevel(Node root, int level)
    {
        if (root == null)
            return;
        if (level == 1) {
            stringBuilder.append(root.data.toString() + "     ");
            System.out.print(root.data.toString() + "     ");
        }
        else if (level > 1)
        {
            if(root.leftMostChild != null )
                root = root.leftMostChild;
            else if(root.rightSib != null )
                root = root.rightSib;
            printGivenLevel(root, level-1);
            while (root.rightSib != null ) {
                root = root.rightSib;
                printGivenLevel(root, level-1);
            }
        }
    }

    static int height(Node aNode) {
        if (aNode == null) {
            return -1;
        }
        int level = 1;

        while (aNode.leftMostChild != null) {
            aNode = aNode.leftMostChild;

            while (aNode.rightSib != null )
                aNode = aNode.rightSib;
            level++;
        }
        return level;
    }

    public static void log(String fileName, Collection<String> lines) {
        File f = new File(fileName);
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(f,true))) {
            for (String l : lines){
                wr.write(l + "\n");
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void log(String fileName, Collection<String> lines, String separator, boolean isContinue) {
        File f = new File(fileName);
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(f,isContinue))) {
            for (String l : lines){
                wr.write(l + separator);
            }
            wr.write("\n");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void log(String fileName, StringBuilder stringBuilder) {
        File f = new File(fileName);
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(f))) {
                wr.write(stringBuilder.toString());
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
