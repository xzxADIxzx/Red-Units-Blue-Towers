package rubt.input;

import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.ui.TextField;
import rubt.content.TurretTypes;
import rubt.content.UnitTypes;
import rubt.graphics.Drawf;
import rubt.graphics.Palette;
import rubt.logic.State;
import rubt.net.Send;
import rubt.ui.fragments.ChatFragment;
import rubt.world.Tile;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;

import static arc.Core.*;
import static rubt.Vars.*;

public class DesktopInput extends InputHandler {

    @Override
    protected void updateCamera() {
        if (state != State.game || scene.hasMouse()) return;

        if (Math.abs(input.axis(KeyCode.scroll)) > 0.2f && !scene.hasScroll()) renderer.zoom(input.axis(KeyCode.scroll));
        if (input.keyDown(KeyCode.mouseMiddle)) camera.position.add(
                (screenX - input.mouseX()) / renderer.current,
                (screenY - input.mouseY()) / renderer.current);
    }

    @Override
    protected void updateMisc() {
        if (input.keyTap(KeyCode.mouseLeft) && scene.hasField() && scene.hit(input.mouseX(), input.mouseY(), true) instanceof TextField == false)
            scene.setKeyboardFocus(null); // reset keyboard focus on click

        if (input.keyTap(KeyCode.f9)) ui.debugfrag.toggle();

        if (state == State.menu) return; // chat isn't available in main menu
        var frag = ui.chatfrag;

        if (input.keyTap(KeyCode.enter)) frag.toggle();
        if (frag.shown) {
            if (input.keyTap(KeyCode.up)) frag.next();
            if (input.keyTap(KeyCode.down)) frag.prev();

            frag.scroll = (int) Mathf.clamp(frag.scroll + input.axis(KeyCode.scroll), 0, Math.max(0, frag.messages.size - ChatFragment.messagesShown));
        }
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

        if (input.keyTap(KeyCode.v)) Send.spawnUnit(tileOn() == null ? UnitTypes.sunbeam : UnitTypes.furbo, input.mouseWorld());
    }

    @Override
    protected void updateBlue() {
        Tile tile = tileOn();
        if (tile == null) return;

        if (input.keyTap(KeyCode.b)) Send.buildTurret(TurretTypes.imat, tile);
    }

    @Override
    protected void drawRed() {
        Lines.stroke(2f, Palette.red);
        controlled.each(unit -> {
            Lines.square(unit.getX(), unit.getY(), 18f, 45f);
            Drawf.drawTarget(unit.target); // TODO only draw unique targets
        });

        var unit = unitOn();

        if (input.keyDown(KeyCode.mouseLeft) && !scene.hasMouse() && !scene.hasField()) {
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
        Lines.square(tile.getX(), tile.getY(), 10f + Mathf.absin(4f, 2f));
    }
}
