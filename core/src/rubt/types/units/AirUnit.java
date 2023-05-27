package rubt.types.units;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import rubt.graphics.*;
import rubt.types.UnitType;
import rubt.world.Unit;

public class AirUnit extends UnitType {

    public TextureRegion head, glow;

    public AirUnit(String name) {
        super(name);
        flying = true;
    }

    @Override
    public void loadui() {
        super.loadui();

        head = Textures.load("sprites/units/", name + "-body");
        glow = Textures.load("sprites/units/", name + "-glow");
    }

    public void update(Unit unit) {
        unit.moveTo(unit.target, size);
    }

    public void draw(Unit unit) {
        Shaders.normal.draw(unit.rotation, () -> Draw.rect(head, unit, 16f, 16f, unit.rot()));
    }

    public void drawGlow(Unit unit) {
        Draw.rect(glow, unit, 16f, 16f, unit.rot());

        engines.each(engine -> Drawf.drawEngine(engine, unit));
        // airbags.each(airbug -> Drawf.drawAirbug(airbug, unit));
    }
}
