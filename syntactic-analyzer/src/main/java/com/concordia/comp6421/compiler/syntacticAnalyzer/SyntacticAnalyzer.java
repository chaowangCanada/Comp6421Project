package com.concordia.comp6421.compiler.syntacticAnalyzer;

import com.concordia.comp6421.compiler.syntacticAnalyzer.entity.*;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.utils.Util;
import lombok.Getter;

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
    @Getter
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
                        lookahead = lex.nextToken();
                        takeLeafAction(leafToken, lookahead);
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
                takeInternalAction((Action)symbol, leafToken.getValue());
            } else {
                stack.pop();
            }
        }

        tree = nodeStack.pop();

        Util.printLevelOrder(tree);

        if( lookahead.get().getTokenType().toString().equalsIgnoreCase(DOLLAR.symbol) ||
                error)
            return false;
        else
            return true;


    }

    private void takeInternalAction(Action actionSymbol, String value) {
        switch (actionSymbol.nodeType) {
            case prog:
                Node prog = Node.makeNode(NodeType.prog, "prog");
                prog.adoptChildren(new ArrayList<>(nodeStack));
                nodeStack.push(prog);
                break;
            case classList:
                makeNodeFromListSubtrees(NodeType.classList, "classList", NodeType.classDecl);
                break;
            case funcDefList:
                makeNodeFromListSubtrees(NodeType.funcDefList, "funcDefList", NodeType.funcDef);
                break;
            case classDecl:
                makeNodeFromTypeIdSubtree(NodeType.classDecl, "classDecl", NodeType.id, NodeType.inherList, NodeType.membList);
                break;
            case inherList:
                makeNodeFromIdSubtrees(NodeType.inherList, "inherList", NodeType.id);
                break;
            case membList:
                makeNodeFromListSubtrees(NodeType.membList, "memberList", NodeType.varDecl, NodeType.funcDecl);
                break;
            case scopeSpec:
                if (nodeStack.peek().nodeType == NodeType.id)
                    nodeStack.push(Node.makeNode(NodeType.scopeSpec, nodeStack.pop().data.toString(), true));
                break;
            case funcDef:
                makeNodeFromTypeIdSubtree(NodeType.funcDef, "funcDef",
                        NodeType.type, NodeType.scopeSpec, NodeType.id, NodeType.fParamList, NodeType.statBlock);
                break;
            case varDecl:
                makeNodeFromTypeIdSubtree(NodeType.varDecl, "varDecl", NodeType.type, NodeType.id, NodeType.dimList);
                break;
            case assignOp:
                nodeStack.push(Node.makeNode(NodeType.assignOp, value));
                break;
            case relOp:
                nodeStack.push(Node.makeNode(NodeType.relOp, value));
                break;
            case relExpr:
                Node p22 = nodeStack.pop();
                Node relExpr = nodeStack.pop();
                Node p11 = nodeStack.pop();
                nodeStack.push(Node.makeFamiliy(NodeType.relExpr, relExpr.data.toString(), p11, p22));
                break;
            case addOp:
                Node p2 = nodeStack.pop();
                Node op = nodeStack.pop();
                Node p1 = nodeStack.pop();
                nodeStack.push(Node.makeFamiliy(NodeType.addOp, op.data.toString(), p1, p2));
                break;
            case multOp:
                Node pRight = nodeStack.pop();
                Node multiOp = nodeStack.pop();
                Node pLeft = nodeStack.pop();
                nodeStack.push(Node.makeFamiliy(NodeType.multOp, multiOp.data.toString(), pLeft, pRight));
                break;
            case assignStat:
                Node pSec = nodeStack.pop();
                Node addOp = nodeStack.pop();
                Node pFst = nodeStack.pop();
                nodeStack.push(Node.makeFamiliy(NodeType.assignStat, addOp.data.toString(), pFst, pSec));
                break;
            case fParam:
                makeNodeFromTypeIdSubtree(NodeType.fParam, "fparam", NodeType.type, NodeType.id, NodeType.dimList);
                break;
            case funcDecl:
                makeNodeFromTypeIdSubtree(NodeType.funcDecl, "funcDecl", NodeType.type, NodeType.id, NodeType.fParamList);
                break;
            case dimList:
                makeNodeFromListSubtrees(NodeType.dimList, "dimList", NodeType.floatNum, NodeType.intNum);
                break;
            case fParamList:
                makeNodeFromListSubtrees(NodeType.fParamList, "fParamList", NodeType.fParam);
                break;
            case indexList:
                makeNodeFromListSubtrees(NodeType.indexList, "indexList", NodeType.addOp, NodeType.multOp, NodeType.var, NodeType.floatNum,
                        NodeType.intNum);
                break;
            case dataMember:
                makeNodeFromTypeIdSubtree(NodeType.dataMember,"dataMember", NodeType.id, NodeType.indexList);
                break;
//            case membList:
//                makeNodeFromListSubtrees(NodeType.membList, "memblist", NodeType.dataMember);
//                break;
            case fCall:
                makeNodeFromTypeIdSubtree(NodeType.fCall, "fcall",NodeType.id, NodeType.aParams);
                break;
            case aParams:
                makeNodeFromTypeIdSubtree(NodeType.aParams,"aParams", NodeType.var, NodeType.addOp, NodeType.multOp, NodeType.floatNum,
                        NodeType.intNum, NodeType.fCall, NodeType.not, NodeType.sign, NodeType.relExpr);
                break;
            case var:
                makeNodeFromListSubtrees(NodeType.var,"var", NodeType.dataMember);
                break;
            case statBlockStart:
                nodeStack.push(Node.makeNode(NodeType.statBlockStart, "startBlockStart"));
                break;
            case statBlock:
                NodeType[] childTypes = Arrays.copyOf(NodeType.StatTypes, NodeType.StatTypes.length + 3);
                childTypes[NodeType.StatTypes.length] = NodeType.varDecl;
//                childTypes[NodeType.StatTypes.length + 1] = NodeType.fParamList;
                makeNodeFromListSubtrees(NodeType.statBlock, "statBlock", childTypes);
                break;
            case ifStat:
                makeNodeFromListSubtrees(NodeType.ifStat, "ifStat", NodeType.relExpr,
                        NodeType.statBlock, NodeType.statBlock);
                break;
            case forStat:
                makeNodeFromListSubtrees(NodeType.forStat, "forStat", NodeType.type, NodeType.assignStat, NodeType.relExpr,
                        NodeType.assignStat, NodeType.statBlockStart, NodeType.statBlock);
                break;
            case getStat:
                nodeStack.push(Node.makeNode(NodeType.getStat, "getStat").adoptChildren(nodeStack.pop()));
                break;
            case putStat:
                nodeStack.push(Node.makeNode(NodeType.putStat, "putStat").adoptChildren(nodeStack.pop()));
                break;
            case returnStat:
                nodeStack.push(Node.makeNode(NodeType.returnStat, "returnStat").adoptChildren(nodeStack.pop()));
                break;
            case not:
                nodeStack.push(Node.makeNode(NodeType.not, "not").adoptChildren(nodeStack.pop()));
                break;
            case sign:
                Node p = nodeStack.pop();
                Node sign = nodeStack.pop();
                nodeStack.push(Node.makeFamily(NodeType.sign, sign.data.toString(), p));
                break;
            case expr:  //not done
                if (nodeStack.peek().nodeType == NodeType.relExpr || nodeStack.peek().nodeType == NodeType.arithExpr) {
                    nodeStack.push(Node.makeNode(NodeType.expr, "expr").adoptChildren(nodeStack.pop()));
                }
                break;
            case term:
            case factor:
            case arithExpr:
            default:
                break;
        }
    }

    private void makeNodeFromIdSubtrees(NodeType rootNodetype, String rootName, NodeType childNodetype) {
        Node inherList = Node.makeNode(rootNodetype, rootName);
        while (!nodeStack.isEmpty() && nodeStack.peek().nodeType == childNodetype) {
            Node tmp = nodeStack.pop();
            if (nodeStack.isEmpty() || nodeStack.peek().nodeType == childNodetype) {
                nodeStack.push(tmp);
                break;
            }
            inherList.adoptChildren(tmp);
        }
        if (inherList.leftMostChild != null) {
            nodeStack.push(inherList);
        }
    }

    private void takeLeafAction(Token tokenBST, Optional<Token> lookahead) {
        switch (tokenBST.getTokenType()) {
            case INTEGER:
                nodeStack.push(Node.makeNode(NodeType.type, tokenBST.getValue(), true));
                break;
            case FLOAT:
                nodeStack.push(Node.makeNode(NodeType.type, tokenBST.getValue(), true));
                break;
            case INT_NUM:
                nodeStack.push(Node.makeNode(NodeType.intNum, tokenBST.getValue(), true));
                break;
            case FLOAT_NUM:
                nodeStack.push(Node.makeNode(NodeType.floatNum, tokenBST.getValue(), true));
                break;
            case IDENTIFIER:
                if(lookahead.isPresent() && lookahead.get().getTokenType() == TokenType.IDENTIFIER)
                    nodeStack.push(Node.makeNode(NodeType.type, tokenBST.getValue(), true));
                else
                    nodeStack.push(Node.makeNode(NodeType.id, tokenBST.getValue(), true));
                break;
            case EQ:
                nodeStack.push(Node.makeNode(NodeType.assignOp, tokenBST.getValue(), true));
                break;
        }
    }

    private void makeNodeFromTypeIdSubtree(NodeType rootNodeType, String rootNodeVal, NodeType... childrenTypes) {
        Node parent;

        if(Arrays.asList(NodeType.AtomicTypes).contains(rootNodeType))
            parent = Node.makeNode(rootNodeType, rootNodeVal, true);
        else
            parent = Node.makeNode(rootNodeType, rootNodeVal);

        List<Node> children = new ArrayList<>();
        Stack<NodeType> nodeTypeStack = new Stack<>();
        nodeTypeStack.addAll(Arrays.asList(childrenTypes));
        if(nodeTypeStack.peek() != nodeStack.peek().nodeType)
            nodeTypeStack.pop();
        while(!nodeTypeStack.isEmpty() && !nodeStack.empty()) {
            if( nodeTypeStack.peek() == nodeStack.peek().nodeType)
                children.add(0, nodeStack.pop());
            nodeTypeStack.pop();
        }
        children.forEach(parent::adoptChildren);
        if(parent.leftMostChild != null)
            nodeStack.push(parent);
    }

    public void makeNodeFromListSubtrees(NodeType rootNodeType, String rootNodeVal, NodeType... childNodeTypes) {
        Node parent;

        if(Arrays.asList(NodeType.AtomicTypes).contains(rootNodeType))
            parent = Node.makeNode(rootNodeType, rootNodeVal, true);
        else
            parent = Node.makeNode(rootNodeType, rootNodeVal);

        List<Node> children = new ArrayList<>();
        while(!nodeStack.isEmpty() && ( Arrays.asList(childNodeTypes).contains(nodeStack.peek().nodeType)
                                       || (nodeStack.peek().nodeType == rootNodeType && nodeStack.peek().nodeType != NodeType.statBlock))) {
            if(rootNodeType == NodeType.forStat && nodeStack.peek().nodeType == childNodeTypes[0]){
                children.add(0, nodeStack.pop());
                break;
            }
            else if(nodeStack.peek().nodeType == NodeType.statBlockStart)
                nodeStack.pop();
            else
                children.add(0, nodeStack.pop());
        }

        for (Node node : children) {
            if(node.nodeType == rootNodeType)
                parent.adoptChildren(node.leftMostChild);
            else
                parent.adoptChildren(node);
        }

        if(parent.leftMostChild != null)
            nodeStack.push(parent);
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

        if(!rhsSymbols.stream().filter(symbol -> symbol instanceof Action).collect(Collectors.toSet()).isEmpty())
            System.out.println("$$action: " +
                    rhsSymbols.stream().filter(symbol -> symbol instanceof Action)
                            .map(Symbol::toString)
                            .collect(Collectors.joining(" ")));

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
