package rubt.input;

import arc.struct.Seq;
import rubt.Groups;
import rubt.logic.Logic;
import rubt.logic.State;
import rubt.net.Send;
import rubt.world.*;

import static arc.Core.*;
import static rubt.Vars.*;

public abstract class InputHandler {

    /** Last known mouse position. */
    public float lastX, lastY;

    /** Last known mouse position in screen coordinates. */
    public int screenX, screenY;

    /** Mouse position where a player start dragging a line. */
    public float dragX = -1f, dragY = -1f;

    /** Current controlled units. */
    public Seq<Unit> controlled = new Seq<>();

    public InputHandler(){
        Logic.schedule(() -> clientCon.isConnected(), () -> Send.cursor(input.mouseWorld()));
    }

    public void update() {
        updateCamera();
        updateMisc();

        screenX = input.mouseX();
        screenY = input.mouseY();
        lastX = input.mouseWorldX();
        lastY = input.mouseWorldY();

        if (scene.hasMouse() || scene.hasField()) return;

        if (state == State.editor)
            updateEditor();
        else if (state == State.game) {
            updateRed();
            updateBlue();
        }
    }

    public void draw() {
        if (state == State.editor)
            drawEditor();
        else if (state == State.game) {
            drawRed();
            drawBlue();
        }
    }

    public Tile tileOn() {
        return world.get(lastX, lastY);
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

    /** Draw overlay of the red team. */
    protected abstract void drawRed();

    /** Input update of the blue team. */
    protected abstract void updateBlue();

    /** Draw overlay of the blue team. */
    protected abstract void drawBlue();

    /** Input update of the editor */
    protected abstract void updateEditor();

    /** Draw overlay of the editor. */
    protected abstract void drawEditor();

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
