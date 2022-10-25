package rubt.input;

import arc.input.KeyCode;
import arc.math.Mathf;
import rubt.net.Send;
import rubt.world.Tile;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;

import static arc.Core.*;

public class DesktopInput extends InputHandler {

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
    }

    @Override
    protected void updateBlue() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void drawRed() {
        controlled.each(unit -> {
            Lines.stroke(2f, Color.red);
            Lines.square(unit.getX(), unit.getY(), 18f, 45f);
        });

        if (!input.keyDown(KeyCode.mouseLeft)) return;

        Draw.color(Color.red, .3f);
        Fill.crect(dragX, dragY, lastX - dragX, lastY - dragY);

        selected().each(unit -> {
            Lines.stroke(2f, Color.red);
            Lines.square(unit.getX(), unit.getY(), 16f + Mathf.absin(4f, 2f), 45f);
        });
    }

    @Override
    protected void drawBlue() {
        Tile tile = tileOn();
        if (tile == null) return;

        Lines.stroke(2f, Color.blue);
        Lines.square(tile.drawX(), tile.drawY(), 10f + Mathf.absin(4f, 2f));
    }
}
