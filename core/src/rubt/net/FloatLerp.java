package rubt.net;

import arc.math.Mathf;
import arc.util.Time;
import arc.util.io.Reads;
import rubt.logic.Logic;

public class FloatLerp {

    public static float updateSpacing = 1000f / Logic.litsFrequency;
    public float last, target;

    public void set(float value) {
        last = target;
        target = value;
    }

    public void read(Reads r) {
        set(r.f());
    }

    public float get(long lastUpdate) {
        return Mathf.lerp(last, target, Time.timeSinceMillis(lastUpdate) / updateSpacing);
    }

    public float getAngel(long lastUpdate){
        return Mathf.slerp(last, target, Time.timeSinceMillis(lastUpdate) / updateSpacing);
    }
}
