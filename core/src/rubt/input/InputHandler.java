package rubt.input;

import arc.graphics.g2d.Draw;
import arc.struct.Seq;
import rubt.Groups;
import rubt.world.*;

import static arc.Core.*;
import static rubt.Vars.*;

public abstract class InputHandler {

    /** Last known mouse position. */
    public float lastX, lastY;

    /** Last known mouse position in screen coordinates. */
    public int screenX, screenY;

    /** Current controlled units. */
    public Seq<Unit> controlled = new Seq<>();

    /** Mouse position where player start dragging a line. */
    public float dragX = -1f, dragY = -1f;

    public void update() {
        updateCamera();
        updateMisc();

        screenX = input.mouseX();
        screenY = input.mouseY();
        lastX = input.mouseWorldX();
        lastY = input.mouseWorldY();

        if (scene.hasMouse()) return;

        updateRed();
        updateBlue();
    }

    public void draw() {
        Draw.reset();

        drawRed();
        drawBlue();
    }

    public Tile tileOn() { // TODO replace by world.get and world stream
        return Groups.tiles.find(tile -> {
            final float x = lastX + 8f, y = lastY + 8f;
            return x > tile.getX() && x < tile.getX() + tilesize && y > tile.getY() && y < tile.getY() + tilesize;
        });
    }

    public Unit unitOn() {
        Unit closest = Groups.units.min(unit -> unit.dst(lastX, lastY));
        return closest != null && closest.dst(lastX, lastY) <= closest.type.size ? closest : null;
    }

    public Seq<Unit> selected() {
        var n = new Normalized(lastX, lastY, dragX, dragY);
        return Groups.units.select(unit -> {
            final float x = unit.getX(), y = unit.getY();
            return x > n.x1 && x < n.x2 && y > n.y1 && y < n.y2;
        });
    }

    /** Input update of the camera. */
    protected abstract void updateCamera();

    /** Input update for both teams. */
    protected abstract void updateMisc();

    /** Input update of the red team. */
    protected abstract void updateRed();

    /** Input update of the blue team. */
    protected abstract void updateBlue();

    /** Draw overlay of the red team. */
    protected abstract void drawRed();

    /** Draw overlay of the blue team. */
    protected abstract void drawBlue();

    /** Four-dimensional vector where the first two axes are always less than the other two. */
    protected class Normalized {

        public final float x1, y1, x2, y2;

        public Normalized(float x1, float y1, float x2, float y2) {
            boolean gx = x1 > x2, gy = y1 > y2;

            this.x1 = gx ? x2 : x1;
            this.y1 = gy ? y2 : y1;
            this.x2 = gx ? x1 : x2;
            this.y2 = gy ? y1 : y2;
        }
    }
}
