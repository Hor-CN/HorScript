package utils;


import utils.pool.StrPool;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringUtil implements StrPool {

    /**
     * 字符串常量：{@code "null"} <br>
     * 注意：{@code "null" != null}
     */
    public static final String NULL = "null";

    /**
     * 字符串常量：空字符串 {@code ""}
     */
    public static final String EMPTY = "";

    /**
     * 字符串常量：空格符 {@code " "}
     */
    public static final String SPACE = " ";


    /**
     * 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }


    public static boolean isBlank(CharSequence str) {
        final int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!CharUtil.isBlankChar(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }


    /**
     * 字符串是否为空
     * @param str 被检测的字符串
     * @return 是否为空
     * @see #isBlank(CharSequence)
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is {} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示，如果模板为null，返回"null"
     * @param params   参数值
     * @return 格式化后的文本，如果模板为null，返回"null"
     */
    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return NULL;
        }
        if (ArrayUtil.isEmpty(params) || isBlank(template)) {
            return template.toString();
        }
        return format(template.toString(), params);
    }


    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray   参数列表
     * @return 结果
     */
    public static String format(String strPattern, Object... argArray) {
        return formatWith(strPattern, StringUtil.EMPTY_JSON, argArray);
    }

    /**
     * 格式化字符串<br>
     * 此方法只是简单将指定占位符 按照顺序替换为参数<br>
     * 如果想输出占位符使用 \\转义即可，如果想输出占位符之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "{}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "{}", "a", "b") =》 this is {} for a<br>
     * 转义\： format("this is \\\\{} for {}", "{}", "a", "b") =》 this is \a for b<br>
     *
     * @param strPattern  字符串模板
     * @param placeHolder 占位符，例如{}
     * @param argArray    参数列表
     * @return 结果
     * @since 5.7.14
     */
    public static String formatWith(String strPattern, String placeHolder, Object... argArray) {
        if (StringUtil.isBlank(strPattern) || StringUtil.isBlank(placeHolder) || ArrayUtil.isEmpty(argArray)) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();
        final int placeHolderLength = placeHolder.length();

        // 初始化定义好的长度以获得更好的性能
        final StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;// 记录已经处理到的位置
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            delimIndex = strPattern.indexOf(placeHolder, handledPosition);
            if (delimIndex == -1) {// 剩余部分无占位符
                if (handledPosition == 0) { // 不带占位符的模板直接返回
                    return strPattern;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                sbuf.append(strPattern, handledPosition, strPatternLength);
                return sbuf.toString();
            }

            // 转义符
            if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == StringUtil.C_BACKSLASH) {// 转义符
                if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == StringUtil.C_BACKSLASH) {// 双转义符
                    // 转义符之前还有一个转义符，占位符依旧有效
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append(str(argArray[argIndex], StandardCharsets.UTF_8));
                    handledPosition = delimIndex + placeHolderLength;
                } else {
                    // 占位符被转义
                    argIndex--;
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append(placeHolder.charAt(0));
                    handledPosition = delimIndex + 1;
                }
            } else {// 正常占位符
                sbuf.append(strPattern, handledPosition, delimIndex);
                sbuf.append(str(argArray[argIndex], StandardCharsets.UTF_8));
                handledPosition = delimIndex + placeHolderLength;
            }
        }

        // 加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPatternLength);

        return sbuf.toString();
    }

    /**
     * 将对象转为字符串
     * <pre>
     * 	 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
     * 	 2、对象数组会调用Arrays.toString方法
     * </pre>
     *
     * @param obj     对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return str((byte[]) obj, charset);
        } else if (obj instanceof Byte[]) {
            return str((Byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        } else if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.toString(obj);
        }

        return obj.toString();
    }



    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     * 如果经过修正的index中from大于to，则互换from和to example: <br>
     * @param str              String
     * @param startIndex 开始的index（包括）
     * @param endIndex   结束的index（不包括）
     * @return 字串
     */
    public static String sub(CharSequence str, int startIndex, int endIndex) {
        if (isEmpty(str)) {
            return str(str);
        }
        int len = str.length();

        if (startIndex < 0) {
            startIndex = len + startIndex;
            if (startIndex < 0) {
                startIndex = 0;
            }
        } else if (startIndex > len) {
            startIndex = len;
        }

        if (endIndex < 0) {
            endIndex = len + endIndex;
            if (endIndex < 0) {
                endIndex = len;
            }
        } else if (endIndex > len) {
            endIndex = len;
        }

        if (endIndex < startIndex) {
            int tmp = startIndex;
            startIndex = endIndex;
            endIndex = tmp;
        }

        if (startIndex == endIndex) {
            return EMPTY;
        }

        return str.toString().substring(startIndex, endIndex);
    }


    /**
     * 实现类似Python的字符串切片功能
     *
     * @param str        String
     * @param startIndex 开始的index（包括）
     * @param endIndex   结束的index（不包括）
     * @param step       步长,正值表示从左到右取值，负值表示从右到左取值,不能为0
     * @return 字符串
     */
    public static String sub(CharSequence str, Integer startIndex, Integer endIndex, int step) {
        if (isEmpty(str)) {
            return str(str);
        }
        if (step == 0) {
            step = 1;
        }

        if (step < 0) {
            // 从右往左取值，而startIndex省略时，表示从端点开始，因此这里的端点是“终点”
            if (startIndex == null) {
                startIndex = str.length() - 1;
            } else if (startIndex < 0) {
                startIndex = str.length() + startIndex;
            }
            if (endIndex == null) {
                endIndex = -1;
            } else if (endIndex < 0) {
                endIndex = str.length() + endIndex;
            }
        } else {
            // 从左往右取值，而startIndex省略时，表示从端点开始，因此这里的端点是“起点”
            if (startIndex == null) {
                startIndex = 0;
            } else if (startIndex < 0) {
                startIndex = str.length() + startIndex;
            }
            if (endIndex == null) {
                endIndex = str.length();
            } else if (endIndex < 0) {
                endIndex = str.length() + endIndex;
            }
        }
        if (startIndex.equals(endIndex)) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; step > 0 ? i < endIndex : i > endIndex; i += step) {
            char c = charAt(str, i);
            if (c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 获取字符串下标
     *
     * @param str   字符串
     * @param index 下标，允许存在正负值，负值表示从后取值
     * @return 下标字符
     */
    public static char charAt(CharSequence str, int index) {
        if (index >= 0) {
            if (index > str.length() - 1) {
                return 0;
            }
            return str.charAt(index);
        } else {
            index = str.length() + index;
            if (index > 0) {
                return str.charAt(index);
            } else {
                return 0;
            }
        }
    }




}
