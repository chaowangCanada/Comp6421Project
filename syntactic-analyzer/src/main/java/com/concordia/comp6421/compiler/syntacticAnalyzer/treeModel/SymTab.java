package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

import java.util.ArrayList;

public class SymTab {

    public String name;
    public ArrayList<SymTabEntry> symList;
    public int size;
    public int currentOffset;

    public SymTab(String name){
        this.name = name;
        symList = new ArrayList<SymTabEntry>();
        this.currentOffset = 0;
    }

    public void addEntry(SymTabEntry entry){
        if(entry != null ) {
            symList.add(entry);
            size = symList.size();
        }
    }

}
