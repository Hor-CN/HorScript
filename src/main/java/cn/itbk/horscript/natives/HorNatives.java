package cn.itbk.horscript.natives;

import cn.itbk.horscript.domain.ValueModel;
import cn.itbk.horscript.natives.mod.SystemMod;
import cn.itbk.horscript.natives.mod.MathMod;
import cn.itbk.horscript.natives.mod.NetMod;
import cn.itbk.horscript.natives.mod.RandomMod;

import java.util.Arrays;
import java.util.List;

public class HorNatives extends Natives {

    private static final List<Class<?>> classes = Arrays.asList(
            SystemMod.class,
            MathMod.class,
            NetMod.class,
            RandomMod.class
    );
    private static final HorNatives instance = new HorNatives();

    private HorNatives() {
        classes.forEach(Natives::initNatives);
    }

    public static HorNatives getInstance() {
        return instance;
    }


    @Override
    public ValueModel importNative(String modName) {
        return mods.get(modName);
    }
}
