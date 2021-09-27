package buildengine.utils;

import buildengine.graphics.Shader;
import buildengine.graphics.sprite.SpriteSheet;
import buildengine.graphics.sprite.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    public static final boolean CRASH_ON_LOAD_FAIL = true;
    public static final String WORKING_DIR = System.getProperty("user.dir") + "/";

    private static final Map<String, Texture> textureMap = new HashMap<>();
    private static final Map<String, Shader> shaderMap = new HashMap<>();

    // Saving pool
    private static final Map<String, SpriteSheet> spriteSheetMap = new HashMap<>();

    public static Texture getTexture(String path) {
        path = WORKING_DIR + path;
        File file = new File(path);
        if(textureMap.containsKey(file.getAbsolutePath()))
            return textureMap.get(file.getAbsolutePath());
        Texture texture = TextureLoader.load(path);
        if(texture == null && CRASH_ON_LOAD_FAIL)
            throw new IllegalStateException("FATAL ERROR: Failed to load essential resource.");
        textureMap.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static Shader getShader(String path) {
        path = WORKING_DIR + path;
        File file = new File(path);
        if(shaderMap.containsKey(file.getAbsolutePath()))
            return shaderMap.get(file.getAbsolutePath());
        Shader shader = ShaderLoader.load(path);
        if(shader == null && CRASH_ON_LOAD_FAIL)
            throw new IllegalStateException("FATAL ERROR: Failed to load essential resource.");
        shaderMap.put(file.getAbsolutePath(), shader);
        return shader;
    }

    // Spritesheets
    public static void createSpriteSheet(String key, SpriteSheet spriteSheet) {
        if(key == null || key.isBlank() || spriteSheet == null || spriteSheet.getSheet() == null)
            throw new IllegalArgumentException("Failed to create sprite sheet: key or sprite sheet is null");
        if(spriteSheetMap.containsKey(key) && spriteSheetMap.get(key) != spriteSheet)
            throw new IllegalStateException("Failed to create sprite sheet: sprite sheet with the same name already exists.");
        spriteSheetMap.put(key, spriteSheet);
    }

    public static SpriteSheet getSpriteSheet(String key) {
        SpriteSheet spriteSheet = spriteSheetMap.get(key);
        if(spriteSheet == null)
            throw new IllegalArgumentException("SpriteSheet '" + key + "' doesn't exist. Create one first.");
        return spriteSheet;
    }
}
