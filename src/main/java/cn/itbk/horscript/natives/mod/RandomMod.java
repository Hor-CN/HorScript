package cn.itbk.horscript.natives.mod;

import cn.itbk.horscript.domain.ListModel;
import cn.itbk.horscript.natives.annotation.NativesClassAnnotation;
import cn.itbk.horscript.natives.annotation.NativesMethodAnnotation;
import cn.itbk.horscript.utils.RandomUtil;


@NativesClassAnnotation("随机模块")
public class RandomMod {

    /**
     * 获得随机数int值
     * @return 随机数
     */
    @NativesMethodAnnotation("随机数")
    public static int randomInt() {
        return RandomUtil.randomInt();
    }
    /**
     * 获得指定范围内的随机数 [0,limit)
     */
    @NativesMethodAnnotation("随机数")
    public static int randomInt(int limit) {
        return RandomUtil.randomInt(limit);
    }
    /**
     * 获得指定范围内的随机数
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     */
    @NativesMethodAnnotation("随机数")
    public static int randomInt(int min, int max) {
        return RandomUtil.randomInt(min,max);
    }
    /**
     * 获得随机数[0, 1)
     */
    @NativesMethodAnnotation("随机小数")
    public static double randomDouble() {
        return RandomUtil.randomDouble();
    }
    /**
     * 获得指定范围内的随机数 [0,limit)
     * @param limit 限制随机数的范围，不包括这个数
     * @return 随机数
     */
    @NativesMethodAnnotation("随机小数")
    public static double randomDouble(int limit) {
        return RandomUtil.randomDouble(limit);
    }
    /**
     * 获得指定范围内的随机数
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     */
    @NativesMethodAnnotation("随机小数")
    public static double randomDouble(int min,int max) {
        return RandomUtil.randomDouble(min,max);
    }
    /**
     * 随机字母或数字，小写
     * @return 随机字符
     */
    @NativesMethodAnnotation("随机字符")
    public static char randomChar() {
        return RandomUtil.randomChar();
    }
    /**
     * 随机字符
     * @param str 随机字符选取的样本
     * @return 随机字符
     */
    @NativesMethodAnnotation("随机字符")
    public static char randomChar(String str) {
        return RandomUtil.randomChar(str);
    }
    /**
     * 随机汉字（'\u4E00'-'\u9FFF'）
     * @return 随机的汉字字符
     */
    @NativesMethodAnnotation("随机汉字")
    public static char randomHz() {
        return RandomUtil.randomChinese();
    }
    /**
     * 随机bytes
     * @param len 长度
     * @return bytes
     */
    @NativesMethodAnnotation("随机字节")
    public static byte[] randomByte(int len) {
        return RandomUtil.randomBytes(len);
    }
    /**
     * 获得随机Boolean值
     * @return true or false
     */
    @NativesMethodAnnotation("随机布尔")
    public static boolean randomBoolean() {
        return RandomUtil.randomBoolean();
    }
    /**
     * 随机获得列表中的元素
     * @param <T>  元素类型
     * @param list 列表
     * @return 随机元素
     */
    @NativesMethodAnnotation("随机列表元素")
    public static <T> Object randomListEle(ListModel list) {
        return RandomUtil.randomEle(list.asOv());
    }
    /**
     * 随机获得列表中的一定量元素
     * @param list  列表
     * @param count 随机取出的个数
     * @return 随机元素
     */
    @NativesMethodAnnotation("随机列表元素")
    public static <T> Object randomListEleS(ListModel list,int count) {
        return RandomUtil.randomEles(list.asOv(),count);
    }

}
