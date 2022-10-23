package rubt.types;

import arc.scene.style.Drawable;
import arc.struct.Seq;
import rubt.Groups.GroupObject;
import rubt.ui.Textures;

import static arc.Core.*;

public abstract class ContentType extends GroupObject {

    /** Internal name used to find textures, bundles and etc. */
    public final String name;
    /** Localized name used in UI. */
    public final String localized;
    /** Description used in details menu. */
    public final String description;

    public Drawable icon;

    public <T extends GroupObject> ContentType(Seq<T> group, String name) {
        super(group);

        this.name = name;
        this.localized = bundle.get(name + ".name");
        this.description = bundle.get(name + ".desc");
    }

    /** Load icon and etc. */
    public void loadui() {
        icon = Textures.loadIcon(name);
    }
}
