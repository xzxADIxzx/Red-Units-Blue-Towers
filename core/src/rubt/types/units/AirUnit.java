package rubt.types.units;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import rubt.graphics.Drawf;
import rubt.graphics.Textures;
import rubt.types.UnitType;
import rubt.types.textures.NormalTexture;
import rubt.world.Unit;

public class AirUnit extends UnitType {

    public NormalTexture head;
    public TextureRegion glow;

    public AirUnit(String name) {
        super(name);
    }

    @Override
    public void loadui() {
        super.loadui();

        head = Textures.loadNormal("sprites/units/" + name + "-body");
        glow = Textures.load("sprites/units/", name + "-glow");
    }

    public void update(Unit unit) {
        unit.moveVel(unit.target);
        unit.faceMovement();
    }

    public void draw(Unit unit) {
        Draw.rect(head.region(unit.rotation), unit, 16f, 16f, unit.rot());
    }

    public void drawGlow(Unit unit) {
        Draw.rect(glow, unit, 16f, 16f, unit.rot());

        engines.each(engine -> Drawf.drawEngine(engine, unit));
        // airbags.each(airbug -> Drawf.drawAirbug(airbug, unit));
    }
}
