package natives.mod;

import natives.annotation.NativesClassAnnotation;
import natives.annotation.NativesMethodAnnotation;

/**
 * 内置 "网络模块"
 */
@NativesClassAnnotation("网络模块")
public class NetMod {

    @NativesMethodAnnotation("是否内网IP")
    public static boolean isInnerIP() {
        return false;
    }
}
