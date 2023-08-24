package natives.mod;

import domain.ValueModel;
import natives.annotation.NativesClassAnnotation;
import natives.annotation.NativesFieldAnnotation;
import natives.annotation.NativesMethodAnnotation;

import java.util.Random;
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

}
