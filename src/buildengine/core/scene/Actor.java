package buildengine.core.scene;

import buildengine.configuration.Configuration;
import buildengine.core.Debug;
import buildengine.game.physics.CollideEvent;
import buildengine.game.physics.CollisionBox;
import buildengine.game.physics.CollisionData;
import buildengine.game.physics.CollisionRegisterDirector;
import buildengine.graphics.Sprite;
import buildengine.math.Transform;
import buildengine.math.shape.Rectangle;
import buildengine.math.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Actors are the objects in a scene. Also referred to as GameObject or Entity. Positioning center-point.
 * The Actor holds a name, the scene its in (witch is declared when added to a scene), a transform,
 * a sprite object, a collision body (visibility bounds by default) and a configuration object.
 *
 * @config bool invisible           (default: false)    If true the sprite will not be drawn.
 * @config bool inactive            (default: false)    If true directors will not update this actor.
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
    /** If false this actor is omitted from being listed in a scene {@see Scene#getList()}
     * and will not be updated/rendered. */
    private boolean destroyed;

    /** Sprite of the Actor */
    private final Sprite sprite;
    /** The list with collide events */
    private final List<CollideEvent> collideEvents;

    /** Configuration settings for the actor */
    private final Configuration configuration;

    /**
     * Creates a new instance of an Actor object setting the default configuration.
     * @param name              Name of the actor, doesn't have to be unique
     * @param transform         The transform containing the actor's position, size and rotation
     * @param sprite            Sprite object containing the actor's rendering data
     * @param components        Components to be added to this actor
     */
    public Actor(String name, Transform transform, Sprite sprite, ActorComponent... components) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.sprite = sprite;

        collideEvents = new ArrayList<>();

        // Default configuration
        configuration = new Configuration();
        configuration.set("invisible", false);
        configuration.set("inactive", false);

        if(sprite != null)
            sprite.setOwner(this);
        addComponents(components);
    }

    /**
     * Gets the visibility bounds as a Rectangle object. Transforming to top-left positioning. Used
     * for collisions.
     * @see Rectangle
     * @see Debug#VISIBLE_COLOR
     * @return The visibility bounds in TL space.
     */
    public Rectangle getVisibilityBounds() {
        return new Rectangle(transform.getPosition().getX() - transform.getWidth() / 2,
                transform.getPosition().getY() - transform.getHeight() / 2,
                transform.getWidth(), transform.getHeight(), transform.getRotation());
    }

    /**
     * Gets the collision bounds as a Rectangle object. Transforming to top-left positioning. If no
     * collision body is defined, it returns the visibility bounds. Used for collisions.
     * @see #getVisibilityBounds()
     * @see Rectangle
     * @see Debug#COLLISION_COLOR
     * @return The collision bounds in TL space.
     */
    public Rectangle getCollisionBounds() {
        CollisionBox cb = getComponent(CollisionBox.class);
        if(cb == null)
            return getVisibilityBounds();
        return cb.getAbsoluteBounds();
    }

    // This is relative! So no absolute word space
    public Vector2f getAnchorPoint() {
        CollisionBox cb = getComponent(CollisionBox.class);
        if(cb == null)
            return new Vector2f(transform.getWidth() / 2, transform.getHeight());// Using low point
        return cb.getRelativeBounds().getCenter();
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
        if(hasComponent(component.getClass()))
            throw new IllegalStateException("Can't add two instances of the same component type (" + component.getClass().getSimpleName() + ").");
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

    public <U extends ActorComponent> boolean hasComponent(Class<U> componentType) {
        for(ActorComponent comp : components)
            if(comp.getClass() == componentType)
                return true;
        return false;
    }

    // Collide event

    public void registerCollideEvent(CollideEvent collideEvent) {
        collideEvents.add(collideEvent);
    }

    public List<CollideEvent> getCollideEvents() {
        return collideEvents;
    }

    public void clearCollideEvents() {
        collideEvents.clear();
    }

    public List<CollisionData> getCollisions() {
        return scene.getDirector(CollisionRegisterDirector.class).getCollisions(this);
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

    public Sprite getSprite() {
        return sprite;
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

    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", transform=" + transform +
                '}';
    }
}
