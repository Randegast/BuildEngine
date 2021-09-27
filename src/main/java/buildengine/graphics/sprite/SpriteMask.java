package buildengine.graphics.sprite;

import buildengine.core.scene.ActorComponent;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * SpriteMask Component
 *
 * Holds information on what sprite, or color to render. The anchor variable
 * tells the renderer how to center the sprite.
 *
 * @see ActorComponent
 */
public class SpriteMask extends ActorComponent {

    private static Vector2f DEFAULT_ANCHOR = new Vector2f(0.5f, 0.5f);
    public static void setDefaultAnchor(Vector2f anchor) {DEFAULT_ANCHOR = anchor;}

    private Sprite sprite;
    private Vector4f colour;
    private Vector2f anchor;
    private int zIndex;

    /** When a changes have occurred */
    private boolean dirty;

    public SpriteMask(Vector4f colour) {
        this(colour, 0);
    }

    public SpriteMask(Vector4f colour, int zIndex) {
        this(new Sprite(null), zIndex);
        this.colour = colour;
    }

    public SpriteMask(Sprite sprite) {
        this(sprite, 0);
    }

    public SpriteMask(Sprite sprite, int zIndex) {
        this.sprite = sprite;
        this.colour = new Vector4f(1);
        this.zIndex = zIndex;
        this.anchor = DEFAULT_ANCHOR;
        this.dirty = true;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.dirty = true;
    }

    public Vector4f getColour() {
        return colour;
    }

    public void setColour(Vector4f colour) {
        this.colour.set(colour);
        this.dirty = true;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public Vector2f getAnchor() {
        return anchor;
    }

    public void setAnchor(Vector2f anchor) {
        this.anchor = anchor;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
