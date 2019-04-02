package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

import java.util.ArrayList;

public class SymTab {

    public String name;
    public ArrayList<SymTabEntry> symList;
    public int size;
//    public SymTab upperTable;

    public SymTab(String name){
        this.name = name;
        symList = new ArrayList<SymTabEntry>();
//        this.upperTable = uppertable;
    }

    public void addEntry(SymTabEntry entry){
        if(entry != null ) {
            symList.add(entry);
            size = symList.size();
        }
    }

}
