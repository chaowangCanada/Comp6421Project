package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class SymTabEntry {
    public String name;
    public String kind;
    public String type;
    public int offset = 0;
    public int size = 0;
    public SymTab thisTable;
    public List<Integer> dims = new ArrayList<>();


    public SymTabEntry(String name, String kind, String type, SymTab tab) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.thisTable = tab;
    }


    public SymTabEntry(int size, int offset) {
        this.size = size;
        this.offset = offset;

    }
}
