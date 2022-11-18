package parser;


import domain.ValueModel;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;
import java.util.Map;

public class Function {

    private Scope parentScope;
    private List<TerminalNode> params;
    private ParseTree block;

    Function(Scope parentScope, List<TerminalNode> params, ParseTree block) {
        this.parentScope = parentScope;
        this.params = params;
        this.block = block;
    }

    public ValueModel invoke(List<ValueModel> args, Map<String, Function> functions) {
        if (args.size() != this.params.size()) {
            throw new RuntimeException("非法函数调用");
        }
        // create function scope
        Scope scopeNext = new Scope(parentScope, true);
        for (int i = 0; i < this.params.size(); i++) {
            ValueModel value = args.get(i);
            scopeNext.assignParam(this.params.get(i).getText(), value);
        }
        HorScriptVisitor evalVisitorNext = new HorScriptVisitor(scopeNext,functions);

        ValueModel ret = ValueModel.VOID;
        try {
            evalVisitorNext.visit(this.block);
        } catch (ReturnValue returnValue) {
            ret = returnValue.value;
        }
        return ret;
    }
}