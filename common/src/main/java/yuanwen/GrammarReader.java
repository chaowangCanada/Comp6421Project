package yuanwen;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrammarReader {
    private BufferedReader br;
    private Grammar grammar;

    public GrammarReader(File f) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(f));
    }

    public void process() throws IOException {
        String line;
        grammar = new Grammar();
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()){
                continue;
            }
            String a = line.split("->")[0].trim();
            String rhs = line.split("->")[1].trim();
            ((NonTerminal) grammar.getOrElseAdd(a))
                    .setAlphas(
                            Stream.of(rhs.split("\\|"))
                                    .map(this::parseProd)
                                    .collect(Collectors.toList()));
        }
    }

    private Alpha parseProd(String rhs) {
        List<Symbol> symbols = Stream.of(rhs.trim().split("\\s+"))
                .map(s -> grammar.getOrElseAdd(s))
                .collect(Collectors.toList());
        return Alpha.of(symbols);
    }

    public Grammar getGrammar() {
        return grammar;
    }
}
