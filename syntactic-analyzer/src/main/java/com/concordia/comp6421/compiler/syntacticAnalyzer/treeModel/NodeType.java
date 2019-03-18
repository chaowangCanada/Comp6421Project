package com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel;

public enum  NodeType {
    prog, classList, funcDefList, statBlock, statBlockStart, classDecl, funcDef,
    id, inherList, membList, type, scopeSpec, membDecl,
    varDecl, funcDecl, dimList, fparamList, fparam, intNum, floatNum, statOrVarDecl, stat,
    ifStat, relExpr, assignStat, var, expr, forStat, getStat, putStat, returnStat,
    addOp, arithExpr, term, relOp, multOp, factor, fCall, not, sign,
    varElement, dataMember, indexList, aParams, op, typeBefore;

    public static NodeType[] statTypes = {ifStat, relExpr, forStat, assignStat, getStat, putStat, returnStat};
}
