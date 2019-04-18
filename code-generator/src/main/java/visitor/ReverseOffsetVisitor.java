package visitor;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTabEntry;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.Visitor;

import java.util.Arrays;

public class ReverseOffsetVisitor extends Visitor{
    @Override
    public void visit(Node p_node) {
        if (p_node.nodeType == NodeType.prog ||  p_node.nodeType == NodeType.classDecl ||
                (p_node.nodeType == NodeType.statBlock && p_node.parent.nodeType == NodeType.prog)){
            for (SymTabEntry entry : p_node.symTab.symList){
                entry.offset = p_node.symTab.size - entry.offset;
            }
        }
        else if(p_node.nodeType == NodeType.funcDef){
            for (SymTabEntry entry : p_node.symTab.symList){
                if(!entry.kind.equalsIgnoreCase("fParam"))
                    entry.offset = p_node.symTab.size - entry.offset;
            }
        }
    }
}
