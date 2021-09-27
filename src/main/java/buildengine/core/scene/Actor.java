package buildengine.core.scene;

import buildengine.math.Transform;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Actors are the objects in a scene. In other engines referred to as GameObject or Entity. Positioning center-point.
 * The Actor holds a name, the scene its in (witch is declared when added to a scene), a transform,
 * a sprite object, a collision body (visibility bounds by default) and a configuration object.
 */
public class Actor {

    /** Identifier of the Actor */
    private final String name;
    /** Scene the actor is in. Assessed when added to a scene.
    * @see Scene#addActor(Actor) */
    private Scene scene;

    /** Transform of the Actor (Relative to world) */
    private final Transform transform;
    /** Components of the actor */
    private final List<ActorComponent> components;

    /** If false directors <u>should</u> ignore this actor but will still be rendered */
    private boolean active;
    /** Default false. If true this object's transform will be updated to the GPU */
    private boolean dynamic;
    /** If false this actor is omitted from being listed in a scene {@see Scene#getList()}
     * and will not be updated/rendered. */
    private boolean destroyed;

    /**
     * Creates a new instance of an Actor object with an empty transform.
     * @param name              Name of the actor, doesn't have to be unique
     * @param components        Components to be added to this actor
     */
    public Actor(String name, ActorComponent... components) {
        this(name, new Transform(), components);
    }

    /**
     * Creates a new instance of an Actor object.
     * @param name              Name of the actor, doesn't have to be unique
     * @param transform         The transform containing the actor's position, size and rotation
     * @param components        Components to be added to this actor
     */
    public Actor(String name, Transform transform, ActorComponent... components) {
        this.name = name;
        this.transform = transform;

        this.components = new ArrayList<>();
        addComponents(components);
    }

    // Components

    public void addComponents(ActorComponent... components) {
        for(ActorComponent component : components)
            addComponent(component);
    }

    public void addComponent(ActorComponent component) {
        if(getComponent(component.getClass()) != null)
            throw new IllegalStateException("Can't add two instances of the same component.");
        component.setOwner(this);
        components.add(component);
    }

    public <U extends ActorComponent> U getComponent(Class<U> componentType) {
        for(ActorComponent comp : components)
            if(comp.getClass() == componentType)
                return componentType.cast(comp);
            return null;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public Transform getTransform() {
        return transform;
    }

    public List<ActorComponent> getComponents() {
        return components;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    // Class

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", transform=" + transform +
                '}';
    }
}
