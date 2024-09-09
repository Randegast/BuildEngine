package buildengine.game.physics;

import buildengine.core.scene.Actor;
import buildengine.core.scene.Scene;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.ExecutionPhase;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.math.collision.CollisionTester;
import buildengine.math.collision.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Director responsible for registering collisions of actors in a scene.
 * It does so on the beginning of an update cycle, so all directors can work with the up-to-date info.
 *
 * @see Actor
 * @see Scene
 */
public class CollisionRegisterDirector extends Director implements MonoBehaviour {


    private HashMap<Actor, List<CollisionData>> collisions;

    public enum Mode {ALL_ACTORS, COLLISION_BOX}
    private Mode mode;

    public CollisionRegisterDirector(Mode mode) {
        this.mode = mode;
        this.collisions = new HashMap<>();

        executionPhase = ExecutionPhase.POST;
    }

    @Override
    public void begin() {

    }

    @Override
    public void update() {
        registerCollisions();
    }

    public void registerCollisions() {
        collisions = new HashMap<>();
        switch (mode) {
            case ALL_ACTORS -> {
                for(Actor actor : scene.getActiveActors())
                    registerActors(actor);
            }
            case COLLISION_BOX -> {
                for(CollisionBox cb : scene.getActiveComponents(CollisionBox.class))
                    registerCollisions(cb);
            }
        }
    }

    private void registerActors(Actor actor) {
        collisions.put(actor, new ArrayList<>());
        for(Actor other : scene.getActiveActors()) {
            if(actor == other || other == null)
                continue;
            Contact contact = CollisionTester.compareRectangle(actor.getCollisionBounds(), other.getCollisionBounds());
            if(contact == null)
                continue;
            // There is collision
            CollisionData collisionData = new CollisionData(other, contact);
            for(CollideEvent collideEvent : actor.getCollideEvents())
                collideEvent.collide(collisionData);
            collisions.get(actor).add(collisionData);
        }
    }

    private void registerCollisions(CollisionBox cb) {
        collisions.put(cb.getOwner(), new ArrayList<>());
        for(CollisionBox other : scene.getActiveComponents(CollisionBox.class)) {
            if(cb == other || other == null)
                continue;
            Contact contact = CollisionTester.compareRectangle(cb.getAbsoluteBounds(), other.getAbsoluteBounds());
            if(contact == null)
                continue;
            // There is collision
            CollisionData collisionData = new CollisionData(other.getOwner(), contact);
            for(CollideEvent collideEvent : cb.getOwner().getCollideEvents())
                collideEvent.collide(collisionData);
            collisions.get(cb.getOwner()).add(collisionData);
        }
    }

    public List<CollisionData> getCollisions(Actor actor) {
        if (!collisions.containsKey(actor))
            return new ArrayList<>();
        return collisions.get(actor);
    }

    public HashMap<Actor, List<CollisionData>> getAllCollisions() {
        return collisions;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
