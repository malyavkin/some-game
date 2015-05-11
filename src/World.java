import java.util.ArrayList;
public class World {
    public Map map;
    public ArrayList<Entity> heroes;
    public World(Map map, ArrayList<Entity> heroes){
        this.map = map;

        this.heroes = heroes;
    }
}

class Map {
    public Tile[] levelData;
    public Resources theme;
    int w,h;
    public Map(Tile[]lvl, int w, int h) {
        levelData = lvl;
        this.w = w;
        this.h = h;
    }

    public Map(int[][]lvl, int[] types) {
        levelData = new Tile[lvl.length*lvl[0].length];
        for (int i = 0; i < lvl.length; i++) {
            for (int j = 0; j < lvl[0].length; j++) {
                levelData[i*lvl[0].length+j] = new Tile(types[lvl[i][j]]);
            }
        }
        this.w = lvl[0].length;
        this.h = lvl.length;
    }
    public Point getXY (int i){
        return new Point(i%this.w, i/this.w);
    }
    public Tile getTile(Point position) {
        if (position.y >=0 &&
            position.y < this.h &&
            position.x >=0 &&
            position.x <this.w) {
            return levelData[position.y * this.w + position.x];
        }
        return null;
    }
    public static Map generate (int w, int h) {
        Tile[] tiles = new Tile[h * w];
        Map map;
        for (int i= 0; i<w*h; i++){
            tiles[i] = new Tile( (i>=w && i%w!=0 && i!=228) ?TileType.GROUND:TileType.WALL);
        }
        map = new Map(tiles, w, h);
        return map;
    }

}

class Tile {
    public int type;
    public Tile(int type) {
        this.type = type;
    }
    public boolean canCollide(){
        for (int i = 0; i < TileType.obstacleTypes.length; i++) {
            if(this.type == TileType.obstacleTypes[i]) {
                return true;
            }
        }
        return false;
    }
}

class TileType{
    public static int GROUND= 1024;
    public static int WALL= 1024+4*32;
    public static int[] obstacleTypes = new int[]{TileType.WALL};
}

