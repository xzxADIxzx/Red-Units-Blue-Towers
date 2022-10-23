package rubt.input;

import arc.input.KeyCode;
import rubt.world.Tile;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;

import static arc.Core.*;

public class DesktopInput extends InputHandler {

    @Override
    protected void updateRed() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void updateBlue() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void drawRed() {
        if (!input.keyDown(KeyCode.mouseLeft)) return;

        Draw.color(Color.red, .3f);
        Fill.crect(dragX, dragY, lastX - dragX, lastY - dragY);
    }

    @Override
    protected void drawBlue() {
        Tile tile = tileOn();
        if (tile == null) return;

        Lines.stroke(2f, Color.blue);
        Lines.square(tile.drawX(), tile.drawY(), 12f);
    }
}
