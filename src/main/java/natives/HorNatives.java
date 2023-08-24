package natives;

import domain.ValueModel;
import natives.mod.MathMod;
import natives.mod.NetMod;
import natives.mod.SystemMod;
import java.util.Arrays;
import java.util.List;

public class HorNatives extends Natives {

    private static final List<Class<?>> classes = Arrays.asList(
            SystemMod.class,
            MathMod.class,
            NetMod.class
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
