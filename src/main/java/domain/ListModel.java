package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** 列表模型
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-24
 */
public class ListModel implements DataModel {

    private final List<DataModel> dataModel = new ArrayList<>();


    public ListModel() {}

    public ListModel(Collection<?> dataItem) {
        if (dataItem != null) {
            for (Object object : dataItem) {
                this.add(DomainHelper.convertTo(object));
            }
        }
    }

    /**
     * 获取某一个元素
     * @param index 下标
     * @return 元素值
     */
    public DataModel get(int index) {
        return this.dataModel.get(index);
    }

    /**
     * 向集合的末尾追加一个元素
     */
    public void add(Object object) {
        this.dataModel.add(DomainHelper.convertTo(object));
    }

    /**
     * 设置某下标元素值
     * @param intValue 下标
     * @param newVal   新值
     */
    public void set(int intValue, ValueModel newVal) {
        this.dataModel.set(intValue,newVal);
    }

    /**
     * 插入列表
     * @param listModel 列表
     */
    public void addAll(ListModel listModel) {
        this.dataModel.addAll(listModel.asOv());
    }

    /**
     * 删除某元素
     * @param object 元素
     */
    public void remove(ValueModel object) {
        this.dataModel.remove(object);
    }

    /**
     * 集合大小
     */
    public int size() {
        return this.dataModel.size();
    }

    @Override
    public List<DataModel> asOv() {
        return this.dataModel;
    }

    @Override
    public List<Object> unwrap() {
        List<Object> unwrap = new ArrayList<>(this.dataModel.size());
        this.dataModel.forEach(dataModel -> unwrap.add(dataModel.unwrap()));
        return unwrap;
    }

    /**
     * 判断是否为 ListModel 类型值
     */
    @Override
    public boolean isList() {
        return true;
    }

    /**
     * 判断是否为 ValueModel 类型值
     */
    public boolean isValue(int index) {
        return this.dataModel.get(index) instanceof ValueModel;
    }

    /**
     * 判断是否为 ListModel 类型值
     */
    public boolean isList(int index) {
        return this.dataModel.get(index) instanceof ListModel;
    }

    /**
     * 判断是否为 ObjectModel 类型值
     */
    public boolean isObject(int index) {
        return this.dataModel.get(index) instanceof ObjectModel;
    }

    /**
     * 将某一个元素转换为 ValueModel
     */
    public ValueModel getValue(int index) {
        Object dataItem = this.dataModel.get(index);
        if (dataItem instanceof ValueModel) {
            return (ValueModel) dataItem;
        }
        throw new ClassCastException(dataItem.getClass() + " not Cast to ValueModel.");
    }

    /**
     * 将某一个元素转换为 ListModel
     */
    public ListModel getList(int index) {
        Object dataItem = this.dataModel.get(index);
        if (dataItem instanceof ListModel) {
            return (ListModel) dataItem;
        }
        throw new ClassCastException(dataItem.getClass() + " not Cast to ListModel.");
    }

    /**
     * 将某一个元素转换为 ObjectModel
     */
    public ObjectModel getObject(int index) {
        Object dataItem = this.dataModel.get(index);
        if (dataItem instanceof ObjectModel) {
            return (ObjectModel) dataItem;
        }
        throw new ClassCastException(dataItem.getClass() + " not Cast to ObjectModel.");
    }

    @Override
    public String toString() {
        return  dataModel.toString();
    }

    @Override
    public boolean equals(Object obj) {
        ListModel obj1 = (ListModel) obj;
        return dataModel.equals(obj1.dataModel);
    }


}
