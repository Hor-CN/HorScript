package parser;

import domain.DataModel;
import domain.ObjectModel;
import domain.ValueModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-28
 */
public class Object {

    // 对象中可以访问的父对象
    private final Object parent;
    // 对象属性
    private final Map<String, DataModel> dataModel = new LinkedHashMap<>();

    public Object() {
        this(null);
    }
    public Object(Object parent) {
        this.parent = parent;
    }

    public void add(String id, ValueModel valueModel) {
        dataModel.put(id,valueModel);
    }

    public ValueModel get(String id) {
        return (ValueModel) dataModel.get(id);
    }

    // 是否存在父对象
    public boolean isGlobalObject() {
        return parent != null;
    }
    public Object parent() {
        return parent;
    }


}
