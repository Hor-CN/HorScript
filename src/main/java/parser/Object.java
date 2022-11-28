package parser;

import domain.DataModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-28
 */
public class Object {

    // 对象中可以访问的父作用域
    private final Scope parentScope;
    // 对象属性
    private final Map<String, DataModel> dataModel = new LinkedHashMap<>();


    public Object(Scope parentScope) {
        this.parentScope = parentScope;
    }
}
