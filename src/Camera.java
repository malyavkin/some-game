import org.newdawn.slick.opengl.Texture;
import java.util.Collections;

public class Camera {
    public Point size;
    public Point position;
    private int tileZoom = 4;
    private Point stage;

    /**
     * Camera object. Camera object draws world
     * @param position position _relative_ to world origin
     * @param w width of viewport in display pixels
     * @param h height of viewport in display pixels
     * @param stage position of a camera _relative_ to opengl window
     */
    public Camera(Point position, int w, int h, Point stage) {
        this.size = new Point(w,h);
        //position of topleft corner of the camera relative to world origin in unscaled pixels
        this.position = position;

        //position of topleft corner relative to window in real pixels
        this.stage = stage;
    }

    /**
     * Tells you if rectangle is visible in this camera
     */


    private boolean isVisible(Rectangle rectangle) {
        Rectangle internal = new Rectangle(this.position, this.size.div(this.tileZoom));
        return internal.intersects(rectangle);
    }

    private Point toGlobal (Point relative) {
        return relative.sub(this.position).add(this.stage.div(this.tileZoom));
    }

    public void centerTo (Entity entity) {
        Point point = entity.position.add(entity.model.actual.size.div(2));
        centerTo(point);
    }

    public void centerTo (Point point){
        this.position = point.sub(this.size.div(tileZoom*2));
    }

    public void drawRectangle(Rectangle rectangle, Color color){
        for (int i = 0; i < 4; i++) {
            drawPoint(rectangle.getAngle(i), color);
        }
    }

    public void drawBorder(Entity entity, Color color){
        for (int i = 0; i < 4; i++) {
            drawPoint(entity.getAngle(i), color);
        }
    }
    public Point toLocal(Point point) {
        return  new Point();
    }
    public void drawPoint(Point point, Color color) {
        Point squareLocation;
        squareLocation = point.sub(this.position).mul(this.tileZoom).add(this.stage);
        DrawShit.shittySquare(squareLocation.x, squareLocation.y, this.tileZoom, this.tileZoom, color);

    }

    public void draw(World world){
        // tile position in the grid. NOT IN PIXELS. IN TILES;
        Point tilePos = new Point();
        // size of a tile in pixels
        Point real =world.map.theme.size.mul(this.tileZoom);

        Point squareLocation;
        Entity entity;

        for (int i = 0; i < world.map.levelData.length; i++) {
            tilePos.x = i%world.map.w;
            tilePos.y = (i-tilePos.x)/world.map.w;

            Rectangle tileRec = new Rectangle(tilePos.mul(world.map.theme.size),world.map.theme.size );

            boolean d = isVisible(tileRec);

            if(d) {
                squareLocation = tilePos.mul(world.map.theme.size).sub(this.position).mul(this.tileZoom).add(this.stage);
                DrawShit.shittySquare(squareLocation, real, world.map.theme.textures[world.map.levelData[i].type]);
            }
        }
        //player

        Collections.sort(world.heroes, (o1, o2) -> {

            Rectangle r1 = o1.getRectangle();
            Rectangle r2 = o2.getRectangle();
            return r1.position.y + r1.size.y - r2.position.y - r2.size.y;
        });
        for (int i = 0; i < world.heroes.size(); i++) {
            entity =world.heroes.get(i);

            // ?????????? ?????????? ???????? (???????? ?? origin ?????? ? ?????? ????????, ????? ? ?????? ?????????)
            squareLocation = toGlobal(entity.position.sub(entity.model.actual.position));
            // ???????
            squareLocation= squareLocation.mul(this.tileZoom);
            Texture t = entity.getCurrentTexture();
            DrawShit.shittySquare(squareLocation, entity.model.res.size.mul(this.tileZoom), t);

        }


    }

}