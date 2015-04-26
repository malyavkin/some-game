import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

/**
 * Everything openGL related
 */
public class DrawShit {

    public static void shittySquare(Point position, Point size, Texture t) {
        shittySquare(position.x, position.y, size.x, size.y, t);
    }

    public static void shittySquare(Point position, Point size, Texture t, Color color) {
        glColor3b(color.r, color.g, color.b);
        shittySquare(position,size,t);
        glColor3b((byte) 127, (byte) 127, (byte) 127);
    }

    public static void shittySquare(int x, int y, int w, int h, Texture t) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        t.bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2i(x, y);
        glTexCoord2f(1, 0);
        glVertex2i(x + w, y);
        glTexCoord2f(1, 1);
        glVertex2i(x + w, y + h);
        glTexCoord2f(0, 1);
        glVertex2i(x,y+h);
        glEnd();
    }

    public static void shittySquare(int x, int y, int w, int h, Color color) {
        glDisable(GL_TEXTURE_2D);
        glColor3b(color.r, color.g, color.b);
        glBegin(GL_QUADS);
        glVertex2i(x, y);
        glVertex2i(x + w, y);
        glVertex2i(x + w, y + h);
        glVertex2i(x,y+h);
        glEnd();
        glColor3b((byte) 127, (byte) 127, (byte) 127);
        glEnable(GL_TEXTURE_2D);

    }

    public static void initgl() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 1280, 720, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);

        //transparent pngs
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT);
    }
}

class Color{
    byte r;
    byte g;
    byte b;

    public Color(byte r, byte g, byte b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color Red = new Color((byte)127,(byte)-128,(byte)-128);
    public static Color Green = new Color((byte)-128,(byte)127,(byte)-128);
    public static Color Blue = new Color((byte)-128,(byte)-128,(byte)127);
    public static Color White = new Color((byte)127,(byte)127,(byte)127);
}
