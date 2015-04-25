import static org.lwjgl.opengl.GL11.*;


import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.LWJGLException;
import org.newdawn.slick.opengl.Texture;

import java.net.ResponseCache;


public class Main {

    static World world;
    static Camera camera;
    static long lastDate = System.currentTimeMillis();
    static int fps = 0;
    static Character hero;
    static Controller controller;
    static Resources wasd;

    public static void main(String[] args) {
        createDisplay();
        initgl();
        initStuff();
            gameLoop();
        quit();
    }

    private static void initStuff() {

        wasd = new Resources("res/wasd.png", 8, 8 );
        Resources hero_res = new Resources("res/knight_a.png", 32, 32 );
        Resources map = new Resources("res/hw.png", 8, 8);

        hero = new Knight(new Point(60,60));
        Knight villain = new Knight(new Point(60,100));

        hero.model = new Model(hero_res, new Point(8,8), new Point(12,13));
        villain.model = new Model(hero_res, new Point(8,8), new Point(12,13));

        world = new World(Map.generate(50,30), new Entity[]{hero, villain});
        world.map.theme = map;

        camera = new Camera(new Point(0,0), 1280, 720, new Point(0,0));

        controller = new Controller(world, camera);

    }

    private static void initgl() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 1280, 720, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);

        //transparent pngs
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    public static void createDisplay(){
        try {
            Display.setDisplayMode(new DisplayMode(1280,720));
            Display.setTitle("test title please ignore");
            Display.create();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }

    }
    public static void gameLoop(){
        while (!Display.isCloseRequested()){
            glClear(GL_COLOR_BUFFER_BIT);
            controller.draw();

            // DEBUG

            for (int i = 0; i < 4; i++) {
                camera.drawPoint(world, hero.getAngle(i));
            }

            // END DEBUG





            // direction
            if(Keyboard.isKeyDown(Keyboard.KEY_W)){
                controller.move(hero, Direction.UP);
                world.heroes[0].facing = Facing.UP;

                DrawShit.shittySquare(((1*8)+1)*4,0,32,32, wasd.textures[0]);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A)){
                controller.move(hero, Direction.LEFT);
                world.heroes[0].facing = Facing.LEFT;

                DrawShit.shittySquare(((0*8)+0)*4,((1*8)+1)*4,32,32, wasd.textures[1]);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S)){
                controller.move(hero, Direction.DOWN);
                world.heroes[0].facing = Facing.DOWN;
                DrawShit.shittySquare(((1*8)+1)*4,((2*8)+2)*4,32,32, wasd.textures[2]);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D)){
                controller.move(hero, Direction.RIGHT);
                world.heroes[0].facing = Facing.RIGHT;

                DrawShit.shittySquare(((2*8)+2)*4,((1*8)+1)*4,32,32, wasd.textures[3]);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                quit();
            }
            //fps++;
            //if(System.currentTimeMillis() - lastDate >= 1000) {
            //    lastDate = System.currentTimeMillis();
            //    System.out.println(fps + " fps");
            //    fps =0;
            //}
            Display.update();
            Display.sync(60);
        }
    }
    public static void quit(){
        Display.destroy();
        System.exit(0);
    }
}
