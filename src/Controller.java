/**
 * Created by lexa on 4/23/2015.
 */
public class Controller {

    public World world;
    public Camera camera;

    public Controller(World world, Camera camera) {
        this.world = world;
        this.camera= camera;
    }
    public void draw(){
        camera.draw(world);
    }
    public Point getCurrentTile(Point px) {
        Point tmp = px.same();
        Point out = new Point(0,0);

        while (tmp.x < 0) {
            out.x --;
            tmp.x += world.map.theme.size.x;
        }

        while (tmp.y < 0) {
            out.y --;
            tmp.y += world.map.theme.size.y;
        }

        out.y += tmp.y / world.map.theme.size.y;
        out.x += tmp.x / world.map.theme.size.x;
        return out;

    }
    private int getInitialDistance(Point start, Direction direction) {
        switch (direction) {
            case UP:
                return start.y%world.map.theme.size.y;
            case RIGHT:
                return world.map.theme.size.x - (start.x%world.map.theme.size.x)-1;
            case DOWN:
                return world.map.theme.size.y - (start.y%world.map.theme.size.y)-1;
            case LEFT:
                return start.x%world.map.theme.size.x;
            default:
                return 0;
        }
    }
    private int getDistanceIncrement(Direction direction){
        switch (direction) {
            case UP:
            case DOWN:
                return world.map.theme.size.y;
            case RIGHT:
            case LEFT:
                return world.map.theme.size.x;
            default:
                return 0;
        }
    }
    private void movePoint(Point point, Direction direction, int d) {
        switch (direction) {
            case UP:
                point.y-=d;
                break;
            case RIGHT:
                point.x+=d;
                break;
            case DOWN:
                point.y+=d;
                break;
            case LEFT:
                point.x-=d;
                break;
        }
    }


    /**
     * Returns the distance between given coordinates and obstacle in given direction
     */
    public int ray(Point start, Direction direction) {
        Point currentTile = getCurrentTile(start);
        Tile current = world.map.getTile(currentTile);
        int distance = -1;

        while (current != null && !current.isCollideable()) {
            if(distance == -1) {
                distance =getInitialDistance(start, direction);
            } else {
                distance+=getDistanceIncrement(direction);
            }
            movePoint(currentTile, direction, 1);
            current = world.map.getTile(currentTile);
        }
        return distance;
    }

    public int ray(Entity entity, Direction direction) {
        Point[] angles = entity.getAnglesOfSide(direction);
        int len1 = ray(angles[0], direction);
        int len2 = ray(angles[1], direction);
        return Math.min(len1,len2);
    }

    public void move(Entity entity, Direction direction) {
        int d = Math.min(ray(entity,direction), entity.movementSpeed);
        if(d > 0) {
            movePoint(camera.position, direction, d);
            movePoint(world.heroes[0].position, direction, d);
        }


    }

}
