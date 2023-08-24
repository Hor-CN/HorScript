package domain;

import java.util.Objects;

/** 变量名模型
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2023-01-13
 */
public class VariableModel {

    /**
     *  键值
     *  */
    private String key;

    private int id = 0;
    /**
     * 变量类型 默认 值类型
     */
    private ModelType type = ModelType.value;

    public VariableModel(String key) {
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }


    public ModelType getType() {
        return type;
    }

    public void setType(ModelType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableModel that = (VariableModel) o;
        return Objects.equals(key, that.key) && Objects.equals(id,that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key + id);
    }

    @Override
    public String toString() {
        return getKey();
    }
}
