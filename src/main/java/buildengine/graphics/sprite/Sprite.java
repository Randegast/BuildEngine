package buildengine.graphics.sprite;

import org.joml.Vector2f;

/**
 * Sprite holding a Texture object, and corresponding UV-coordinates
 * (cropping form an image) in normalized coordinates.
 */
public class Sprite {

    /** OpenGL Texture object */
    private final Texture texture;
    /** UV coordinates, normalized */
    private final Vector2f[] uvCoordinates;

    /**
     * Create a new Sprite object with the full image
     * @param texture the texture image
     */
    public Sprite(Texture texture) {
        this(texture, new Vector2f[] {
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1)
        });
    }

    /**
     * Create a new Sprite object
     * @param texture the texture image
     * @param uvCoordinates the cropped image
     */
    public Sprite(Texture texture, Vector2f[] uvCoordinates) {
        this.texture = texture;
        this.uvCoordinates = uvCoordinates;
    }

    /**
     * Get the texture image object
     * @return the texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * The UV coordinates used to crop the image
     * @return the normalized UV coordinates
     */
    public Vector2f[] getUvCoordinates() {
        return uvCoordinates;
    }
}
