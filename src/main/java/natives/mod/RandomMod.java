package natives.mod;

import natives.annotation.NativesClassAnnotation;
import natives.annotation.NativesMethodAnnotation;
import utils.NumberUtil;
import utils.RandomUtil;

@NativesClassAnnotation("随机模块")
public class RandomMod {


    @NativesMethodAnnotation("随机数")
    public static int randomInt() {
        return RandomUtil.randomInt();
    }

    @NativesMethodAnnotation("随机小数")
    public static double randomDouble() {
        return RandomUtil.randomDouble();
    }

    @NativesMethodAnnotation("范围随机数")
    public static int randomIntFw(int min, int max) {
        return RandomUtil.randomInt(min,max);
    }
}
