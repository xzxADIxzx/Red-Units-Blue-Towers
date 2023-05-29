package rubt.world;

import arc.func.Prov;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import rubt.Groups.Entity;

public class Entities {

    private static Seq<Prov<? extends Entity>> provs = new Seq<>();
    private static ObjectIntMap<Class<?>> entityToId = new ObjectIntMap<>();

    /** Registers a new entity type for serialization. */
    public static void register(Prov<? extends Entity> prov, Class<? extends Entity> type) {
        provs.add(prov);
        entityToId.put(type, provs.size - 1); // could be obtained from the constructor, but then new entity would get into Groups
    }

    /** Returns the entity type id used for serialization. */
    public static byte typeId(Entity entity) {
        int id = entityToId.get(entity.getClass(), -1);
        if (id == -1) throw new RuntimeException("Unknown entity!");

        return (byte) id;
    }

    /** Creates a new entity by the given id. */
    public static Entity newEntity(byte id) {
        return provs.get(id).get();
    }

    public static void load() {
        register(Unit::new, Unit.class);
        register(Turret::new, Turret.class);
        register(Tile::new, Tile.class);
    }
}
