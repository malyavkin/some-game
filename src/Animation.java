import java.util.*;
import java.util.Map;

class Animation {
    boolean priority;
    int[] timings;
    int[] tiles;
    String name;
    public Animation(String name, boolean priority, int[] timings, int[] tiles) {
        this.priority = priority;
        this.timings = timings;
        this.tiles = tiles;
        this.name = name;
    }
}