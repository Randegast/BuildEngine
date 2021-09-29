package buildengine.core.scene;

import buildengine.core.Console;
import buildengine.physics.CollisionRegisterer;
import buildengine.physics.CollisionResolver;
import buildengine.core.scene.director.Director;
import buildengine.core.event.EventExecutor;
import buildengine.core.scene.director.Loader;
import buildengine.docs.NonNull;
import buildengine.graphics.rendering.BatchRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A scene is responsible for holding data like a container (no functionality).
 * The scene holds a list of Actors, Directors, UIElements and some basic systems like
 * physics, renderer and collisions. See respectable classes for functionality.
 *
 * The scene also has a component list, and collected when calling get components.
 *
 * @see Actor
 * @see Director
 */
public class Scene implements Loader {

    /** Name of the Scene */
    private final String name;
    /**
     * The list containing the Actors in the Scene
     */
    private final List<Actor> actors;
    /**
     * The list containing the Directors in the Scene
     */
    private final List<Director> directors;

    /**
     * Initialize a new {@code Scene} object, using "Unnamed" for the name.
     */
    public Scene() {
        this("Unnamed");
    }

    /**
     * Initialize a new {@code Scene} object.
     * @param name the name of the {@code Scene}; doesn't have to be unique.
     */
    public Scene(String name) {
        this.name = name;

        actors = new ArrayList<>();
        directors = new ArrayList<>();
    }

    // Scene

    /**
     * Begin method adds the default directors to the scene.
     * Overwrite this method to add actors to the scene. Make sure to call
     * the super method when overwriting to keep te default directors active.
     */
    @Override
    public void begin() {
        // Rendering
        addDirector(new BatchRenderer());
        // Event handling
        addDirector(new EventExecutor());
        // Default collisions
        addDirector(new CollisionRegisterer(), new CollisionResolver());
    }

    // Actors

    /**
     * Adds an actor to the scene. Updates the scene object of the actor
     * and automatically activates it.
     */
    public void addActor(Actor actor) {
        actor.setScene(this);
        actor.setActive(true);
        actors.add(actor);
    }

    /**
     * Adds actors to the scene. Updates the scene object of the actor
     * and automatically activates it.
     */
    public void addActors(Actor... actors) {
        for(Actor actor : actors)
            addActor(actor);
    }

    /**
     * Creates a list containing all the actors (destroyed actors excluded).
     * @return a list of all actors in the scene.
     */
    @NonNull
    public List<Actor> getActors() {
        List<Actor> list = new ArrayList<>();
        for(Actor actor : actors)
            if(!actor.isDestroyed())
                list.add(actor);
        return list;
    }

    /**
     * Creates a list containing all the active actors (= the actors to be updated).
     * @return a list of all active actors in the scene.
     */
    @NonNull
    public List<Actor> getActiveActors() {
        List<Actor> list = new ArrayList<>();
        for(Actor actor : getActors())
            if(actor.isActive())
                list.add(actor);
        return list;
    }

    /**
     * List all ACTIVE components with type U.
     * @param classType the class of the component.
     * @param <U> the type of component.
     * @return a list of all actor components in this scene of type U
     */
    @NonNull
    public <U extends ActorComponent> List<U> getActiveComponents(Class<U> classType) {
        List<U> list = new ArrayList<>();
        for (Actor actor : getActiveActors()) {
            U comp = actor.getComponent(classType);
            if (comp != null)
                list.add(comp);
        }
        return list;
    }

    /**
     * List ALL components with type U. Including inactive ones.
     * @param classType the class of the component.
     * @param <U> the type of component.
     * @return a list of all actor components in this scene of type U
     */
    @NonNull
    public <U extends ActorComponent> List<U> getComponents(Class<U> classType) {
        List<U> list = new ArrayList<>();
        for (Actor actor : getActors()) {
            U comp = actor.getComponent(classType);
            if (comp != null)
                list.add(comp);
        }
        return list;
    }

    // Directors

    public void addDirector(Director... directors) {
        for(Director director : directors)
            addDirector(director);
    }

    public void addDirector(Director director) {
        if(getDirector(director.getClass()) != null) {
            Console.warn("Tried to add multiple director instances of " + director.getClass().getSimpleName());
            return;
        }
        director.setScene(this);
        directors.add(director);
        Collections.sort(directors);
    }

    public void removeDirector(Director director) {
        if(!directors.contains(director))
            return;
        directors.remove(director);
    }

    public <U extends Director> U getDirector(Class<U> classType) {
        for(Director director : directors)
            if(director.getClass() == classType)
                return classType.cast(director);
            return null;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "name='" + name + '\'' +
                '}';
    }
}
