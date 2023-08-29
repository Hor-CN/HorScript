package cn.itbk.horscript.utils;

public class ObjectUtil {


    /**
     * 检查对象是否为null<br>
     * 判断标准为：
     * <pre>
     * 1. == null
     * 2. equals(null)
     * </pre>
     * @param obj 对象
     * @return 是否为null
     */
    public static boolean isNull(Object obj) {
        //noinspection ConstantConditions
        return null == obj || obj.equals(null);
    }

}
