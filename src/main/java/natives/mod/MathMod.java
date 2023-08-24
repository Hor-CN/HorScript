package natives.mod;

import natives.annotation.NativesClassAnnotation;
import natives.annotation.NativesMethodAnnotation;

@NativesClassAnnotation("数学模块")
public class MathMod {


    @NativesMethodAnnotation("随机数")
    public static double randomInt() {
        return Math.random();
    }
}
