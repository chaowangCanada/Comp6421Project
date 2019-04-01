package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SymTabEntry {
    public String name;
    public String kind;
    public String type;
    public int offset;
    public SymTab thisTable;


    public SymTabEntry() {
        new SymTabEntry("name", "kind" ,"type",0 , null);
    }





}
