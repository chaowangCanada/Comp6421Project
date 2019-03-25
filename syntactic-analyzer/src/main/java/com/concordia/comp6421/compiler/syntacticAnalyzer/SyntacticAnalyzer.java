package com.concordia.comp6421.compiler.syntacticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.*;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;

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
    private Stack<Node> nodeStack;
    private Node tree;


    public SyntacticAnalyzer(Grammar grammar) {
        this.grammar = grammar;
        this.stack = new Stack<>();
        nodeStack = new Stack<>();
        this.derivation = new ArrayList<>();
        grammar.buildFirst();
        grammar.buildFollow();
        grammar.buildTable();
        grammar.writeFirstFollowSets();
    }

    public boolean parse(LexicalAnalyzer input) throws IOException {
        boolean error = false;
        this.lex = input;
        stack.push(DOLLAR);
        stack.push(grammar.getStart());
        derivation.add(grammar.getStart().symbol);
        printDerivation();
        Optional<Token> lookahead = lex.nextToken();
        Token leafToken = lookahead.get();
        while (stack.peek() != DOLLAR && lookahead.isPresent()
                && !lookahead.get().getValue().equalsIgnoreCase("Not supported Lexical Symbol \uFFFF")) {
            Token token = lookahead.get();
            Symbol symbol = stack.peek();
            if (symbol instanceof Terminal || symbol instanceof Epsilon) {
                stack.pop();
                if(symbol instanceof Terminal) {
                    if (symbol.symbol.equals(token.getTokenType().toString())) {
                        updateDerivation(symbol.symbol, token.getValue());
                        leafToken = token;
                        takeTerminalAction(leafToken);
                        lookahead = lex.nextToken();
                    } else {
                        lookahead = skipErrors(token);
                        error = true;
                    }
                }
            } else if (symbol instanceof NonTerminal){
                Rule rule = grammar.getRule(symbol.symbol, token);
                if(rule != null) {
                    stack.pop();
                    inverseRHSMultiplePush(rule);
                    updateDerivation(((NonTerminal)symbol).symbol, rule.rhs.symbolSeq);
                }
                else {
                    lookahead = skipErrors(token);
                    error = true;
                }
            } else if (symbol instanceof Action) {
                stack.pop();
                takeNonTerminalAction((Action)symbol, leafToken.getValue());
            } else {
                stack.pop();
            }
        }
        if( lookahead.get().getTokenType().toString().equalsIgnoreCase(DOLLAR.symbol) ||
                error)
            return false;
        else
            return true;
    }

    private void takeNonTerminalAction(Token tokenBST) {
        switch (tokenBST.getValue()) {
            case "prog":
                Node prog = Node.makeNode(NodeType.prog, "prog");
                prog.adoptChildren(new ArrayList<>(nodeStack));
                nodeStack.push(prog);
                break;
            case "=":
                break;

        }
    }

    private void takeTerminalAction(Token tokenBST) {
        switch (tokenBST.getTokenType()) {
            case INTEGER:
                nodeStack.push(Node.makeNode(NodeType.integer_, tokenBST.getValue()));
                break;
            case FLOAT:
                nodeStack.push(Node.makeNode(NodeType.float_, tokenBST.getValue()));
                break;
            case INT_NUM:
                nodeStack.push(Node.makeNode(NodeType.intNum, tokenBST.getValue()));
                break;
            case FLOAT_NUM:
                nodeStack.push(Node.makeNode(NodeType.floatNum, tokenBST.getValue()));
                break;
            case IDENTIFIER:
                nodeStack.push(Node.makeNode(NodeType.id, tokenBST.getValue()));
                break;
            case EQ:
                nodeStack.push(Node.makeNode(NodeType.op, tokenBST.getValue()));
        }
    }

    private void updateDerivation(String symbolStr, List<Symbol> symbolSeq) {
        int index = derivation.indexOf(symbolStr);
        if(index >= 0 ) {
            derivation.remove(index);
            List<String> symbolSeqStr = symbolSeq.stream().
                    filter(symbol -> !symbol.symbol.equalsIgnoreCase(EPSILON.symbol) &&
                           !(symbol instanceof  Action)).
                    map(Symbol::toString).
                    collect(Collectors.toList());
            derivation.addAll(index, symbolSeqStr);
        }
        printDerivation();
    }

    private void updateDerivation(String symbolStr, String replacement) {
        int index = derivation.indexOf(symbolStr);
        if(index >= 0 ) {
            derivation.remove(symbolStr);
            derivation.add(index, replacement);
        }

        if(!lex.hasNext()) {
            derivation.subList(index+1, derivation.size()).clear();
        }
        printDerivation();
    }

    private void printDerivation() {
        System.out.println(derivation.stream().map(String::toString).collect(Collectors.joining(" ")));
    }


    private void inverseRHSMultiplePush(Rule rule) {
        List<Symbol> rhsSymbols = new ArrayList<>(rule.rhs.symbolSeq);
        Collections.reverse(rhsSymbols);
        for (Symbol symbol : rhsSymbols)
            stack.push(symbol);
    }

    private Optional<Token> skipErrors(Token token) throws IOException {
        System.out.println("syntax error at " + token.getLocation() + " token value : " + token.getValue());
        NonTerminal top;
        while (!(stack.peek() instanceof NonTerminal) && stack.peek() != DOLLAR) {
            stack.pop();
        }

        if (stack.peek() == DOLLAR) {
            top =  null;
        } else {
            top = (NonTerminal) stack.peek();
        }

        Optional<Token> lookahead = Optional.of(token);
        if (top == null) {
            return lookahead;
        }

        Set<Symbol> first = top.getFirst();
        Set<Symbol> follow = top.getFollow();
        if (containsToken(follow, lookahead.get())) {
            stack.pop();
            return lookahead;
        } else {
            while (!containsToken(first, lookahead.get()) ||
                    (first.contains(EPSILON) && !containsToken(follow, lookahead.get()))) {
                lookahead = lex.nextToken();
                if (!lookahead.isPresent()) {
                    continue;
                }
            }
        }
        return lookahead;
    }

    private boolean containsToken(Set<Symbol> symbols, Token token) {
        for (Symbol symbol :symbols) {
            if(symbol.matchToken(token))
                return true;
        }
        return false;
    }
}
