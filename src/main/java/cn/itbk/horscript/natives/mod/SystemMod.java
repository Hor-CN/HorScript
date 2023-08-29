package cn.itbk.horscript.natives.mod;

import cn.itbk.horscript.natives.annotation.NativesClassAnnotation;
import cn.itbk.horscript.natives.annotation.NativesFieldAnnotation;
import cn.itbk.horscript.natives.annotation.NativesMethodAnnotation;

import java.util.Scanner;

/**
 * 内置 "系统模块"
 */
@NativesClassAnnotation("系统模块")
public class SystemMod {

    private static final Scanner scanner = new Scanner(System.in);


    @NativesFieldAnnotation("版本")
    public static final String version = "V1.0";


    @NativesMethodAnnotation("当前时间戳")
    public static long currentTime() {
        return System.currentTimeMillis();
    }

    @NativesMethodAnnotation("输入数字")
    public static int inputInt() {
        return scanner.nextInt();
    }

    @NativesMethodAnnotation("输入")
    public static String inputStr() {
        return scanner.next();
    }

    @NativesMethodAnnotation("输出")
    public static void print(Object o) {
        System.out.print(o);
    }
    @NativesMethodAnnotation("输出换行")
    public static void println(Object o) {
        System.out.println(o);
    }


}
