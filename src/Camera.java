import org.newdawn.slick.opengl.Texture;
import org.w3c.dom.css.Rect;

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
        return relative.sub(this.position).add(stage);
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

    public void drawPoint(Point point, Color color) {
        Point squareLocation;
        squareLocation = point.sub(this.position).mul(this.tileZoom).add(stage);
        DrawShit.shittySquare(squareLocation.x, squareLocation.y, this.tileZoom, this.tileZoom, color);

    }

    public void draw(World world){
        // tile position in the grid. NOT IN PIXELS. IN TILES;
        Point tilePos = new Point(0,0);
        // size of a tile in pixels
        Point real =world.map.theme.size.mul(this.tileZoom);

        Point squareLocation;


        for (int i = 0; i < world.map.lvldata.length; i++) {
            tilePos.x = i%world.map.w;
            tilePos.y = (i-tilePos.x)/world.map.w;

            Rectangle tileRec = new Rectangle(tilePos.mul(world.map.theme.size),world.map.theme.size );

            boolean d = isVisible(tileRec);

            if(d) {
                squareLocation = tilePos.mul(world.map.theme.size).sub(this.position).mul(this.tileZoom).add(stage);
                DrawShit.shittySquare(squareLocation, real, world.map.theme.textures[world.map.lvldata[i].type]);
            }
        }
        //player


        for (int i = 0; i < world.heroes.length; i++) {
            // ?????????? ?????????? ???????? (???????? ?? origin ?????? ? ?????? ????????, ????? ? ?????? ?????????)
            squareLocation = toGlobal(world.heroes[i].position.sub(world.heroes[i].model.actual.position));
            // ???????
            squareLocation= squareLocation.mul(this.tileZoom);
            Texture t = world.heroes[i].model.res.textures[world.heroes[i].getFacingTextureID(world.heroes[i].facing)];
            DrawShit.shittySquare(squareLocation, world.heroes[i].model.res.size.mul(this.tileZoom), t);

        }


    }
}
