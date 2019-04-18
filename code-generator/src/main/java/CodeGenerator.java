package visitor;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.ComputeMemSizeVisitor;
import com.concordia.comp6421.compiler.syntacticAnalyzer.visitorModel.SymTabCreationVisitor;
import instruction.AlignInstruction;
import instruction.EntryInstruction;
import instruction.MathOptImmInstruction;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import model.CodeGenerateContext;
import model.LabelGenerator;
import model.MathOpt;
import model.Register;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {

    @Setter
    @Getter
    private Node tree;

    public SymTabCreationVisitor symTabCreationVisitor;

    public ComputeMemSizeVisitor computeMemSizeVisitor;

    public ReverseOffsetVisitor reverseOffsetVisitor;

    public StackBasedCodeGenerationVisitor stackBasedCodeGenerationVisitor;

    public CodeGenerateContext context ;


//    public StringBuilder output;

    public CodeGenerator() {
        symTabCreationVisitor = new SymTabCreationVisitor();
        computeMemSizeVisitor = new ComputeMemSizeVisitor();
        reverseOffsetVisitor = new ReverseOffsetVisitor();
        context = new CodeGenerateContext();
        stackBasedCodeGenerationVisitor = new StackBasedCodeGenerationVisitor(this.context);
//        output = new StringBuilder();
    }

    public Node getTree() {
        return  tree;
    }

    public void buildTables(Node incomeTree) {
        tree = incomeTree;
        tree.accept(symTabCreationVisitor);
        tree.accept(computeMemSizeVisitor);
        tree.accept(reverseOffsetVisitor);
        registerTable(tree);
    }

    public void registerTable(Node aNode) {
        if (aNode == null || aNode.getChildren() == null) {
            return ;
        }

        if (Arrays.asList(NodeType.needTableType).contains(aNode.nodeType))
            LabelGenerator.Table_Definition_Map.put(new Pair<>(aNode.symTab.name.split(" ")[0], aNode.nodeType), aNode.symTab);

        for(Node child : aNode.getChildren()) {
            registerTable(child);
        }
    }

    public void generate(){
        tree._accept(stackBasedCodeGenerationVisitor);
    }
}
