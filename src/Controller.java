import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Objects;

public class Controller {

    public World world;
    public Camera camera;

    public Entity getAnchor() {
        return anchor;
    }


    public void setAnchor(Entity anchor) {
        this.anchor = anchor;
    }

    public Entity anchor;

    public Controller(World world, Camera camera) {
        this.world = world;
        this.camera= camera;
    }
    public void draw(){
        camera.draw(world);
    }
    public Point getCurrentTile(Point px) {
        Point tmp = px.same();
        Point out = new Point();

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

        while (current != null && !current.canCollide()) {
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


    public boolean rayEntities_rectangleOnRay(Point point, Rectangle rectangle, Direction direction){
        boolean result = false;
        switch (direction) {
            case UP:
                result = rectangle.position.x <= point.x
                        && rectangle.position.x+rectangle.size.x-1 >= point.x
                        && rectangle.position.y <= point.y;
                break;
            case RIGHT:
                // top of a rectangle is higher
                result = rectangle.position.y <= point.y
                        // bottom of a rectangle is lower
                        && rectangle.position.y+rectangle.size.y-1 >= point.y
                        // left of a rectangle is to the right
                        && rectangle.position.x >= point.x;
                break;
            case DOWN:
                result = rectangle.position.x <= point.x
                        && rectangle.position.x+rectangle.size.x-1 >= point.x
                        && rectangle.position.y >= point.y;
                break;
            case LEFT:
                result = rectangle.position.y <= point.y
                        && rectangle.position.y+rectangle.size.y-1 >= point.y
                        && rectangle.position.x <= point.x;
                break;
        }
        return result;
    }

    public boolean rayEntities_isCloser(int bestValue, int thisValue, Direction direction) {
        boolean result = false;
        switch (direction) {
            case UP:
            case LEFT:
                result = thisValue > bestValue;
                break;
            case RIGHT:
            case DOWN:
                result = thisValue < bestValue;
                break;
        }
        return result;

    }

    public int rayEntities_getComparableValue(Rectangle rectangle, Direction direction){
        int result = 0;
        switch (direction) {
            case UP:
                result = rectangle.position.y + rectangle.size.y-1;
                break;
            case RIGHT:
                // we shooting ray to the right, so it hits left border of rectangle
                result = rectangle.position.x;
                break;
            case DOWN:
                result = rectangle.position.y;
                break;
            case LEFT:
                result = rectangle.position.x + rectangle.size.x-1;
                break;
        }
        return result;
    }

    public int rayEntities_getReturnValue(int bestValue, Point point, Direction direction) {
        int result = 0;
        switch (direction) {
            case UP:
                result = point.y-bestValue;
                break;
            case LEFT:
                result = point.x-bestValue;
                break;
            case RIGHT:
                result = bestValue-point.x;
                break;
            case DOWN:
                result = bestValue-point.y;
                break;
        }
        return result;
    }
    public int rayEntities(Entity entity, Direction direction) {

        Point[] angles = entity.getAnglesOfSide(direction);
        boolean first = true;
        int retval = -1, curval;
        for (int i = 0; i < 2; i++) {
            curval = rayEntities(angles[i].add(Point.fromDirection(direction)), direction);
            if(curval != -1) {
                if(first) {
                    retval = curval;
                    first = false;
                } else {
                    retval = Math.min(retval, curval);
                }
            }
        }

        return retval;


    }

    public int rayEntities(Point point, Direction direction) {
        boolean first = true;
        int bestValue = 0;
        int cmpval;
        Rectangle rectangle;
        for (int i = 0; i < world.heroes.size(); i++) {
            rectangle = world.heroes.get(i).getRectangle();

            if(  rayEntities_rectangleOnRay(point,rectangle,direction)   ) {
                cmpval = rayEntities_getComparableValue(rectangle, direction);
                if(first || rayEntities_isCloser(bestValue, cmpval, direction)) {
                    first = false;
                    bestValue = cmpval;
                }
            }
        }
        if (first) return -1;
        // this doesn't take into account the direction
        return rayEntities_getReturnValue(bestValue, point, direction);
    }


    private Direction getHorizontalFrom8way(int value){
        switch (value) {
            case 8 :
            case 7 :
            case 6 :
                return Direction.LEFT;

            case 2 :
            case 3 :
            case 4 :
                return Direction.RIGHT;
        }
        return Direction.NONE;
    }
    private Direction getVerticalFrom8way(int value){
        switch (value) {
            case 8 :
            case 1 :
            case 2 :
                return Direction.UP;
            case 6 :
            case 5 :
            case 4 :
                return Direction.DOWN;
        }
        return Direction.NONE;
    }

    public void moveFrom8way(Entity entity, int value) {

        String state = value!=0?"WALKING":"IDLE";
        int considerValue = value!=0?value:InputManager.getPrevious8way();
        String verticalAnimationWord =getVerticalFrom8way(considerValue).toString();
        String horizontalAnimationWord =getHorizontalFrom8way(considerValue).toString();


        if(value != 0){
            entity.facing = value;
            move(entity, getVerticalFrom8way(considerValue));
            move(entity, getHorizontalFrom8way(considerValue));
        }



        String animation = state;
        if(!Objects.equals(verticalAnimationWord, "NONE")) {
            animation += "_"+verticalAnimationWord;
        }
        if(!Objects.equals(horizontalAnimationWord, "NONE")) {
            animation += "_"+horizontalAnimationWord ;
        }
        if(Objects.equals(verticalAnimationWord, "NONE") && Objects.equals(horizontalAnimationWord, "NONE")) {
            animation = "default";
        }
        entity.setAnimation(animation);
    }

    public void move(Entity entity, Direction direction) {
        if(direction == Direction.NONE) return;

        int d = Math.min(ray(entity, direction), entity.movementSpeed);
        int d1 = rayEntities(entity, direction);
        if(d1 >= 0) {
            d = Math.min(d,d1);
        }


        if(d > 0) {
            if(entity == this.anchor) {
                movePoint(camera.position, direction, d);
            }
            movePoint(entity.position, direction, d);
        }


    }

    public ArrayList<Entity> queryEntities(Rectangle rectangle){
        ArrayList<Entity> list = new ArrayList<>();
        for (int i = 0; i < world.heroes.size(); i++) {
            if(world.heroes.get(i).getRectangle().intersects(rectangle)) {
                list.add(world.heroes.get(i));
            }
        }
        return list;

    }
    public void basicAttack(Entity character) {
        ArrayList<Entity> enemies = queryEntities(character.getBasicAttackArea());
        for (Entity enemy : enemies) {
            enemy.hp -= character.ad;
            if (enemy.hp <=0) {
                world.heroes.remove(enemy);
            }
            System.out.println(enemy.hp + "/" + enemy.max_hp + "hp");
        }
    }

}

/**
 *  8 1 2
 *  7 0 3
 *  6 5 4
 */
class InputManager {
    private static boolean W = false;
    private static boolean S = false;
    private static boolean A = false;
    private static boolean D = false;
    private static boolean space = false;

    public static int getPrevious8way() {
        return previous8way;
    }

    private static int previous8way = 0;
    private static int lastReturned8way = 0;

    public static int get8wayFromInput(){
        int horizontal;
        int vertical;
        int[][] values = {
                {8,1,2},
                {7,0,3},
                {6,5,4}
        };
        if(W == S) {
            vertical = 1;
        } else if(S) {
            vertical = 2;
        } else {
            vertical = 0;
        }

        if(A == D) {
            horizontal = 1;
        } else if (A) {
            horizontal = 0;
        } else {
            horizontal = 2;
        }

        if(lastReturned8way != values[vertical][horizontal]){
            previous8way = lastReturned8way;
            lastReturned8way = values[vertical][horizontal];
        }

        return values[vertical][horizontal];

    }
    public static Point getPointFrom8way(int value){
        Point point = new Point();
        switch (value) {
            case 8 :
            case 1 :
            case 2 :
                point.y = -1;
                break;
            case 6 :
            case 5 :
            case 4 :
                point.y = 1;
                break;

        }
        switch (value) {
            case 8 :
            case 7 :
            case 6 :
                point.x= -1;
                break;
            case 2 :
            case 3 :
            case 4 :
                point.x= 1;
                break;

        }
        return point;
    }
    public static Point getDirectionsFromInput(){
        int horizontal;
        int vertical;
        Direction[] directions = new Direction[2];
        if(W == S) {
            vertical = 0;
        } else if(S) {
            vertical = 1;
        } else {
            vertical = -1;
        }

        if(A == D) {
            horizontal = 0;
        } else if (A) {
            horizontal = -1;
        } else {
            horizontal = 1;
        }
        return new Point(horizontal,vertical);
    }
    public static void update() {
        W = Keyboard.isKeyDown(Keyboard.KEY_W);
        A = Keyboard.isKeyDown(Keyboard.KEY_A);
        S = Keyboard.isKeyDown(Keyboard.KEY_S);
        D = Keyboard.isKeyDown(Keyboard.KEY_D);
        space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);

    }
}
