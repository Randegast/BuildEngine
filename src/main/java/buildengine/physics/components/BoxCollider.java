package buildengine.physics.components;

import buildengine.physics.CollideEvent;
import buildengine.physics.Collision;
import buildengine.physics.CollisionRegisterer;
import buildengine.core.scene.ActorComponent;
import buildengine.math.Transform;
import buildengine.math.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * BoxCollider Component
 *
 * Component required for registering collisions.
 * @see ActorComponent
 * @see CollisionRegisterer
 */
public class BoxCollider extends Collider {

    /** The Rectangle bounds relative to the Actor */
    private final Rectangle bounds;
    /** The list of collisions active at this time (updated by CollisionRegisterer) */
    private final List<Collision> collisions;

    /**
     * Create a new BoxCollider Component, and adding collide event listeners.
     * @param bounds The Rectangle bounds relative to the Actor
     * @param listeners The Collide Event listeners used for functionality
     */
    public BoxCollider(Rectangle bounds, CollideEvent... listeners) {
        super(listeners);
        this.bounds = bounds;
        this.collisions = new ArrayList<>();
    }

    /**
     * Gets absolute bounds, using the current transform position as center, and
     * adding the relative bounds
     * @return The absolute bounds
     */
    public Rectangle getAbsoluteBounds() {
        Transform transform = owner.getTransform();
        return new Rectangle(transform.getPosition().x - bounds.getWidth() / 2 + bounds.x,
                transform.getPosition().y - bounds.getHeight() / 2 + bounds.y,
                bounds.getWidth(), bounds.getHeight(),
                transform.getRotation() + bounds.getRotation());
    }

    /**
     * Gets the relative bounds
     * @return The relative bounds
     */
    public Rectangle getRelativeBounds() {
        return bounds;
    }

    /**
     * Gets the list of current collisions (updated by CollisionRegisterer)
     * @see CollisionRegisterer
     * @return The list of current collisions
     */
    public List<Collision> getCollisions() {
        return collisions;
    }

}
