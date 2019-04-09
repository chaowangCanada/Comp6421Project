package model;

import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.Node;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.NodeType;
import com.concordia.comp6421.compiler.syntacticAnalyzer.treeModel.SymTab;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class LabelGenerator {

    public static LabelGenerator instance = new LabelGenerator();

    private int uniqueLabel;

    private LabelGenerator() {
        this.uniqueLabel = 0;
    }

    public int getUniqueLabel() {
        return ++uniqueLabel;
    }

    public static Map<String, String> function_pointer_table= new HashMap<>();

    public static Map<Pair<String, NodeType>, SymTab> Table_Definition_Map = new HashMap<>();

}