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
        if (input.keyDown(KeyCode.mouseMiddle)) camera.position.add(
                (screenX - input.mouseX()) / renderer.current,
                (screenY - input.mouseY()) / renderer.current);
    }

    @Override
    protected void updateMisc() {
        if (input.keyTap(KeyCode.enter)) ui.chatfrag.toggle();
    }

    @Override
    protected void updateRed() {
        if (input.keyTap(KeyCode.mouseLeft)) {
            dragX = lastX;
            dragY = lastY;
        } else if (input.keyRelease(KeyCode.mouseLeft)) {
            var unit = unitOn();
            var selected = selected();

            if (selected.any() || unit == null)
                controlled = selected;
            else {
                // selects the unit if it was not selected, otherwise deselects
                if (!controlled.remove(unit)) controlled.add(unit);
            }

            dragX = dragY = -1f; // do this only after calling selected()
            ui.redfrag.rebuild();
        }

        if (input.keyTap(KeyCode.mouseRight)) controlled.each(unit -> Send.commandUnit(unit, input.mouseWorld()));

        if (input.keyTap(KeyCode.v)) Send.createUnit(tileOn() == null ? UnitTypes.sunbeam : UnitTypes.furbo, input.mouseWorld());
    }

    @Override
    protected void updateBlue() {
        Tile tile = tileOn();
        if (tile == null) return;

        if (input.keyTap(KeyCode.b)) Send.createTurret(TurretTypes.imat, new Vec2(tile.drawX(), tile.drawY()));
    }

    @Override
    protected void drawRed() {
        Lines.stroke(2f, Palette.red);
        controlled.each(unit -> {
            Lines.square(unit.getX(), unit.getY(), 18f, 45f);
            Drawf.drawTarget(unit.target); // TODO only draw unique targets
        });

        var unit = unitOn();

        if (input.keyDown(KeyCode.mouseLeft) && !scene.hasMouse()) {
            var selected = selected();

            if (selected.any() || unit == null)
                selected.each(u -> Lines.square(u.getX(), u.getY(), 16f + Mathf.absin(4f, 2f), 45f));
            else
                Lines.square(unit.getX(), unit.getY(), 16f + Mathf.absin(4f, 2f), 45f);

            Draw.alpha(.2f);
            Fill.crect(dragX, dragY, lastX - dragX, lastY - dragY);
        } else {
            // displays which unit will be selected by LMB
            if (unit != null) Lines.square(unit.getX(), unit.getY(), 16f + Mathf.absin(4f, 2f), 45f);
        }
    }

    @Override
    protected void drawBlue() {
        Tile tile = tileOn();
        if (tile == null) return;

        Lines.stroke(2f, Palette.blue);
        Lines.square(tile.drawX(), tile.drawY(), 10f + Mathf.absin(4f, 2f));
    }
}
