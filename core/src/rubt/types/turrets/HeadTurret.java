package rubt.types.turrets;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import rubt.graphics.Shaders;
import rubt.graphics.Textures;
import rubt.world.*;

public class HeadTurret extends BaseTurret {

    public TextureRegion head, glow;

    public HeadTurret(String name) {
        super(name);
    }

    @Override
    public void loadui() {
        super.loadui();

        head = Textures.load("sprites/turrets/", name + "-head");
        glow = Textures.load("sprites/turrets/", name + "-glow");
    }

    public void draw(Turret turret) {
        Shaders.normal.draw(turret.rotation, () -> Draw.rect(head, turret, 16f, 16f, turret.rot()));
    }

    public void drawGlow(Turret turret) {
        Draw.rect(glow, turret, 16f, 16f, turret.rot());
    }
}
