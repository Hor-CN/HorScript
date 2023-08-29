package cn.itbk.horscript.natives.mod;

import cn.itbk.horscript.natives.annotation.NativesClassAnnotation;
import cn.itbk.horscript.natives.annotation.NativesMethodAnnotation;
import cn.itbk.horscript.utils.OperatorUtil;


@NativesClassAnnotation("数学模块")
public class MathMod {

    /**
     * 是否为数值
     * @return 布尔
     */
    @NativesMethodAnnotation("是否数字")
    public static boolean isNumber(Object x) {
        return OperatorUtil.isNumber(x);
    }
    @NativesMethodAnnotation("是否小数")
    public static boolean isDecimal(Number x) {
        return OperatorUtil.isDecimal(x);
    }
    @NativesMethodAnnotation("加")
    public static Number add(Number a,Number b) {
        return OperatorUtil.add(a,b);
    }
    @NativesMethodAnnotation("减")
    public static Number subtract(Number a,Number b) {
        return OperatorUtil.subtract(a,b);
    }
    @NativesMethodAnnotation("乘")
    public static Number multiply(Number a,Number b) {
        return OperatorUtil.multiply(a,b);
    }
    @NativesMethodAnnotation("除")
    public static Number divide(Number a,Number b,int precision) {
        return OperatorUtil.divide(a,b,precision,null);
    }
    @NativesMethodAnnotation("整除")
    public static Number aliquot(Number a,Number b) {
        return OperatorUtil.aliquot(a,b);
    }
    @NativesMethodAnnotation("取反")
    public static Number negate(Number obj) {
        return OperatorUtil.negate(obj);
    }
}
