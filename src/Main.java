import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.LWJGLException;

public class Main {

    static World world;
    static Camera camera;
    static long lastDate = System.currentTimeMillis();
    static int fps = 0;
    static Character hero, villain;
    static Controller controller;
    static Resources wasd;

    static boolean space_pressed;
    public static void main(String[] args) {
        createDisplay();
        DrawShit.initgl();
        initStuff();
            gameLoop();
        quit();
    }

    private static void initStuff() {

        wasd = new Resources("res/wasd.png", 8, 8 );
        Resources hero_res = new Resources("res/knight_a.png", 32, 32 );
        Resources map = new Resources("res/hw.png", 8, 8);

        hero = new Knight(new Point(60,60));
        villain = new Knight(new Point(60,100));

        hero.model = new Model(hero_res, new Rectangle(12,13,8,8));
        villain.model = new Model(hero_res, new Rectangle(12,13,8,8));

        world = new World(Map.generate(50,30), new Entity[]{hero, villain});
        world.map.theme = map;

        camera = new Camera(new Point(0,0), 1280, 720, new Point(0,0));

        controller = new Controller(world, camera);
        controller.setAnchor(hero);

    }
    public static void createDisplay(){
        try {
            Display.setDisplayMode(new DisplayMode(1280,720));
            Display.setLocation(100,100);
            Display.setTitle("test title please ignore");
            Display.create();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
    }
    public static void gameLoop(){
        while (!Display.isCloseRequested()){
            DrawShit.clear();
            controller.draw();

            // DEBUG

            camera.drawBorder(hero, Color.Green);
            //camera.drawBorder(villain, Color.Red);
            camera.drawRectangle(hero.getBasicAttackArea(), Color.Blue);
            //System.out.println(controller.queryEntities(hero.getBasicAttackArea()).size());
            if (villain.HP<= 0) {
                Font.render("KILLED", new Point(100,100), 4, Color.Red);
            }



            // END DEBUG
            // direction
            if(Keyboard.isKeyDown(Keyboard.KEY_W)){
                controller.move(hero, Direction.UP);
                hero.facing = Direction.UP;
                // this stuff is just for testing, please ignore
                DrawShit.shittySquare((8+1)*4,0,32,32, wasd.textures[0]);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A)){
                controller.move(hero, Direction.LEFT);
                hero.facing = Direction.LEFT;

                DrawShit.shittySquare(((0*8)+0)*4,((8)+1)*4,32,32, wasd.textures[1]);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S)){
                controller.move(hero, Direction.DOWN);
                hero.facing = Direction.DOWN;
                DrawShit.shittySquare(((8)+1)*4,((2*8)+2)*4,32,32, wasd.textures[2]);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D)){
                controller.move(hero, Direction.RIGHT);
                hero.facing = Direction.RIGHT;

                DrawShit.shittySquare(((2*8)+2)*4,((8)+1)*4,32,32, wasd.textures[3]);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                if(!space_pressed) {
                    controller.basicAttack(hero);
                    space_pressed = true;
                }
                DrawShit.shittySquare(((3 * 8) + 3) * 4, ((8) +1)*4,32,32, wasd.textures[4]);
            } else {
                space_pressed = false;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                quit();
            }
            fps++;
            if(System.currentTimeMillis() - lastDate >= 1000) {
                lastDate = System.currentTimeMillis();
                System.out.println(fps + " fps");
                fps =0;
            }
            Display.update();
            Display.sync(60);


        }
    }
    public static void quit(){
        Display.destroy();
        System.exit(0);
    }
}
