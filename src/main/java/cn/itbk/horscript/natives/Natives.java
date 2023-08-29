package cn.itbk.horscript.natives;

import cn.itbk.horscript.domain.ModelType;
import cn.itbk.horscript.domain.ObjectModel;
import cn.itbk.horscript.domain.ValueModel;
import cn.itbk.horscript.domain.VariableModel;
import cn.itbk.horscript.natives.annotation.NativesClassAnnotation;
import cn.itbk.horscript.natives.annotation.NativesFieldAnnotation;
import cn.itbk.horscript.natives.annotation.NativesMethodAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public abstract class Natives {
    protected static final HashMap<String, ValueModel> mods = new HashMap<>();


    protected static void initNatives(Class<?> clazz){
        NativesClassAnnotation classAnnotation = clazz.getAnnotation(NativesClassAnnotation.class);
        String modName = classAnnotation.value();
        // 防止重复导入
        if (!mods.containsKey(modName)) {
            ObjectModel model = new ObjectModel();

            // 属性
            for (Field declaredField : clazz.getDeclaredFields()) {
                NativesFieldAnnotation declaredFieldAnnotation = declaredField.getAnnotation(NativesFieldAnnotation.class);
                if (declaredFieldAnnotation != null) {
                    try {
                        // 不使用 static 关键字定义常量，就不能使用反射直接获取它们的值
                        if (Modifier.isStatic(declaredField.getModifiers()) &&
                                Modifier.isFinal(declaredField.getModifiers())) {
                            Object fieldValue = declaredField.get(null);
                            VariableModel variableModel= new VariableModel(declaredFieldAnnotation.value());
                            variableModel.setType(ModelType.value);
                            model.put(variableModel,new ValueModel(fieldValue));
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            // 方法
            for (Method method : clazz.getDeclaredMethods()) {
                NativesMethodAnnotation methodAnnotation = method.getAnnotation(NativesMethodAnnotation.class);
                if (methodAnnotation != null) {
                    NativeFunction nativeFunction = new NativeFunction(methodAnnotation.value(), method);

                    VariableModel variableModel = new VariableModel(methodAnnotation.value());
                    variableModel.setId(method.getParameterCount());
                    variableModel.setType(ModelType.function);
                    model.put(variableModel ,new ValueModel(nativeFunction));
                }
            }
            mods.put(modName,new ValueModel(model));
        }
    }

    public abstract ValueModel importNative(String modName);

}
