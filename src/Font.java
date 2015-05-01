import org.newdawn.slick.opengl.Texture;

import java.lang.*;
import java.lang.Character;

public class Font {
    public static Resources fnt = new Resources("res/font2.png", 8, 8);

    
    
    public static void render(String s, Point location, int scale, Color color) {
        Point coords = location.same();
        for (int i = 0; i < s.length(); i++) {

            java.lang.Character character= new java.lang.Character(s.charAt(i));
            Texture t = FontLoader.get(character);
            //int cp = s.codePointAt(i);
            //cp -= "A".codePointAt(0);
            DrawShit.shittySquare(coords, fnt.size.mul(scale), t,color);
            coords = coords.add(fnt.size.onlyX().mul(scale));
        }
    }
}
