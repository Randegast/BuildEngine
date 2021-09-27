package buildengine.core.physics;

import buildengine.core.physics.components.BoxCollider;
import buildengine.core.physics.components.RigidBody;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.ExecutionPhase;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.imgui.element.Debug;
import org.joml.Vector2f;

/**
 * Physics implementation for a scene
 */
public class CollisionResolver extends Director implements MonoBehaviour {

    public CollisionResolver() {
        executionPhase = ExecutionPhase.POST;
    }

    @Override
    public void begin() {}

    @Override
    public void update(float dt) {}

    @Override
    public void fixedUpdate() {
        CollisionRegisterer registerer = scene.getDirector(CollisionRegisterer.class);
        if(registerer == null)
            return;
        registerer.registerCollisions();
        for(RigidBody rigidBody : scene.getComponents(RigidBody.class))
            resolveCollision(rigidBody);
    }

    private void resolveCollision(RigidBody rigidBody) {
        if(rigidBody.isImmovable())
            return;
        BoxCollider collider = rigidBody.getOwner().getComponent(BoxCollider.class);
        if(collider == null || collider.getCollisions().isEmpty())
            return;
        Vector2f displacement = new Vector2f();
        for(Collision collision : collider.getCollisions())
            if(collision.getWith().getOwner().getComponent(RigidBody.class) != null)
                displacement.add(collision.getCorrection());
        rigidBody.getOwner().getTransform().getPosition().add(displacement);
    }
}
