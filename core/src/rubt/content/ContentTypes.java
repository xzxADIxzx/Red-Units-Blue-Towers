package rubt.content;

import arc.struct.Seq;
import arc.util.Log;
import rubt.types.ContentType;

@SuppressWarnings("unchecked")
public class ContentTypes {

    public static Seq<Seq<? extends ContentType>> all;

    public static void load() {
        UnitTypes.load();
        TurretTypes.load();

        all = Seq.with(UnitTypes.all, TurretTypes.all);
        Log.info("Loaded @ items of content.", all.sum(seq -> seq.size));
    }

    public static void loadui() {
        UnitTypes.loadui();
        TurretTypes.loadui();
    }
}
