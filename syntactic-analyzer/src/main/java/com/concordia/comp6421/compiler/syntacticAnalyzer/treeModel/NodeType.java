package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

public enum  NodeType {
    id, intNum, floatNum, prog, relOp, assignOp, op, fParam, fParamList, dimList, type, funcDecl, funcDef,
    indexList, fCall, not, sign, multOp, var, addOp, dataMember, aParams, statBlock, assignStat, classList, funcDefList, classDecl, inherList, membList, scopeSpec, varDecl, ifStat, relExpr, forStat, getStat, putStat, returnStat;

    public static NodeType[] ListPatternNodeType = { statBlock, inherList, membList };

    public static NodeType[] IdListPattenNodeType = {classDecl};

    public static NodeType[] TypeIdListPattenNodeType = {varDecl, funcDecl};

    public static NodeType[] StatTypes = {ifStat, relExpr, forStat, assignStat, getStat, putStat, returnStat};

    public static NodeType[] TypeListIdListPatternNodeType = {funcDef} ;

    public static NodeType[] AtomicTypes = {varDecl, fParam};

    public static NodeType[] needTableType = {prog, classDecl, funcDef, statBlock};


}
