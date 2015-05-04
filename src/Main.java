import org.json.simple.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.LWJGLException;
import java.util.ArrayList;


public class Main {

    static World world;
    static Camera camera;
    static long lastDate = System.currentTimeMillis();
    static long lastDate2 = System.currentTimeMillis();
    static int fps = 0;
    static int last_fps;
    static long last_ms;


    static Controller controller;
    static Resources wasd;
    static ArrayList<Entity> l;
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
        // font
        JSONObject fontJSON= ConfigLoader.getJSON("res/font.json");
        new FontLoader(fontJSON);
        // loading knight model
        JSONObject knightJSON = ConfigLoader.getJSON("res/knight.json");
        new ModelLoader("knight", (JSONObject)knightJSON.get("model"));
        new StatLoader("knight",  (JSONObject)knightJSON.get("stats"));
        Model heroModel = ModelLoader.get("knight");
        //setting up the characters
        Knight hero = new Knight(new Point(80,70));
        hero.model = heroModel;
        Character villain;
        // creating stage
        l = new ArrayList<>() ;
        l.add(hero);
        for (int i = 0; i < 5; i++) {
            villain = new Knight(new Point(30+15*i,50));
            villain.model = heroModel;
            l.add(villain);
        }

        Resources map = new Resources("res/hw.png", 8, 8);
        world = new World(Map.generate(50,30), l);
        world.map.theme = map;

        // creating view

        camera = new Camera(new Point(0,0), 1280, 720, new Point(0,0));

        // finalizing

        controller = new Controller(world, camera);
        controller.setAnchor(hero);
        camera.centerTo(hero);
    }
    public static void createDisplay(){
        try {
            Display.setDisplayMode(new DisplayMode(1280,720));
            Display.setLocation(100, 100);
            Display.setTitle("test title please ignore");
            Display.create();
            Display.setVSyncEnabled(true);
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
    }

    public static void KeyboardLogic() {
        // direction
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            controller.move(controller.getAnchor(), Direction.UP);
            controller.getAnchor().facing = Direction.UP;
            // this stuff is just for testing, please ignore
            DrawShit.shittySquare((8+1)*4,0,32,32, wasd.textures[0]);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            controller.move(controller.getAnchor(), Direction.LEFT);
            controller.getAnchor().facing = Direction.LEFT;

            DrawShit.shittySquare(0,((8)+1)*4,32,32, wasd.textures[1]);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            controller.move(controller.getAnchor(), Direction.DOWN);
            controller.getAnchor().facing = Direction.DOWN;
            DrawShit.shittySquare(((8)+1)*4,((2*8)+2)*4,32,32, wasd.textures[2]);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            controller.move(controller.getAnchor(), Direction.RIGHT);
            controller.getAnchor().facing = Direction.RIGHT;

            DrawShit.shittySquare(((2*8)+2)*4,((8)+1)*4,32,32, wasd.textures[3]);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            if(!space_pressed) {
                controller.basicAttack(controller.getAnchor());
                space_pressed = true;
            }
            DrawShit.shittySquare(((3 * 8) + 3) * 4, ((8) +1)*4,32,32, wasd.textures[4]);
        } else {
            space_pressed = false;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            quit();
        }
    }

    public static void fpsCounter(){
        fps++;
        last_ms = System.currentTimeMillis() - lastDate2;
        if(System.currentTimeMillis() - lastDate >= 1000) {
            lastDate = System.currentTimeMillis();
            last_fps = fps;
            fps =0;
        }
    }

    public static void gameLoop(){
        while (!Display.isCloseRequested()){
            lastDate2 = System.currentTimeMillis();
            DrawShit.clear();
            controller.draw();
            camera.drawBorder(controller.getAnchor(), Color.Blue);

            KeyboardLogic();

            Font.render("animation:"+ controller.getAnchor().currentAnimation.name , new Point(160, 16), 2, Color.Red);
            Font.render("current_frame:"+ controller.getAnchor().currentFrame, new Point(160, 32), 2, Color.Red);
            Font.render("time:"+ controller.getAnchor().time, new Point(160, 48), 2, Color.Red);
            Font.render("animation duration:"+ controller.getAnchor().animationDuration, new Point(160, 64), 2, Color.Red);


            Font.render(last_fps + " fps ("+last_ms+" ms)", new Point(0, 704), 2, Color.Green);
            fpsCounter();
            Display.update();

        }
    }
    public static void quit(){
        Display.destroy();
        System.exit(0);
    }
}
