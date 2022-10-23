package rubt.input;

import arc.graphics.g2d.Draw;
import arc.input.KeyCode;
import rubt.Groups;
import rubt.world.Tile;

import static arc.Core.*;
import static rubt.Vars.*;

public abstract class InputHandler {

    /** Last known mouse position. */
    public float lastX, lastY;

    /** Mouse position where player start dragging a line. */
    public float dragX = -1f, dragY = -1f;

    public void update() {
        lastX = input.mouseWorldX();
        lastY = input.mouseWorldY();

        if (input.keyTap(KeyCode.mouseLeft)) {
            dragX = lastX;
            dragY = lastY;
        } else if (input.keyRelease(KeyCode.mouseLeft)) dragX = dragY = -1f;

        updateRed();
        updateBlue();
    }

    public void draw() {
        Draw.reset();

        drawRed();
        drawBlue();
    }

    public Tile tileOn() {
        return Groups.tiles.find(tile -> {
            final float x = lastX + 8f, y = lastY + 8f;
            return x > tile.drawX() && x < tile.drawX() + tilesize && y > tile.drawY() && y < tile.drawY() + tilesize;
        });
    }

    /** Input update of the red team. */
    protected abstract void updateRed();

    /** Input update of the blue team. */
    protected abstract void updateBlue();

    /** Draw overlay of the red team. */
    protected abstract void drawRed();

    /** Draw overlay of the blue team. */
    protected abstract void drawBlue();
}
