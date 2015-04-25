import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

public class DrawShit {

    public static void shittySquare(Point position, Point size, Texture t) {
        shittySquare(position.x, position.y, size.x, size.y, t);
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

    public static void shittySquare(int x, int y, int w, int h, byte[] rgb) {
        glDisable(GL_TEXTURE_2D);
        glColor3b(rgb[0], rgb[1], rgb[2]);
        glBegin(GL_QUADS);
        glVertex2i(x, y);
        glVertex2i(x + w, y);
        glVertex2i(x + w, y + h);
        glVertex2i(x,y+h);
        glEnd();
        glColor3b((byte)127,(byte)127,(byte)127);
        glEnable(GL_TEXTURE_2D);

    }

}
