package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

public enum  NodeType {
    id, intNum, floatNum, prog, relOp, assignOp, op, fParam, fParamList, dimList, type, funcDecl, funcDef,
    indexList, fCall, not, sign, multOp, var, addOp, dataMember, aParams, statBlock, assignStat, classList, funcDefList, classDecl, inherList, membList, scopeSpec, varDecl, ifStat, relExpr, forStat, getStat, putStat, returnStat;

    public static NodeType[] listPatternNodeType = {classList, funcDefList, statBlock };

    public static NodeType[] idListPattenNodeType = {classDecl};

    public static NodeType[] typeIdListPattenNodeType = {varDecl};

    public static NodeType[] statTypes = {ifStat, relExpr, forStat, assignStat, getStat, putStat, returnStat};

    public static NodeType[] atomicTypes = {varDecl};

}
