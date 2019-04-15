package visitor;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTabEntry;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.Visitor;

import java.util.Arrays;

public class ReverseOffsetVisitor extends Visitor{
    @Override
    public void visit(Node p_node) {
        if (Arrays.asList(NodeType.needTableType).contains(p_node.nodeType)){
            for (SymTabEntry entry : p_node.symTab.symList){
                entry.offset = p_node.symTab.size - entry.offset;
            }
        }
    }
}
