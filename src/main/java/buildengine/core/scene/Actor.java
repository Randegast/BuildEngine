package buildengine.core.scene;

import buildengine.core.Console;
import buildengine.math.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An {@code Actor} is an object in a scene (a.k.a. Entity or GameObject). This object contains a list
 * of {@code ActorComponents}, describing the attributes and capabilities of the {@code Actor}.
 * <p>
 *     The object also knows its position relative to the {@code Scene} its in. Initially however, the
 *     scene variable in this class is {@code null}, because the actor is not yet assigned to a scene.
 *     As soon as the Actor is added, the scene object will be assigned and {@code getScene()} will no
 *     longer return null.
 * </p>
 * <p>
 *     The position is stored in a {@code Transform} object. The position describes the center of the
 *     actor. The width and height describes the scale, and the rotation describes the 2D rotation (roll).
 * </p>
 * @see Scene
 * @see ActorComponent
 * @see Transform
 * @author Kai van Maurik
 * @since 1.0
 */
public class Actor {

    /** Name of the Actor */
    private final String name;
    /** Identifier of the Actor */
    private final UUID uuid; // TODO Implementation by context

    /** Transform of the Actor (Relative to the {@code Scene}) */
    private final Transform transform;
    /** The list containing the components of the actor */
    private final List<ActorComponent> components;

    /** Scene the actor is in. Assessed when added to a scene.
    * @see Scene#addActor(Actor) */
    private Scene scene;

    /** If false directors <u>should</u> ignore this actor but will still be rendered */
    private boolean active;
    /** Default false. If true this object's transform will be updated to the GPU */
    private boolean dynamic;
    /** If false this actor is omitted from being listed in a scene {@see Scene#getList()}
     * and therefore will not be updated/rendered. */
    private boolean destroyed;

    /**
     * Initializes a newly created {@code Actor} object, using an empty {@code Transform} object.
     * @param name              Name of the actor, doesn't have to be unique
     * @param components        Components to be added to this actor
     */
    public Actor(String name, ActorComponent... components) {
        this(name, new Transform(), components);
    }

    /**
     * Initializes a newly created {@code Actor} object.
     * @param name              the name of the actor, doesn't have to be unique.
     * @param transform         the transform describing the actor's position, size and rotation.
     * @param components        the components to be added to this actor.
     */
    public Actor(String name, Transform transform, ActorComponent... components) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.transform = transform;

        this.components = new ArrayList<>();
        addComponents(components);
    }

    // Components

    /**
     * Adds multiple components to this actor, by calling the {@link Actor#addComponent(ActorComponent) addComponent method}.
     * @param components the components to be added.
     */
    public void addComponents(ActorComponent... components) {
        for(ActorComponent component : components)
            addComponent(component);
    }

    /**
     * Adds an {@code ActorComponent} to this actor.
     * @param component the {@code ActorComponent} to be added.
     */
    public void addComponent(ActorComponent component) {
        if(getComponent(component.getClass()) != null) {
            Console.warn("Tried to add multiple component instances of " + component.getClass().getSimpleName());
            return;
        }
        component.setOwner(this);
        components.add(component);
    }

    /**
     * Gets an {@code ActorComponent} by component type. Because components are unique, this will always return
     * the single actor, or {@code null} if this object doesn't contain the specified component type. Formally,
     * compares the class of all components to the given componentType class. If there is a match, returns that
     * match. Otherwise, returns {@code null}.
     * @param componentType the class object of the requested component.
     * @param <U> the {@code ActorComponent} type.
     * @return the component matching the component type, or {@code null} if no such component is present.
     */
    public <U extends ActorComponent> U getComponent(Class<U> componentType) {
        for(ActorComponent comp : components)
            if(comp.getClass() == componentType)
                return componentType.cast(comp);
            return null;
    }

    // Getters and setters

    /**
     * Gets the previously defined {@code String} name of this {@code Actor} object.
     * @return the name of this actor
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the {@code Transform} of this {@code Actor} object, containing the transformation relative to the scene.
     * @return the {@code Transform} of this actor
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Gets the list of components contained by this {@code Actor} object.
     * @return the list of components
     */
    public List<ActorComponent> getComponents() {
        return components;
    }

    /**
     * Gets the {@code Scene} context of this {@code Actor} object.
     * @return the scene context if present, if not return {@code null}.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Changes the {@code Scene} context of this {@code Actor} object. This method should only be called by the
     * {@link Scene#addActor(Actor) addActor} method, or when the scene context of the actor changes.
     * @param scene the scene to change to.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Indicates whether the actor is active or not. Deactivated objects won't be updated, but will still be visible.
     * @implNote The active state determines if the actor <u>should</u>
     * be updated or not. This is however determined by updating {@code Director} classes to use the
     * {@link Scene#getActiveComponents getActiveComponents()} method when updating all components.
     * @return {@code true} if the actor is active and should be updated; {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activates or deactivates this actor. Deactivated objects won't be updated, but will still be visible.
     * Actors are activated automatically when added to a scene.
     * @implNote The active state determines if the actor <u>should</u>
     * be updated or not. This is however determined by updating {@code Director} classes to use the
     * {@link Scene#getActiveComponents getActiveComponents()} method when updating all components.
     * @param active {@code false} if the actor is not to be updated; {@code true} otherwise.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Indicates whether the actor is dynamic or not. Dynamic actors are able to move on screen. Actors
     * are not dynamic by default.
     * @implNote Dynamic actors will be constantly updated to
     * the render system, slowing down the rendering process, but is required if changes to the transform
     * of this actor are to be rendered.
     * @return {@code true} if the actor is dynamic; {@code false} otherwise.
     */
    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * Sets the dynamic state of the actor. Dynamic actors are able to move on screen. Actors are not dynamic by default.
     * @param dynamic {@code true} if the actor is dynamic and able to move on screen; {@code false} otherwise.
     * @implNote Dynamic actors will be constantly updated to
     * the render system, slowing down the rendering process, but is required if changes to the transform
     * of this actor are to be rendered.
     */
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    /**
     * Indicates whether the actor is destroyed or not. Destroyed actors are not updated or rendered. Informally,
     * they are removed from the scene. Formally, they are omitted when listing all actors in a scene.
     * @return {@code true} if the actor is destroyed; {@code false} otherwise.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Destroys or resurrects the actor. Destroyed actors are not updated or rendered. Informally,
     * they are removed from the scene. Formally, they are omitted when listing all actors in a scene.
     * @param destroyed {@code true} to destroy the actor; {@code false} to resurrect.
     */
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
