public class Camera {
    public int width;
    public int height;
    public Point position;
    private int tileZoom = 3;
    private Point stage;

    /**
     * Camera object. Camera object draws world
     * @param position position _relative_ to world origin
     * @param w width of viewport in display pixels
     * @param h height of viewport in display pixels
     * @param stage position of a camera _relative_ to opengl window
     */
    public Camera(Point position, int w, int h, Point stage) {
        this.width = w;
        this.height = h;
        //position of topleft corner of the camera relative to world origin in unscaled pixels
        this.position = position;

        //position of topleft corner relative to window in real pixels
        this.stage = stage;
    }

    /**
     * Tells you if rectangle is visible in this camera
     */
    // todo: BROKEN

    private boolean isVisible(int x, int y, int w, int h) {
        //todo:"fucking broke"
        //throw "fucking broke"

        int internalWidth = this.width/ this.tileZoom;
        int internalHeight = this.height/ this.tileZoom;

        boolean a,b,c,d;
        a = (x >= this.position.x && x <= this.position.x+ internalWidth);
        b = (x+w >= this.position.x && x+w <= this.position.x+ internalWidth);
        c = (y >= this.position.y && y <= this.position.y+ internalHeight);
        d = (y+h >= this.position.y && y+h <= this.position.y+ internalHeight);
        return (a|b) & (c|d);
    }

    private Point toGlobal (Point relative) {
        return relative.sub(this.position).add(stage);
    }

    public void drawPoint(World world, Point point) {
        Point squareLocation;
        squareLocation = point.sub(this.position).mul(this.tileZoom).add(stage);
        DrawShit.shittySquare(squareLocation.x, squareLocation.y, this.tileZoom, this.tileZoom, new byte[]{0,127,0});

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
            boolean d = isVisible(tilePos.x*world.map.theme.size.x,
                    tilePos.y*world.map.theme.size.y,
                    world.map.theme.size.x,
                    world.map.theme.size.y);

            if(d) {
                squareLocation = tilePos.mul(world.map.theme.size).sub(this.position).mul(this.tileZoom).add(stage);
                DrawShit.shittySquare(squareLocation, real, world.map.theme.textures[world.map.lvldata[i].type]);
            }
        }
        //player


        for (int i = 0; i < world.heroes.length; i++) {
            // ?????????? ?????????? ???????? (???????? ?? origin ?????? ? ?????? ????????, ????? ? ?????? ?????????)
            squareLocation = toGlobal(world.heroes[i].position.sub(world.heroes[i].model.origin));
            // ???????
            squareLocation= squareLocation.mul(this.tileZoom);
            DrawShit.shittySquare(squareLocation, world.heroes[i].model.res.size.mul(this.tileZoom), world.heroes[i].model.res.textures[world.heroes[i].facing]);

        }


    };
}
