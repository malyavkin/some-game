package rip.dfg;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Model {
    Resources res;
    Rectangle actual;
    Map<String, Animation> animations = new HashMap<>();

    //animation state


    /**
     * Model constructor.
     * Model is responsible for visual appearance of the entity and its geometry
     * @param res Textures of the model
     * @param actual position and size of the model relative to (0,0) of texture
     */
    public Model(Resources res, Rectangle actual){
        this.res = res;
        this.actual = actual;
    }
    //public initAnimations


}

public class Resources {
    public Texture[] textures;
    public Point size = new Point();

    private BufferedImage[] sliceSprites(BufferedImage img, int w, int h) {

        int nSpritesX, nSpritesY;
        nSpritesX = (img.getWidth()/w);
        nSpritesY = (img.getHeight()/h);
        BufferedImage[] out = new BufferedImage[nSpritesX*nSpritesY];
        int ptr =0;
        for (int i = 0; i < nSpritesY; i++) {
            for (int j = 0; j < nSpritesX; j++) {
                out[ptr++] = img.getSubimage(j*w, i*h, w, h);
            }
        }
        return out;
    }


    public Resources(String filename, int spriteW, int spriteH) {
        this.size.y = spriteH;
        this.size.x = spriteW;
        try {
            BufferedImage[] loadedImages;
            BufferedImage img;
            img = ImageIO.read(new File(filename));
            loadedImages = sliceSprites(img, spriteW, spriteH);




            textures = new Texture[loadedImages.length];
            for (int i = 0; i < loadedImages.length; i++) {
                textures[i]= BufferedImageUtil.getTexture("texture", loadedImages[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
