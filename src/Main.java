import com.sun.org.apache.xpath.internal.operations.Mod;
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
    static int fps = 0;
    static Character villain;
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
        new ModelLoader("knight", knightJSON);
        Model heroModel = ModelLoader.get("knight");

        //setting up the characters

        Knight hero = new Knight(new Point(80,70));

        hero.model = heroModel;


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

            //camera.drawBorder(hero, Color.Green);
            //camera.drawBorder(villain, Color.Red);
            //camera.drawRectangle(hero.getBasicAttackArea(), Color.Blue);
            //System.out.println(controller.queryEntities(hero.getBasicAttackArea()).size());

            for (int i = 0; i < l.size(); i++) {
                Font.render("character "+i+":"+ l.get(i).HP + "/" + l.get(i).maxHP + " hp", new Point(160,16*(i+1)), 2, Color.Red);
            }



            // END DEBUG
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

                DrawShit.shittySquare(((0*8)+0)*4,((8)+1)*4,32,32, wasd.textures[1]);
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
