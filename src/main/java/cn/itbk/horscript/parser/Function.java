package cn.itbk.horscript.parser;



import cn.itbk.horscript.domain.ReturnModel;
import cn.itbk.horscript.domain.ValueModel;
import cn.itbk.horscript.domain.VariableModel;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
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

        if (args.size() != this.params.size()) {
            throw new RuntimeException("非法函数调用");
        }
        Scope scopeNext = new Scope(parentScope, true);

        for (int i = 0; i < this.params.size(); i++) {
            ValueModel value;
            if (i < args.size()) value = args.get(i);
            else value = ValueModel.NULL;
            scopeNext.assignParam(new VariableModel(this.params.get(i).getText()), value);
        }
        HorScriptVisitor evalVisitorNext = new HorScriptVisitor(scopeNext);
        ValueModel ret = ValueModel.VOID;
        try {
            evalVisitorNext.visit(this.block);
        } catch (ReturnModel returnModel) {
            ret = returnModel.value;
        }
        return ret;
    }

    @Override
    public String toString() {
        List<String> params = new ArrayList<>();
        for (TerminalNode param : this.params) {
            params.add(param.getText());
        }
        String param =  params.isEmpty() ? "无" : String.join(",", params);

        if (this.Identifier != null) {

//            return "函数" + Identifier + " (" + String.join(",",params) + ") {\n" +
//                    block.getText() + "\n}";

            return "<函数:" + hashCode() + " 参数:"+ param +">";

        }
//        return "(" +
//                String.join(",",params) + //str -> a,b,c +
//                ") => { " + block.getText() +
//                " }";

        return "<匿名函数:" + hashCode() + " 参数:"+ param + ">";


    }
}