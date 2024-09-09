package buildengine.io;

import buildengine.audio.AudioClip;
import buildengine.configuration.ConfigurationFile;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AssetReader {

    public static final String WORK_DIR = System.getProperty("user.dir") + "/";

    public static BufferedImage readImage(String path) {
        try {
            return ImageIO.read(new File(WORK_DIR + path));
        } catch (IOException e) {
            System.err.println("Warning: Couldn't read image file[" + path + "]: " + e.getMessage());
            return null;
        }
    }

    public static AudioClip readAudioClip(String path) {
        try {
            return new AudioClip(AudioSystem.getAudioInputStream(new File(WORK_DIR + path)));
        } catch (UnsupportedAudioFileException | IOException e) {
            System.err.println("Warning: Couldn't read audio file[" + path + "]: " + e.getMessage());
            return null;
        }
    }

    public static ConfigurationFile readDataFile(String path) {
        File file = new File(WORK_DIR + path);
        if(!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error while creating a new data file: " + e.getMessage());
            }
        }
        return new ConfigurationFile(file);
    }

    public static Font readFont(String path, int size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(WORK_DIR + path)).deriveFont((float) size);
        } catch (FontFormatException | IOException e) {
            System.err.println("Warning: Couldn't read audio file[" + path + "]: " + e.getMessage());
            return new Font("Corrier New", Font.PLAIN, size);
        }
    }

}
