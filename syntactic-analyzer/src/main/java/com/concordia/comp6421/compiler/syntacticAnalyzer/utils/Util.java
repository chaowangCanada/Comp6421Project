package com.concordia.comp6421.compiler.syntacticAnalyzer.utils;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;

public class Util {

    public static void printLevelOrder(Node root)
    {
        int h = Util.height(root);
        System.out.println("tree height :"  + h );
        int i;
        for (i=1; i<=h; i++)
        {
            printGivenLevel(root, i);
            System.out.println();
        }
    }

    static void printGivenLevel(Node root, int level)
    {
        if (root == null)
            return;
        if (level == 1) {
            System.out.print(root.getData().toString() + "     ");
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
}
