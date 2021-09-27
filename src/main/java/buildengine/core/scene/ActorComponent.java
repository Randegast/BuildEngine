package buildengine.core.scene;

/**
 * Component for an actor, holding an actor object.
 * This actor object is only assigned when added to a scene.
 *
 */
public abstract class ActorComponent {

    protected Actor owner = null;

    public Actor getOwner() {
        return owner;
    }

    public void setOwner(Actor owner) {
        this.owner = owner;
    }
}
