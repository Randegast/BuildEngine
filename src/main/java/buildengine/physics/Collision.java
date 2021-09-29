package buildengine.physics;

import buildengine.physics.components.BoxCollider;
import org.joml.Vector2f;

/**
 * Contains collision information
 */
public class Collision {

    private final BoxCollider with;
    private final Vector2f correction;

    public Collision(BoxCollider with, Vector2f correction) {
        this.with = with;
        this.correction = correction;
    }

    /**
     * Returns the collider who is collided with
     * @return the collider who is collided with
     */
    public BoxCollider getWith() {
        return with;
    }

    public Vector2f getCorrection() {
        return correction;
    }
}
