package rubt.input;

import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import rubt.content.TurretTypes;
import rubt.content.UnitTypes;
import rubt.graphics.Drawf;
import rubt.graphics.Palette;
import rubt.net.Send;
import rubt.world.Tile;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;

import static arc.Core.*;
import static rubt.Vars.*;

public class DesktopInput extends InputHandler {

    @Override
    protected void updateCamera() {
        if (Math.abs(input.axis(KeyCode.scroll)) > 0.2f && !scene.hasScroll()) renderer.zoom(input.axis(KeyCode.scroll));
    }

    @Override
    protected void updateRed() {
        if (input.keyTap(KeyCode.mouseLeft)) {
            dragX = lastX;
            dragY = lastY;
        } else if (input.keyRelease(KeyCode.mouseLeft)) {
            controlled = selected();
            dragX = dragY = -1f;
        }

        if (input.keyTap(KeyCode.mouseRight)) controlled.each(unit -> Send.commandUnit(unit, input.mouseWorld()));

        if (input.keyTap(KeyCode.mouseMiddle)) Send.createUnit(UnitTypes.imau, input.mouseWorld());
    }

    @Override
    protected void updateBlue() {
        Tile tile = tileOn();
        if (tile == null) return;

        if (input.keyTap(KeyCode.b)) Send.createTurret(TurretTypes.imat, new Vec2(tile.drawX(), tile.drawY()));
    }

    @Override
    protected void drawRed() {
        controlled.each(unit -> {
            Lines.stroke(2f, Palette.red);
            Lines.square(unit.getX(), unit.getY(), 18f, 45f);

            Drawf.drawTarget(unit.target); // TODO only draw unique targets
        });

        if (!input.keyDown(KeyCode.mouseLeft)) return;

        Draw.color(Palette.red, .2f);
        Fill.crect(dragX, dragY, lastX - dragX, lastY - dragY);

        Lines.stroke(2f, Palette.red);
        selected().each(unit -> {
            Lines.square(unit.getX(), unit.getY(), 16f + Mathf.absin(4f, 2f), 45f);
        });
    }

    @Override
    protected void drawBlue() {
        Tile tile = tileOn();
        if (tile == null) return;

        Lines.stroke(2f, Palette.blue);
        Lines.square(tile.drawX(), tile.drawY(), 10f + Mathf.absin(4f, 2f));
    }
}
