package buildengine.graphics.sprite;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * A SpriteMask object containing one texture and multiple Sprites derived from that texture
 */
public class SpriteSheet {

    /** The sheet for sprites to get cropped off */
    private final Texture sheet;
    /** The list of cropped sprites */
    private final List<Sprite> sprites;

    /**
     * Create a new SpriteSheet using a pixel grid
     * @param sheet The SpriteSheet image
     * @param spriteSize The size of one image in pixels
     * @param spriteCount The amount of sprites
     * @param padding The extra empty space to be removed
     */
    public SpriteSheet(Texture sheet, Vector2i spriteSize, int spriteCount, Vector2i padding) {
        this.sheet = sheet;
        sprites = new ArrayList<>();

        if(sheet == null || spriteSize == null || spriteCount < 1)
            return;

        if(padding == null)
            padding = new Vector2i();

        int currentX = 0, currentY = sheet.getHeight() - spriteSize.y;

        for (int i = 0; i < spriteCount; i++) {
            float topY = (currentY + spriteSize.y) / (float) sheet.getHeight();
            float rightX = (currentX + spriteSize.x) / (float) sheet.getWidth();
            float leftX = currentX / (float) sheet.getWidth();
            float bottomY = currentY / (float) sheet.getHeight();

            Vector2f[] textCoords = {
                    new Vector2f(leftX, bottomY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(rightX, topY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(sheet, textCoords);
            sprites.add(sprite);

            currentX += spriteSize.x + padding.x;
            if(currentX >= sheet.getWidth()) {
                currentX = 0;
                currentY -= spriteSize.y + padding.y;
            }
        }
    }

    /**
     * Gets a sprite from the list by index
     * @param index The index of the sprite
     * @return The sprite of the given index
     */
    public Sprite getSprite(int index) {
        return sprites.get(index);
    }

    /**
     * Gets the sprite sheet image
     * @return The sprite sheet image
     */
    public Texture getSheet() {
        return sheet;
    }
}
