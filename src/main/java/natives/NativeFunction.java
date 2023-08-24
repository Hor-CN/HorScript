package natives;

import domain.ValueModel;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 调用内置函数.
 */
public class NativeFunction {
    protected Method method;   // 存储内置函数的方法对象
    protected String name;     // 存储内置函数的名称
    protected int numParams;   // 存储内置函数的参数数量

    /**
     * 构造函数，用于创建 NativeFunction 实例。
     * @param n 内置函数的名称
     * @param m 内置函数的方法对象
     */
    public NativeFunction(String n, Method m) {
        name = n;
        method = m;
        numParams = m.getParameterTypes().length; // 获取方法的参数数量
    }

    /**
     * 获取内置函数的参数数量。
     * @return 内置函数的参数数量
     */
    public int numOfParameters() {
        return numParams;
    }

    /**
     * 获取内置函数的方法对象。
     * @return 内置函数的方法对象
     */
    public Method getMethod() {
        return method;
    }

    /**
     * 调用内置函数并返回结果。
     * @param args 调用内置函数时传递的参数
     * @return 内置函数的调用结果
     */
    public Object invoke(List<ValueModel> args) {
        try {
            if (args.isEmpty()) {
                return method.invoke(null);
            }
            // 使用反射机制调用静态方法
            return method.invoke(null, args);
        } catch (Exception e) {
            // 在调用出现异常时捕获并处理
             throw new RuntimeException("内置函数错误: " + name + "，原因：" + e.getMessage());
        }
    }

    /**
     * 返回 NativeFunction 对象的字符串表示形式。
     * @return NativeFunction 对象的字符串表示形式
     */
    @Override
    public String toString() {
        List<String> params = IntStream.range(0, numOfParameters())
                .mapToObj(i -> "arg" + i)
                .collect(Collectors.toList());
        String param =  params.isEmpty() ? "无" : String.join(",", params);

        return "<内置模块函数:" + hashCode() + " 参数:"+ param +">";
    }
}