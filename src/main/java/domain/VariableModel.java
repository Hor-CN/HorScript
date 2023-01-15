package domain;

import java.util.Objects;

/** 变量 key
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2023-01-13
 */
public class VariableModel {

    private String key;
    private boolean global;
    private ModelType type = ModelType.value;

    public VariableModel(String key) {
        this.key = key;
        this.global = false;
    }
    public VariableModel(String key, boolean global) {
        this.key = key;
        this.global = global;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
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
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
