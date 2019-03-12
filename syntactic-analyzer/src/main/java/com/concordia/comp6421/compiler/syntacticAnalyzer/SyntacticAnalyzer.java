package com.concordia.comp6421.compiler.syntacticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.DOLLAR;
import static com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Default.EPSILON;

public class SyntacticAnalyzer {

    Grammar grammar;
    private Stack<Symbol> stack;
    private List<String> derivation;
    private LexicalAnalyzer lex;

    public SyntacticAnalyzer(Grammar grammar) {
        this.grammar = grammar;
        this.stack = new Stack<>();
        this.derivation = new ArrayList<>();
        grammar.buildFirst();
        grammar.buildFollow();
        grammar.buildTable();
    }

    public boolean parse(LexicalAnalyzer input) throws IOException {
        boolean error = false;
        this.lex = input;
        stack.push(DOLLAR);
        stack.push(grammar.getStart());
        derivation.add(grammar.getStart().symbol);
        printDerivation();
        Optional<Token> lookahead = lex.nextToken();
        while (stack.peek() != DOLLAR && lookahead.isPresent()
                && !lookahead.get().getValue().equalsIgnoreCase("Not supported Lexical Symbol \uFFFF")) {
            Token token = lookahead.get();
            Symbol x = stack.peek();
            if (x.isEpsilon()) {
                stack.pop();
            } else if (x.isTerminal()) {
                if (x.symbol.equals(token.getTokenType().toString())) {
                    stack.pop();
                    updateDerivation(x.symbol, Collections.singletonList(token.getValue()));
                    lookahead = lex.nextToken();
                } else {
                    lookahead = skipErrors(token);
                    error = true;
                }
            } else {
                Rule rule;
                if ((rule = grammar.getRule(x.symbol, token)) != null) {
                    stack.pop();
                    inverseRHSMultiplePush(rule);
                    updateDerivation(rule.lhs.symbol,
                            rule.rhs.symbolSeq.stream().map(Symbol::toString).collect(Collectors.toList()));
                } else {
                    lookahead = skipErrors(token);
                    error = true;
                }
            }
        }
        reset();
        return !lookahead.isPresent() && stack.size() == 1 && !error;
    }

    private void updateDerivation(String lhs, List<String> rhs) {
        int idx = 0;
        for (int i = 0; i < derivation.size(); i++) {
            if (derivation.get(i).equals(lhs)) {
                idx = i;
                break;
            }
        }
        derivation.remove(idx);
        derivation.addAll(idx, rhs.stream().filter(s -> !s.equals(EPSILON.symbol)).collect(Collectors.toList()));
        printDerivation();
    }

    private void printDerivation() {
        derivation.forEach(s -> System.out.print(s + " "));
        System.out.println();
    }

    private void inverseRHSMultiplePush(Rule rule) {
        List<Symbol> copy = new ArrayList<>(rule.rhs.symbolSeq);
        Collections.reverse(copy);
        copy.forEach(s -> stack.push(s));
    }

    private Optional<Token> skipErrors(Token token) throws IOException {
        System.out.println("syntax error at " + token.getLocation());
        NonTerminal top = topNonterminal();
        Optional<Token> lookahead = Optional.of(token);
        if (top == null) {
            return lookahead;
        }
        Set<Symbol> first = top.getFirst();
        Set<Symbol> follow = top.getFollow();
        if (containsToken(follow, lookahead.get())) {
            stack.pop();
        } else {
            while (!containsToken(first, lookahead.get()) ||
                    (first.contains(EPSILON) && !containsToken(follow, lookahead.get()))) {
                lookahead = lex.nextToken();
                if (!lookahead.isPresent()) {
                    break;
                }
            }
        }
        return lookahead;
    }

    private void reset() {
        lex = null;
    }

    private NonTerminal topNonterminal() {
        while (!stack.peek().isNonTerminal() && stack.peek() != DOLLAR) {
            stack.pop();
        }
        if (stack.peek() == DOLLAR) {
            return null;
        } else {
            return (NonTerminal) stack.peek();
        }
    }

    private boolean containsToken(Set<Symbol> symbols, Token token) {
        return symbols.stream().anyMatch(s -> s.matchToken(token));
    }
}
