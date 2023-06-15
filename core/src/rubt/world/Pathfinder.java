package rubt.world;

import arc.struct.Seq;

/**
 * Simple pathfinding algorithm, nothing interesting here.
 *
 * @author xzxADIxzx
 */
public class Pathfinder {

    /** Max number of tiles that will be iterated before {@link Pathfinder} gives up. */
    public static final int maxTilesSize = 1024;

    /** Finds a path from beginning to end, otherwise returns null. */
    public Path findPath(Tile from, Tile to) {
        Seq<Tile> iterated = Seq.with(to); // start looking for a way from the end
        Seq<Path> paths = Seq.with(Path.with(to));

        while (iterated.size < maxTilesSize) { // if more than a thousand tiles are iterated, then there is no way

            var old = paths;
            paths = new Seq<>(old.size);

            for (Path path : old) {
                if (path.start() == from) return path;

                for (Tile tile : path.start().neighbours) {
                    if (iterated.contains(tile) || tile.type.solid) continue;
                    iterated.add(tile); // save the tile in seq so that we don’t even remember about it anymore

                    // create copies of the path on all non-iterated walkable tiles
                    paths.add(path.copy(tile));
                }
            }
        }

        return null;
    }

    /** Set of tiles with methods for traversing the path. */
    public static record Path(Seq<Tile> tiles) {

        /** Creates a path with a given set of tiles. */
        public static Path with(Tile... tiles) {
            return new Path(Seq.with(tiles));
        }

        /** Copies the path and adds a given tile to it. */
        public Path copy(Tile tile) {
            return new Path(tiles.copy().add(tile));
        }

        /**
         * Returns the start of the path.
         * <p>
         * Note: with each tile passed, the start will move forward.
         */
        public Tile start() {
            return tiles.peek();
        }

        /** Returns the end of the path. */
        public Tile end() {
            return tiles.first();
        }

        /** Returns the next tile on the path. */
        public Tile nextTile(Tile current) {
            if (tiles.peek() == current && tiles.size > 1) tiles.pop();
            return tiles.peek();
        }
    }
}
