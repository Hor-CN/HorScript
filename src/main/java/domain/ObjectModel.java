package domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-23
 */
public class ObjectModel implements DataModel {

    private  Map<String, DataModel> dataModel = new LinkedHashMap<>();



    public List<String> fieldNames() {
        return new ArrayList<>(this.dataModel.keySet());
    }


    public void put(String key, DataModel value) {
        this.dataModel.put(key, value);
    }
    public <K, V> void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.put(e.getKey().toString(), (DataModel) e.getValue());
        }
    }

    public boolean isKey(String key) {
        return this.dataModel.containsKey(key);
    }

    public int size() {
        return this.dataModel.size();
    }
    @Override
    public Map<String, DataModel> asOv() {
        return this.dataModel;
    }

    @Override
    public Map<String, Object> unwrap() {
        Map<String, Object> unwrap = new LinkedHashMap<>(this.dataModel.size());
        this.dataModel.forEach((key, dataModel) -> {
            unwrap.put(key, dataModel.unwrap());
        });
        return unwrap;
    }

    /** 判断是否为 ObjectModel 类型值 */
    public boolean isObject() {
        return true;
    }

    /** 获取某一个元素 */
    public DataModel get(String fieldName) {
        return this.dataModel.get(fieldName);
    }

    /** 判断是否为 ObjectModel 类型值 */
    public boolean isObject(String fieldName) {
        return this.dataModel.get(fieldName) instanceof ObjectModel;
    }

    /** 判断是否为 ValueModel 类型值 */
    public boolean isValue(String fieldName) {
        return this.dataModel.get(fieldName) instanceof ValueModel;
    }

    /** 将某一个元素转换为 ValueModel */
    public ValueModel getValue(String fieldName) {
        DataModel dataItem = this.dataModel.get(fieldName);
        if (dataItem instanceof ValueModel) {
            return (ValueModel) dataItem;
        }
        if (dataItem == null) {
            return null;
        }
        throw new ClassCastException(dataItem.getClass() + " not Cast to ValueModel.");
    }


    @Override
    public String toString() {
        return dataModel.toString();
    }
}
