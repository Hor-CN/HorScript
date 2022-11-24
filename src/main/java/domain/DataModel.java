package domain;

/**
 * 结果
 * @author Hor (hor@itbk.cn)
 * @version : 2022-11-10
 */
public interface DataModel {
    /** 原始 */
    Object asOv();

    /** 解开 DataModel 包裹，采用 Map 和 List 封装。 */
    Object unwrap();

    /** 判断是否 ValueModel 类型值 */
    default boolean isValue() {
        return false;
    }

    /** 判断是否为 ListModel 类型值 */
    default boolean isList() {
        return false;
    }

    /** 判断是否为 ObjectModel 类型值 */
    default boolean isObject() {
        return false;
    }
    default  <T> Object asT() {
        return null;
    }
}