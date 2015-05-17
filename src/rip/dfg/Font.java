package rip.dfg;
import org.newdawn.slick.opengl.Texture;

import java.lang.*;

public class Font {
    public static Resources fnt = new Resources("res/font2.png", 8, 8);

    
    
    public static void render(String s, Point location, int scale, Color color) {
        Point position = location.same();
        for (int i = 0; i < s.length(); i++) {

            java.lang.Character character= s.charAt(i);
            Texture t = FontLoader.get(character);
            //int cp = s.codePointAt(i);
            //cp -= "A".codePointAt(0);
            DrawShit.shittySquare(position, fnt.size.mul(scale), t,color);
            position = position.add(fnt.size.onlyX().mul(scale));
        }
    }
}
