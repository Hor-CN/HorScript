package domain;

import utils.ArrayUtil;
import utils.OperatorUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2022-11-24
 */
public class DomainHelper {

    public static DataModel convertTo(Object object) {

        if (object == null) {
            // 基础类型：空
            return ValueModel.NULL;
        }

        if (object instanceof DataModel) {
            // 已经是 DataModel
            return (DataModel) object;
        }

        if (OperatorUtil.isBoolean(object)) {
            // 基础类型：boolean
            if ((boolean) object) {
                return ValueModel.TRUE;
            } else {
                return ValueModel.FALSE;
            }
        }

        if (object instanceof CharSequence) {
            // 基础类型：字符串
            return new ValueModel(String.valueOf(object));
        }

        if (OperatorUtil.isNumber(object)) {
            // 基础类型：数字
            return new ValueModel(object);
        }

        if (object.getClass().isEnum()) {
            // 外部类型：枚举 -> ValueModel（字符串）
            return new ValueModel(((Enum<?>) object).name());
        }

        if (object instanceof Map) {
            // 外部类型：Map -> ObjectModel
            Map mapData = (Map) object;
            Set entrySet = mapData.entrySet();
            ObjectModel objectModel = new ObjectModel();
            for (Object entry : entrySet) {
                if (entry instanceof Map.Entry) {
                    Object key = ((Map.Entry<?, ?>) entry).getKey();
                    Object val = ((Map.Entry<?, ?>) entry).getValue();
                    objectModel.put((VariableModel) key, convertTo(val));
                }
            }
            return objectModel;
        }

        if (object.getClass().isArray()) {
            // 外部类型：数组 -> ListModel
            Class<?> componentType = object.getClass().getComponentType();
            Object[] objectArrays = null;
            if (componentType.isPrimitive()) {
                if (Boolean.TYPE == componentType) {
                    objectArrays = ArrayUtil.toObject((boolean[]) object);
                } else if (Byte.TYPE == componentType) {
                    objectArrays = ArrayUtil.toObject((byte[]) object);
                } else if (Short.TYPE == componentType) {
                    objectArrays = ArrayUtil.toObject((short[]) object);
                } else if (Integer.TYPE == componentType) {
                    objectArrays = ArrayUtil.toObject((int[]) object);
                } else if (Long.TYPE == componentType) {
                    objectArrays = ArrayUtil.toObject((long[]) object);
                } else if (Character.TYPE == componentType) {
                    objectArrays = ArrayUtil.toObject((char[]) object);
                } else if (Float.TYPE == componentType) {
                    objectArrays = ArrayUtil.toObject((float[]) object);
                } else if (Double.TYPE == componentType) {
                    objectArrays = ArrayUtil.toObject((double[]) object);
                } else {
                    objectArrays = (Object[]) object;
                }
            } else {
                objectArrays = (Object[]) object;
            }
            return new ListModel(Arrays.asList(objectArrays));
        }

        if (object instanceof Collection) {
            // 外部类型：集合 -> ListModel
            return new ListModel((Collection<?>) object);
        }

        return ValueModel.NULL;
    }
}