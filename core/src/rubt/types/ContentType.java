package rubt.types;

import arc.scene.style.Drawable;
import arc.struct.Seq;
import rubt.Groups.GroupObject;
import rubt.graphics.Textures;

import static arc.Core.*;

public abstract class ContentType extends GroupObject {

    /** Internal name used to find textures, bundles and etc. */
    public final String name;
    /** Localized name used in UI. */
    public final String localized;
    /** Description used in details menu. */
    public final String description;

    public Drawable icon;

    public ContentType(Seq<? extends GroupObject> group, String name) {
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
