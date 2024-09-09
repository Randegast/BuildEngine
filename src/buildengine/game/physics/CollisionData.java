package buildengine.game.physics;

import buildengine.core.scene.Actor;
import buildengine.math.collision.Contact;

/**
 * Contains collision information
 */
public class CollisionData {

    private final Actor actor;
    private final Contact contact;

    public CollisionData(Actor actor, Contact contact) {
        this.actor = actor;
        this.contact = contact;
    }

    /**
     * Returns the actor who is collided with
     * @return the actor who is collided with
     */
    public Actor getActor() {
        return actor;
    }

    public Contact getContact() {
        return contact;
    }

    @Override
    public String toString() {
        return actor.getName() + ": " + contact.toString();
    }
}
