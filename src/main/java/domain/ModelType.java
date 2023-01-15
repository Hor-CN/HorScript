package domain;

/**
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2023-01-15
 */
public enum ModelType {
    Object("对象"),
    value("值"),
    function("函数");

    private final String val;
    private ModelType(String val){
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}