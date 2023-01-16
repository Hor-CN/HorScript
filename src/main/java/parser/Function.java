package parser;


import cn.hutool.core.collection.CollUtil;
import domain.ListModel;
import domain.ValueModel;
import domain.VariableModel;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.List;

public class Function {

    private final Scope parentScope;
    private final List<TerminalNode> params;
    private final ParseTree block;

    private String Identifier = null;


    Function(Scope parentScope, List<TerminalNode> params, ParseTree block) {
        this.parentScope = parentScope;
        this.params = params;
        this.block = block;
    }

    public List<TerminalNode> getParams() {
        return params;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public ParseTree getBlock() {
        return block;
    }

    public ValueModel invoke(List<ValueModel> args) {
        Scope scopeNext = new Scope(parentScope, true);
        ListModel listModel = new ListModel();
        for (ValueModel value : args) {
            listModel.add(value);
        }

        for (int i = 0; i < this.params.size(); i++) {
            ValueModel value;
            if (i < args.size()) value = args.get(i);
            else value = ValueModel.NULL;
            scopeNext.assignParam(new VariableModel(this.params.get(i).getText()), value);
        }
        scopeNext.localAssign(new VariableModel("参数"),new ValueModel(listModel));
        HorScriptVisitor evalVisitorNext = new HorScriptVisitor(scopeNext);
        ValueModel ret = ValueModel.VOID;
        try {
            evalVisitorNext.visit(this.block);
        } catch (ReturnValue returnValue) {
            ret = returnValue.value;
        }
        return ret;
    }

    @Override
    public String toString() {
        if (this.Identifier != null) {
            return "函数" + Identifier + " (" + CollUtil.join(this.params, ",") + ") {\n" +
                    block.getText() + "\n}";
        }
        return "(" +
                CollUtil.join(this.params, ",") + //str -> a,b,c +
                ") => { " + block.getText() +
                " }";
    }
}