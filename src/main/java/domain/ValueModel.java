package domain;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import core.HorScriptParser.AnyObjectContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import parser.Function;
import utils.NumberUtil;
import utils.OperatorUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/** 变量值模型
 * @author hor (hor@itbk.cn)
 * @version : 1.0
 * @date : 2023-01-13
 */
public class ValueModel implements Comparable<ValueModel>,DataModel{

    // 空值
    public static ValueModel NULL  = new ValueModel("空");
    // 无返回值
    public static final ValueModel VOID = new ValueModel();
    // 真
    public static ValueModel TRUE = new ValueModel(true);
    // 假
    public static ValueModel FALSE = new ValueModel(false);

    private Object value;

    public ValueModel() {
        // 私有构造函数: 仅用于NULL和VOID
        value = new Object();
    }

    public ValueModel(Object value) {
        this.value = value;
    }


    public ValueModel setValue(Object value) {
        this.value = value;
        return this;
    }

    /**
     * 原始值
     */
    @Override
    public Object asOv() {
        return this.value;
    }


    /**
     * 解开 DataModel 包裹，采用 Map 和 List 封装。
     */
    @Override
    public Object unwrap() {
        return this.value;
    }

    /** 判断是否为 ValueModel 类型值 */
    public boolean isValue() {
        return true;
    }

    /** 判断是否为 ObjectModel **/
    public boolean isObjectModel() {
        return this.value instanceof ObjectModel;
    }

    /** 判断是否为 Function **/
    public boolean isFunction() {
        return this.value instanceof Function;
    }

    public Function asFunction() {
        return (Function) this.value;
    }

    public List<TerminalNode> asExplicitParameter() {
        return Convert.convert(new TypeReference<List<TerminalNode>>() {
        }, this.value);
    }

    public List<AnyObjectContext> asImplicitParameter() {
        return Convert.convert(new TypeReference<List<AnyObjectContext>>() {
        }, this.value);
    }


    public ObjectModel asObjectModel() {
        return (ObjectModel) this.value;
    }

    /** 判断是否为 Null */
    public boolean isNull() {
        return this.value == null;
    }

    public boolean isVoid() {
        return this == VOID;
    }

    /** 是否可以转为 Number 类型值 */
    public boolean isNumber() {
        return OperatorUtil.isNumber(this.value);
    }

    /** 是否可以转为 byte 类型值 */
    public boolean isByte() {
        return OperatorUtil.isByteNumber(this.value);
    }

    /** 是否可以转为 short 类型值 */
    public boolean isShort() {
        return OperatorUtil.isShortNumber(this.value) || isByte();
    }

    /** 是否可以转为 int 类型值 */
    public boolean isInt() {
        return OperatorUtil.isIntegerNumber(this.value) || isShort();
    }

    /** 是否可以转为 long 类型值 */
    public boolean isLong() {
        return OperatorUtil.isLongNumber(this.value) || isInt();
    }

    /** 是否可以转为 BigInteger 类型值 */
    public boolean isBigInteger() {
        return this.value instanceof BigInteger || isLong();
    }

    /** 是否可以转为 float 类型值 */
    public boolean isFloat() {
        return OperatorUtil.isFloatNumber(this.value);
    }

    /** 是否可以转为 double 类型值 */
    public boolean isDouble() {
        return OperatorUtil.isDoubleNumber(this.value) || isFloat();
    }

    /** 是否可以转为 BigDecimal 类型值 */
    public boolean isBigDecimal() {
        return this.value instanceof BigDecimal || isDouble();
    }

    /** 是否可以转为 Decimal 类型值 */
    public boolean isDecimal() {
        return isFloat() || isDouble() || isBigDecimal();
    }

    @Override
    public boolean isList() {
        return value instanceof ListModel;
    }

    /** 是否可以转为 boolean 类型值 */
    public boolean isBoolean() {
        return OperatorUtil.isBoolean(this.value);
    }



    /** 转换为 boolean 值，如果为空值，那么返回false。任何整数非0值都为true */
    public boolean asBoolean() {
        if (this.value == null) {
            return false;
        }
        if (this.value instanceof Boolean) {
            return (Boolean) this.value;
        }
        if (this.value instanceof Number) {
            return ((Number) this.value).intValue() != 0;
        }
        if (this.value instanceof String) {
            String strVal = (String) this.value;
            if (strVal.length() == 0 //
                    || "空".equals(strVal)) {
                return false;
            }
            if ("真".equalsIgnoreCase(strVal) //
                    || "1".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("假".equalsIgnoreCase(strVal) //
                    || "0".equals(strVal)) {
                return Boolean.FALSE;
            }
        }
        throw new ClassCastException("无法强制转换为布尔值，值: " + this.value);
    }

    /** 转换为 Number 值，如果为空值，那么返回 (int)0 */
    public Number asNumber() {
        if (this.value == null) {
            return 0;
        }
        return ((Number) this.value);
    }

    /** 转换为 byte 值，如果为空值，那么返回 0 */
    public byte asByte() {
        if (this.value == null) {
            return 0;
        }
        if (this.value instanceof Number) {
            return ((Number) this.value).byteValue();
        }
        if (this.value instanceof String) {
            String strVal = (String) this.value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return 0;
            }
            if (NumberUtil.isNumber(strVal)) {
                return Byte.parseByte(strVal);
            }
        }
        if (this.value instanceof Boolean) {
            return (byte) ((Boolean) this.value ? 1 : 0);
        }
        throw new ClassCastException("can not cast to byte, value : " + this.value);
    }

    /** 转换为 short 值，如果为空值，那么返回 0 */
    public short asShort() {
        if (this.value == null) {
            return 0;
        }
        if (this.value instanceof Number) {
            return ((Number) this.value).shortValue();
        }
        if (this.value instanceof String) {
            String strVal = (String) this.value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return 0;
            }
            if (NumberUtil.isNumber(strVal)) {
                return Short.parseShort(strVal);
            }
        }
        if (this.value instanceof Boolean) {
            return (short) ((Boolean) this.value ? 1 : 0);
        }
        throw new ClassCastException("can not cast to short, value : " + this.value);
    }

    /** 转换为 int 值，如果为空值，那么返回 0 */
    public int asInt() {
        if (this.value == null) {
            return 0;
        }
        if (this.value instanceof Integer) {
            return (Integer) this.value;
        }
        if (this.value instanceof Number) {
            return ((Number) this.value).intValue();
        }
        if (this.value instanceof String) {
            String strVal = (String) this.value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return 0;
            }
            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            if (NumberUtil.isNumber(strVal)) {
                return Integer.parseInt(strVal);
            }
        }
        if (this.value instanceof Boolean) {
            return (Boolean) this.value ? 1 : 0;
        }
        throw new ClassCastException("can not cast to int, value : " + this.value);
    }


    public int asInteger() {
        return (Integer) this.value;
    }


    /** 转换为 long 值，如果为空值，那么返回 0 */
    public long asLong() {
        if (this.value == null) {
            return 0;
        }
        if (this.value instanceof Number) {
            return ((Number) this.value).longValue();
        }
        if (this.value instanceof String) {
            String strVal = (String) this.value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return 0;
            }
            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            if (NumberUtil.isNumber(strVal)) {
                return Long.parseLong(strVal);
            }
        }
        if (this.value instanceof Boolean) {
            return (Boolean) this.value ? 1 : 0;
        }
        throw new ClassCastException("can not cast to long, value : " + this.value);
    }

    /** 转换为 BigDecimal 值，如果为空值，那么返回 BigDecimal.ZERO */
    public BigInteger asBigInteger() {
        if (this.value == null) {
            return BigInteger.ZERO;
        }
        if (this.value instanceof BigInteger) {
            return (BigInteger) this.value;
        }
        if (this.value instanceof Float || this.value instanceof Double) {
            return BigInteger.valueOf(((Number) this.value).longValue());
        }
        String strVal = this.value.toString();
        if (strVal.length() == 0 //
                || "null".equals(strVal) //
                || "NULL".equals(strVal)) {
            return BigInteger.ZERO;
        }
        if (this.value instanceof Boolean) {
            return (Boolean) this.value ? BigInteger.ONE : BigInteger.ZERO;
        }
        try {
            return new BigInteger(strVal);
        } catch (NumberFormatException e) {
            throw new ClassCastException("can not cast to BigInteger, value : " + this.value);
        }
    }

    /** 转换为 float 值，如果为空值，那么返回 0.0 */
    public float asFloat() {
        if (this.value == null) {
            return 0.0f;
        }
        if (this.value instanceof Number) {
            return ((Number) this.value).floatValue();
        }
        if (this.value instanceof String) {
            String strVal = this.value.toString();
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return 0.0f;
            }
            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            if (NumberUtil.isNumber(strVal)) {
                return Float.parseFloat(strVal);
            }
        }
        if (this.value instanceof Boolean) {
            return (Boolean) this.value ? 1 : 0;
        }
        throw new ClassCastException("can not cast to float, value : " + this.value);
    }

    /** 转换为 double 值，如果为空值，那么返回 0.0 */
    public Double asDouble() {
        if (this.value == null) {
            return 0.0d;
        }
        if (this.value instanceof Number) {
            return ((Number) this.value).doubleValue();
        }
        if (this.value instanceof String) {
            String strVal = this.value.toString();
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)
                    || "空".equals(strVal)) {
                return 0.0d;
            }
            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            if (NumberUtil.isNumber(strVal)) {
                return Double.parseDouble(strVal);
            }
        }
        if (this.value instanceof Boolean) {
            return (Boolean) this.value ? 1.0 : 0.0;
        }
        throw new ClassCastException("can not cast to double, value : " + this.value);
    }

    /** 转换为 BigDecimal 值，如果为空值，那么返回 BigDecimal.ZERO */
    public BigDecimal asBigDecimal() {
        if (this.value == null) {
            return BigDecimal.ZERO;
        }
        if (this.value instanceof BigDecimal) {
            return (BigDecimal) this.value;
        }
        if (this.value instanceof BigInteger) {
            return new BigDecimal((BigInteger) this.value);
        }
        if (this.value instanceof Boolean) {
            return (Boolean) this.value ? BigDecimal.ONE : BigDecimal.ZERO;
        }
        String strVal = this.value.toString();
        if (strVal.length() == 0) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(strVal);
        } catch (NumberFormatException e) {
            throw new ClassCastException("can not cast to BigDecimal, value : " + this.value);
        }
    }

    /** 判断是否为 String 类型值 */
    public boolean isString() {
        return value instanceof String;
    }

    /** 转换为 String 值 */
    public String asString() {
        return toString();
    }

    /** 转换为List  */
    public ListModel asList() {
        return (ListModel) value;
    }

    public <T> Object asT() {
        if (this.isList()) {
            return this.asList();
        }
        else if (this.isNumber()) {
            return  this.asLong();
        }
        else if (this.isString()) {
            return this.asString();
        }
        else if (this.isBoolean()) {
            return this.asBoolean();
        }
        else {
            throw new ClassCastException("无法识别值类型："+this.value);
        }

    }

    @Override
    public int compareTo(ValueModel that) {
        if(this.isNumber() && that.isNumber()) {
            if(this.equals(that)) {
                return 0;
            }
            else {
                return this.asDouble().compareTo(that.asDouble());
            }
        }
        else if(this.isString() && that.isString()) {
            return this.asString().compareTo(that.asString());
        }
        else {
            throw new RuntimeException("非法表达式: 无法比较 `" + this + "` to `" + that + "`");
        }
    }
    @Override
    public boolean equals(Object o) {
        if(this == VOID || o == VOID) {
            throw new RuntimeException("不能使用VOID: " + this + " ==/!= " + o);
        }
        if(this == o) {
            return true;
        }
        if(o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ValueModel that = (ValueModel)o;
        if(this.isNumber() && that.isNumber()) {
            double diff = Math.abs(this.asDouble() - that.asDouble());
            return diff < 0.00000000001;
        }
        else {
            return this.value.equals(that.value);
        }
    }
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public String toString() {
        if (isVoid()) {
            return "";
        }
        return isNull() ? "空" : String.valueOf(value);
    }

}
