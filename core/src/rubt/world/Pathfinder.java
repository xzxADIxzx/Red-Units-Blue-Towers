package rubt.world;

import arc.struct.Seq;

public class Pathfinder {

    public Path findPath(Tile from, Tile to) {
        var tiles = Seq.with(from);

        // TODO find path, huh

        return new Path(tiles.add(to));
    }

    public static record Path(Seq<Tile> tiles) {

        public Tile nextOnPath(Tile from) {
            if (tiles.peek() == from) tiles.pop();
            return tiles.peek();
        }
    }
}
