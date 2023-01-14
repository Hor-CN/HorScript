package parser;

import domain.ValueModel;
import domain.VariableModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 函数作用域
 */
public class Scope {

    /**
     * 父作用域
     */
    private final Scope parent;
    /**
     * 变量
     */
    private final Map<VariableModel, ValueModel> variables;
    /**
     * 是否是局部作用域
     */
    private final boolean isLocalScope;

    public Scope() {
        // only for the global scope, the parent is null
        this(null, false);
    }

    public Scope(Scope parent, boolean localScope) {
        this.parent = parent;
        this.variables = new HashMap<>();
        this.isLocalScope = localScope;
    }


    public void assignParam(VariableModel var, ValueModel value) {
        variables.put(var, value);
    }

    public void localAssign(VariableModel var, ValueModel value) {
        this.assignParam(var, value);
    }

    public void globalAssign(VariableModel var, ValueModel value) {
        if (!isLocalScope) {
            variables.put(var, value);
        } else {
            parent.globalAssign(var, value);
        }
    }


    public void assign(VariableModel var, ValueModel value) {
        if (resolve(var, isLocalScope) != null) {
            // There is already such a variable, re-assign it
            this.reAssign(var, value);
        } else {
            // A newly declared variable
            variables.put(var, value);
        }
    }

    /**
     * 获取父作用域
     *
     * @return Scope
     */
    public Scope parent() {
        return parent;
    }

    /**
     * 是否存在父作用域
     *
     * @return boolean
     */
    public boolean isGlobalScope() {
        return parent == null;
    }

    /**
     * 是否是局部作用域
     *
     * @return boolean
     */
    public boolean isLocalScope() {
        return isLocalScope;
    }


    private void reAssign(VariableModel identifier, ValueModel value) {
        if (variables.containsKey(identifier)) {
            // The variable is declared in this scope
            variables.put(identifier, value);
        } else if (parent != null) {
            // The variable was not declared in this scope, so let
            // the parent scope re-assign it
            parent.reAssign(identifier, value);
        }
    }

    public ValueModel resolve(VariableModel var) {
        // 检查父作用域
        return resolve(var, true);
    }

    private ValueModel resolve(VariableModel var, boolean checkParent) {
        // 获取当前作用域下变量表
        ValueModel value = variables.get(var);
        if (value != null) {
            return value;
        } else if (checkParent && !isGlobalScope()) {
            // Let the parent scope look for the variable
            return parent.resolve(var, parent.isLocalScope);
        } else {
            // Unknown variable
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<VariableModel, ValueModel> var : variables.entrySet()) {
            sb.append(var.getKey()).append("->").append(var.getValue()).append(",");
        }
        return sb.toString();
    }
}
