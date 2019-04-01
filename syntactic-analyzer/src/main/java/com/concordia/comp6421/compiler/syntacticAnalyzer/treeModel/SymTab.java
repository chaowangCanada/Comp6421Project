package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

import java.util.ArrayList;

public class SymTab {

    public String name;
    public ArrayList<SymTabEntry> symList;
    public int size;
    public SymTab upperTable;

    public SymTab(String name, SymTab uppertable){
        this.name = name;
        symList = new ArrayList<SymTabEntry>();
        this.upperTable = uppertable;
    }

    public void addEntry(SymTabEntry entry){
        if(entry != null ) {
            symList.add(entry);
            size = symList.size();
        }
    }

    public SymTabEntry lookupName(String tolookup) {
        SymTabEntry returnvalue = new SymTabEntry();
        boolean found = false;
        for( SymTabEntry rec : symList) {
            System.out.println(rec.name);
            if (rec.name.equals(tolookup)) {
                returnvalue = rec;
                found = true;
            }
        }
        if (!found) {
            if (upperTable != null) {
                returnvalue = upperTable.lookupName(tolookup);
            }
        }
        return returnvalue;
    }

}
