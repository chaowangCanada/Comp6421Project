package com.concordia.comp6421.compiler.syntacticAnalyzer.utils;
import lombok.*;

public class StringBuilderRemember
{

    private StringBuilder sb=new StringBuilder();
    @Getter
    private String lastString;
    @Getter
    private char lastChar;
    @Getter
    private int lastInt;
    @Getter
    private float lastFloat;

    public void appendLine(String s)
    {
        lastString=s;
        sb.append(s);
    }

    public StringBuilderRemember append(String s)
    {
        lastString=s;
        sb.append(s);
        return this;
    }

    public StringBuilderRemember append(char cha)
    {
        lastChar=cha;
        sb.append(cha);
        return this;
    }

    public StringBuilderRemember append(int i)
    {
        lastInt=i;
        sb.append(i);
        return this;
    }

    public StringBuilderRemember append(float f)
    {
        lastFloat=f;
        sb.append(f);
        return this;
    }


    public String toString()
    {
        return sb.toString();
    }

    public void setLength(int i)
    {
        sb.setLength(i);
    }
}
