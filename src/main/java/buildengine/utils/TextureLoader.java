package buildengine.utils;

import buildengine.graphics.sprite.Texture;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class TextureLoader {

    private TextureLoader() {}

    /**
     * Loads a texture from an image. The image will be repeated and pixelated (not blurred).
     * @param path      The path of the image file
     * @return The newly created texture object containing id.
     */
    public static Texture load(String path) {
        return load(path, true, true);
    }

    /**
     * Loads a texture from an image
     * @param path      The path of the image file
     * @param repeat    Wether or not to repeat the image when wrapping
     * @param pixelate  Pixelate the image. {@code false} - to blur the image instead
     * @return The newly created texture object containing id.
     */
    public static Texture load(String path, boolean repeat, boolean pixelate) {
        if(!new File(path).exists()) {
            System.err.println("ERROR: Texture file '" + path + "' doesn't exist.");
            return null;
        }

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        ByteBuffer data = stbi_load(path, width, height, comp, 4);

        if(data == null) {
            System.err.println("ERROR: STB was unable to load the image '" + path + "'");
            return null;
        }

        int id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);

        if(repeat) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        }

        if(pixelate) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        }

        if(comp.get(0) == 3)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        else if(comp.get(0) == 4)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        else {
            System.err.println("ERROR: Unable to read image '" + path + "'. Image must have either 3 (RGB) or 4 (RGBA) channels. " +
                    "The image has " + comp.get(0) + ".");
            return null;
        }
        stbi_image_free(data);

        glBindTexture(GL_TEXTURE_2D, 0);
        return new Texture(id, width.get(0), height.get(0));
    }

}
