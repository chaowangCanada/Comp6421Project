package com.concordia.comp6421.compiler.syntacticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Alpha;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Grammar;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.NonTerminal;
import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.Symbol;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrammarService {
    BufferedReader br;
    Grammar grammar;

    public GrammarService(File file) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(file));
        try
        {
            process();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void process() throws IOException {
        String line;
        grammar = new Grammar();
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            String a = line.split("->")[0].trim();
            String rhs = line.split("->")[1].trim();
            ((NonTerminal) grammar.getOrAdd(a))
                    .setAlphas(
                            Stream.of(rhs.split("\\|"))
                                    .map(this::parseProd)
                                    .collect(Collectors.toList()));
        }
    }

    private Alpha parseProd(String rhs) {
        List<Symbol> symbols = Stream
                .of(rhs.trim().split("\\s+"))
                .map(s -> grammar.getOrAdd(s))
                .collect(Collectors.toList());
        return Alpha.of(symbols);
    }

    public Grammar getGrammar() {
        return grammar;
    }


}
