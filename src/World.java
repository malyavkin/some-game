import java.util.ArrayList;
import java.util.Random;
public class World {
    public Map map;
    public ArrayList<Entity> heroes;
    public World(Map map, ArrayList<Entity> heroes){
        this.map = map;

        this.heroes = heroes;
    }
}

class Map {
    public Tile[] lvldata;
    public Resources theme;
    int w,h;
    public Map(Tile[]lvl, int w, int h) {
        lvldata = lvl;
        this.w = w;
        this.h = h;
    }
    public Tile getTile(Point position) {
        if (position.y >=0 &&
            position.y < this.h &&
            position.x >=0 &&
            position.x <this.w) {
            return lvldata[position.y * this.w + position.x];
        }
        return null;
    }
    public static Map generate (int w, int h) {
        Tile[] tiles = new Tile[h * w];
        Map map;
        Random r = new Random();
        for (int i= 0; i<w*h; i++){
            //tiles[i] = new Tile(r.nextBoolean()?TileType.GROUND:TileType.WALL);
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
    public boolean isCollideable(){
        for (int i = 0; i < TileType.collidables.length; i++) {
            if(this.type == TileType.collidables[i]) {
                return true;
            }
        }
        return false;
    }
}

class TileType{
    public static int GROUND= 1024;
    public static int WALL= 1024+4*32;
    public static int[] collidables = new int[]{TileType.WALL};
}

