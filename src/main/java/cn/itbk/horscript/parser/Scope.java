package cn.itbk.horscript.parser;

import cn.itbk.horscript.domain.ValueModel;
import cn.itbk.horscript.domain.VariableModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 作用域
 */
public class Scope {

    /**
     * 父作用域
     */
    private final Scope parent;
    /**
     * 变量
     */
    private final HashMap<VariableModel, ValueModel> variables;
    /**
     * 是否是局部作用域
     */
    private final boolean isLocalScope;

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

    /**
     * 全局变量
     */
    public void globalAssign(VariableModel var, ValueModel value) {
        if (!isLocalScope) {
            variables.put(var, value);
        } else {
            parent.globalAssign(var, value);
        }
    }

    /**
     * 声明赋值变量
     */
    public void assign(VariableModel var, ValueModel value) {
        if (resolve(var, isLocalScope) != null) {
            // 已经有这样一个变量了，重新赋值
            this.reAssign(var, value);
        } else {
            // 新声明的变量
            variables.put(var, value);
        }
    }

    /**
     * 获取父作用域
     * @return Scope
     */
    public Scope parent() {
        return parent;
    }

    /**
     * 是否存在父作用域
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


    /**
     * 重新赋值
     */
    private void reAssign(VariableModel identifier, ValueModel value) {
        if (variables.containsKey(identifier)) {
            // 变量在此范围内声明
            variables.put(identifier, value);
        } else if (parent != null) {
            // 变量没有在这个范围内声明，所以让
            // 父作用域重新分配它
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
            // 让父作用域查找变量
            return parent.resolve(var, parent.isLocalScope);
        } else {
            // 未知的变量
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
