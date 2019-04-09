package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

public enum  NodeType {
    typeerror,
    id, intNum, floatNum, prog, relOp, assignOp, op, fParam, fParamList, dimList, type, funcDecl, funcDef,
    indexList, fCall, not, sign, multOp, var, addOp, dataMember, aParams, statBlock, assignStat, classList, funcDefList, classDecl, inherList, membList, scopeSpec, varDecl, ifStat, relExpr, forStat, getStat, putStat, returnStat, statBlockStart, arithExpr, factor, term, expr;

    public static NodeType[] ListPatternNodeType = { statBlock, inherList, membList, fParamList };

    public static NodeType[] IdListPattenNodeType = {classDecl};

    public static NodeType[] TypeIdListPattenNodeType = {varDecl, funcDecl, fParam};

    public static NodeType[] StatTypes = {ifStat, forStat, assignStat, getStat, putStat, returnStat};

    public static NodeType[] TypeListIdListPatternNodeType = {funcDef} ;

    public static NodeType[] AtomicTypes = {varDecl, fParam, scopeSpec};

    public static NodeType[] needTableType = {prog, classDecl, funcDef, statBlock};

    public static NodeType[] binaryOpType = {addOp, multOp};
}
