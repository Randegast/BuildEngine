package buildengine.core.scene;

/**
 * An abstract template for an {@code ActorComponent}.
 * The {@code ActorComponent} is a component for an actor. Actor components describe the
 * attributes and capabilities of the {@code Actor} object they are added to.
 * <p>
 *     A component is unique. Only one component of the same type can be added to an actor. Formally, its
 *     responsibility is just to hold variables, and functions to change those variables.
 * </p>
 * <p>
 *     The component contains a reference to its {@code Actor} owner, but this is {@code null} until the
 *     component is added to an {@code Actor}.
 * </p>
 * @see Actor
 * @see buildengine.core.scene.director.Director
 * @since 1.0
 * @author Kai van Maurik
 *
 * @implNote A component in this ECS design should have minimal functionality (in most cases none at all).
 * It is mainly responsible for keeping track of values. These values should be assigned, updated and read
 * from by one or more {@code Director} objects in the scene.
 */
public abstract class ActorComponent {

    /**
     * The {@code Actor} owner of this component. Initially null until assigned to an actor.
     */
    protected Actor owner;

    /**
     * Gets the {@code Actor} owner of this component.
     * @return the {@code Actor} owner of this component, or {@code null} if the component has no owner.
     */
    public Actor getOwner() {
        return owner;
    }

    /**
     * Changes the {@code Actor} owner of this component. This method should only be called by the
     * {@link Actor#addComponent addComponent} method, or when the actor context of the component changes.
     */
    public void setOwner(Actor owner) {
        this.owner = owner;
    }
}
