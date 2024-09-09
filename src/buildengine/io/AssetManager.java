package buildengine.io;

import buildengine.audio.AudioClip;
import buildengine.audio.SoundEffect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static buildengine.io.AssetReader.readAudioClip;
import static buildengine.io.AssetReader.readImage;

/**
 * This class allows you to load assets and automatically saves it in cache.
 *
 * @author Kai van Maurik
 */
public class AssetManager {

    // Cache
    private static final HashMap<String, BufferedImage> images = new HashMap<>();
    private static final HashMap<String, AudioClip> audio = new HashMap<>();

    private AssetManager() {}

    /**
     * Gets an image, either from cache by name, or from disk by path.
     * @param path The name of the preloaded image, or the path to the newly loaded image.
     */
    public static BufferedImage getImage(String path) {
        if(!images.containsKey(path))
            loadImage(path, path);
        return images.get(path);
    }

    /**
     * Gets an audio clip, either from cache by name, or from disk by path.
     * @param path The name of the preloaded audio clip, or the path to the newly loaded audio clip.
     */
    public static AudioClip getAudio(String path) {
        if(!audio.containsKey(path))
            loadAudio(path, path);
        return audio.get(path);
    }

    /**
     * Load and store an image into cache, by name.
     * @param name The name the image can be referenced to.
     * @param path The path to the image to be loaded.
     */
    private static void loadImage(String name, String path) {
        images.put(name, readImage(path));
    }

    /**
     * Load and store an audio clip into cache, by name.
     * @param name The name the audio clip can be referenced to.
     * @param path The path to the audio to be loaded.
     */
    private static void loadAudio(String name, String path) {
        audio.put(name, readAudioClip(path));
    }

    /**
     * Load an image array from a row in a sprite sheet
     * @param path The pathOrName to the sprite sheet
     * @param frameDimension The dimension of one frame
     * @param startRow The start row (0 if you have just one row)
     * @return Get the animation object from a sprite sheet
     */
    public static BufferedImage[] loadImageArray(String path, Dimension frameDimension, int startRow, int frameCount) {
        BufferedImage spriteSheet = getImage(path);

        BufferedImage[] anim = new BufferedImage[frameCount];
        for(int i = 0; i < frameCount; i++) {
            anim[i] = spriteSheet.getSubimage(i * frameDimension.width, startRow * frameDimension.height,
                    frameDimension.width, frameDimension.height);
        }

        return anim;
    }

    public static SoundEffect loadSoundEffect(String... names) {
        AudioClip[] list = new AudioClip[names.length];
        for(int i = 0; i < names.length; i++) {
            list[i] = getAudio(names[i]);
        }
        return new SoundEffect(list);
    }
}
