package com.concordia.comp6421.compiler.syntacticAnalyzer.yuanwen;

import java.util.Objects;

public class TableKey {
    String nt;
    String t;

    TableKey(String nt, String t){
        this.nt = nt;
        this.t = t;
    }

    public static TableKey of(String nt, String t){
        return new TableKey(nt, t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableKey)) return false;
        TableKey tableKey = (TableKey) o;
        return Objects.equals(nt, tableKey.nt) &&
                Objects.equals(t, tableKey.t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nt, t);
    }
}
