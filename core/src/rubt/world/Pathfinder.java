package rubt.world;

import arc.struct.Seq;

public class Pathfinder {

    public static final int maxDatasSize = 1000;

    public Path findPath(Tile from, Tile to) {
        PathData winner = null;
        Seq<PathData> datas = Seq.with(PathData.from(to));

        outer:
        while (datas.size < maxDatasSize) { // if the size is more than a thousand, there is no way

            var old = datas;
            datas = new Seq<>();

            for (PathData data : old) {
                if (data.from == from) {
                    winner = data;
                    break outer;
                }

                datas.addAll(iteratePath(data));
            }
        }

        return winner == null ? null : new Path(winner.tiles);
    }

    private Seq<PathData> iteratePath(PathData parent) {
        Seq<PathData> datas = new Seq<>();

        parent.from.neightbours().each(tile -> {
            if (parent.tiles.contains(tile)) return;
            datas.add(parent.copy(tile));
        });

        return datas;
    }

    public static record Path(Seq<Tile> tiles) {

        public Tile nextOnPath(Tile from) {
            if (tiles.size == 1) return tiles.first();

            if (tiles.peek() == from) tiles.pop();
            return tiles.peek();
        }
    }

    public static record PathData(Seq<Tile> tiles, Tile from) {

        public static PathData from(Tile tile) {
            return new PathData(Seq.with(tile), tile);
        }

        public PathData copy(Tile tile) {
            return new PathData(tiles.copy().add(tile), tile);
        }
    }
}
