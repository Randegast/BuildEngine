package buildengine.core.physics;

import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.ExecutionPhase;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.imgui.element.Debug;
import buildengine.math.collision.Collisions;
import buildengine.math.collision.Contact;
import buildengine.core.physics.components.BoxCollider;
import org.joml.Vector2f;

/**
 * Director responsible for registering collisions of colliders in a scene.
 *
 * @see BoxCollider
 */
public class CollisionRegisterer extends Director implements MonoBehaviour {

    public CollisionRegisterer() {
        executionPhase = ExecutionPhase.PRE;
    }

    @Override
    public void begin() {}

    @Override
    public void update(float dt) {
        registerCollisions();
    }

    public void registerCollisions() {
        for(BoxCollider collider : scene.getComponents(BoxCollider.class))
            registerCollisions(collider);
    }

    private void registerCollisions(BoxCollider collider) {
        collider.getCollisions().clear();
        for(BoxCollider other : scene.getComponents(BoxCollider.class)) {
            if(collider == other)
                continue;
            Contact contact = Collisions.compareRectangle(collider.getAbsoluteBounds(),
                    other.getAbsoluteBounds());
            if(contact == null)
                return;
            // There is collision
            Vector2f correction = contact.getNormal().get(new Vector2f()).mul(contact.getPenetration());
            Collision collision = new Collision(other, correction);
            collider.getCollisions().add(collision);
            for(CollideEvent event : collider.getEvents())
                event.collide(collision);
        }
    }
}
