package cn.itbk.horscript.natives.mod;

import cn.itbk.horscript.natives.annotation.NativesClassAnnotation;
import cn.itbk.horscript.natives.annotation.NativesMethodAnnotation;

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
