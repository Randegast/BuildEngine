package buildengine.game.physics;

import buildengine.core.scene.Actor;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.ExecutionPhase;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.math.vector.Vector2f;

import java.util.List;

/**
 * Physics implementation for a scene. The update has to be POST, so all corrections are done after the other collision tasks.
 */
public class CollisionCorrectionDirector extends Director implements MonoBehaviour {

    private CollisionRegisterDirector registerDirector;

    public CollisionCorrectionDirector() {
        executionPhase = ExecutionPhase.POST;
    }

    @Override
    public void begin() {
        registerDirector = scene.getDirector(CollisionRegisterDirector.class);
        if(registerDirector == null)
            throw new IllegalStateException("Scene doesn't contain a CollisionRegisterDirector.");
    }

    @Override
    public void update() {}

    @Override
    public void fixedUpdate() {
        if(registerDirector == null)
            return;
        registerDirector.registerCollisions();
        correctCollisions();
    }

    private void correctCollisions() {
        for(Actor actor : registerDirector.getAllCollisions().keySet()) {
            List<CollisionData> actorCollisionData = registerDirector.getCollisions(actor);
            if(actorCollisionData == null || actorCollisionData.isEmpty())
                continue;
            RigidBody rigidBody = actor.getComponent(RigidBody.class);
            if(rigidBody == null || rigidBody.isImmovable())
                continue;
            Vector2f displacement = new Vector2f();
            for(CollisionData collisionData : actorCollisionData) {
                RigidBody otherBody = collisionData.getActor().getComponent(RigidBody.class);
                if(otherBody == null)
                    return;
                float correction = otherBody.isImmovable() ? 1f : 0.5f;
                displacement.add(collisionData.getContact().getNormal().duplicate().mul(collisionData.getContact().getPenetration() * correction));
            }
            actor.getTransform().getPosition().add(displacement);
        }
    }
}
