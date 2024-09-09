package buildengine.graphics;

import buildengine.graphics.renderer.Animation;
import buildengine.io.AssetReader;

import java.awt.image.BufferedImage;

/**
 * Static utility for Sprite and BufferedImage[] loading.
 */
public class SpriteLoader {

    private SpriteLoader() {}

    /**
     * Loads sprite sheet from an image
     * @param imagePath The image file containing the sprite sheet
     * @param width     The width of one frame
     * @param height    The height of one frame
     * @return A new BufferedImage array object
     */
    public static BufferedImage[][] loadSpriteSheet(String imagePath, int width, int height) {
        BufferedImage image = AssetReader.readImage(imagePath);
        if(image == null)
            throw new IllegalArgumentException("SpriteSheet could not be generated because the image is null.");
        BufferedImage[][] frames = new BufferedImage[image.getHeight() / height][image.getWidth() / width];
        for(int i = 0; i < frames.length; i++)
            for(int j = 0; j < frames[i].length; j++)
                frames[i][j] = image.getSubimage(j * width, i * height, width, height);
        return frames;
    }

    /**
     * Loads frames from an image using the height of the image
     * @param imagePath The image file containing frames
     * @param width     The width of one frame
     * @return A new BufferedImage array object
     */
    public static BufferedImage[] loadFrames(String imagePath, int width) {
        BufferedImage image = AssetReader.readImage(imagePath);
        if(image == null)
            throw new IllegalArgumentException("Frames could not be generated because the image is null.");
        BufferedImage[] frames = new BufferedImage[image.getWidth() / width];
        for(int i = 0; i < frames.length; i++)
            frames[i] = image.getSubimage(i * width, 0, width, image.getHeight());
        return frames;
    }

    /**
     * Loads frames from an image
     * @param imagePath The image file containing frames
     * @param width     The width of one frame
     * @param height    The height of one frame
     * @return A new BufferedImage array object
     */
    public static BufferedImage[] loadFrames(String imagePath, int width, int height) {
        BufferedImage image = AssetReader.readImage(imagePath);
        if(image == null)
            throw new IllegalArgumentException("Frames could not be generated because the image is null.");
        BufferedImage[] frames = new BufferedImage[image.getWidth() / width];
        for(int i = 0; i < frames.length; i++)
            frames[i] = image.getSubimage(i * width, 0, width, height);
        return frames;
    }

    /**
     * Loads a sprite from an image
     * @param imagePath The image file containing a sprite
     * @param width     The width of one frame
     * @return A new sprite object
     */
    public static Sprite loadSprite(String imagePath, int width) {
        return new Sprite(new Animation(100L, loadFrames(imagePath, width)));
    }

}
