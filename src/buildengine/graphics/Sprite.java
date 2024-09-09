package buildengine.graphics;

import buildengine.core.scene.ActorComponent;
import buildengine.graphics.renderer.Animation;
import buildengine.math.vector.Vector2f;

import java.awt.image.BufferedImage;

/**
 * Sprite object containing drawing information for an Actor. It can hold either an image, an animation or is empty.
 */
public class Sprite extends ActorComponent {

    private Type type = Type.EMPTY;

    private BufferedImage staticImage;
    private Animation animation;

    private DrawMode drawMode = DrawMode.STRETCH;
    private float width = 1, height = 1;
    private int zIndex = 0;
    private float drawOpacity = 1;

    public Sprite() {}

    /**
     * Create a new Sprite component, matching the actor's size.
     * In case of repeat it repeats every unit.
     *
     * @param image    The frame images this sprite consists of
     */
    public Sprite(BufferedImage image) {
        this(image, DrawMode.STRETCH, 1, 1, 1);
    }

    public Sprite(Animation animation) {
        this(animation, DrawMode.STRETCH, 1, 1, 1);
    }

    /**
     * Create a new Sprite component using a still image.
     *
     * @param image    The image the sprite represents
     * @param width     The width of the drawn sprite relative to the actor (1 fills the whole actor)
     *                  In case of the REPEAT setting, this value represents the unit size of one image
     * @param height    The height of the drawn sprite relative to the actor (1 fills the whole actor)
     *                  In case of the REPEAT setting, this value represents the unit size of one image
     */
    public Sprite(BufferedImage image, DrawMode drawMode, float width, float height, float drawOpacity) {
        set(image);
        this.drawMode = drawMode;
        this.width = width;
        this.height = height;
        this.drawOpacity = drawOpacity;

        zIndex = 0;
    }

    /**
     * Create a new Sprite using an Animation
     *
     * @param animation The Animation the sprite represents
     * @param width     The width of the drawn sprite relative to the actor (1 fills the whole actor)
     *                  In case of the REPEAT setting, this value represents the unit size of one image
     * @param height    The height of the drawn sprite relative to the actor (1 fills the whole actor)
     *                  In case of the REPEAT setting, this value represents the unit size of one image
     */
    public Sprite(Animation animation, DrawMode drawMode, float width, float height, float drawOpacity) {
        set(animation);
        this.drawMode = drawMode;
        this.width = width;
        this.height = height;
        this.drawOpacity = drawOpacity;

        zIndex = 0;
    }

    // Methods

    public void set(BufferedImage image) {
        this.type = Type.STILL_IMAGE;
        this.staticImage = image;
    }

    public void set(Animation animation) {
        this.type = Type.ANIMATION;
        this.animation = animation;
    }

    public BufferedImage getCurrentDrawableImage() {
        switch (type) {
            case STILL_IMAGE -> {return staticImage;}
            case ANIMATION -> {return animation.getCurrentFrame();}
        }
        return null;
    }

    // Getters and Setters


    public Type getType() {
        return type;
    }

    public BufferedImage getStaticImage() {
        return staticImage;
    }

    public Animation getAnimation() {
        return animation;
    }

    public DrawMode getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Vector2f getSize() {
        return new Vector2f(width, height);
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }


    public float getDrawOpacity() {
        return drawOpacity;
    }

    public void setDrawOpacity(float drawOpacity) {
        this.drawOpacity = drawOpacity;
    }

    public enum Type {
        EMPTY, STILL_IMAGE, ANIMATION
    }
}
