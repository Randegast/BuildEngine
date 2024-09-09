package buildengine.core.scene;

import buildengine.core.Stage;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.Loader;
import buildengine.game.AnimationDirector;
import buildengine.game.physics.CollisionCorrectionDirector;
import buildengine.game.physics.CollisionRegisterDirector;
import buildengine.graphics.renderer.DefaultSceneRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A scene is responsible for holding data like a container (no functionality).
 * The scene holds a list of Actors, Directors, UIElements and some basic systems like
 * physics, renderer and collisions. See respectable classes for functionality.
 *
 * @see Stage
 * @see Actor
 * @see Director
 * @see buildengine.core.scene.director.Renderer
 * @see CollisionCorrectionDirector
 * @see CollisionRegisterDirector
 */
public class Scene implements Loader {

    // Scene
    /** Name of the Scene */
    public final String name;
    /**
     * The list containing the Actors in the Scene
     */
    private List<Actor> actors = new ArrayList<>();
    /**
     * The list containing the Directors in the Scene
     */
    private List<Director> directors = new ArrayList<>();

    private final Camera camera;

    public Scene() {
        this("Unnamed");
    }

    public Scene(String name) {
        this.name = name;
        this.camera = new Camera();
    }

    /**
     * Begin method adds the default directors to the scene.
     * Overwrite this method to add actors to the scene. Make sure to call
     * the super method when overwriting to keep te default directors active.
     */
    @Override
    public void begin() {
        addDefaultDirectors();
    }

    public void addDefaultDirectors() {
        addDirector(new AnimationDirector(), new CollisionRegisterDirector(CollisionRegisterDirector.Mode.COLLISION_BOX),
            new CollisionCorrectionDirector(), new DefaultSceneRenderer());
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
    public <U extends ActorComponent> List<U> getComponents(Class<U> classType) {
        List<U> list = new ArrayList<>();
        for (Actor actor : getActors()) {
            U comp = actor.getComponent(classType);
            if (comp != null)
                list.add(comp);
        }
        return list;
    }

    public List<Actor> getActors(String name) {
        List<Actor> actors = new ArrayList<>();
        for(Actor actor : getActors())
            if(actor.getName().equals(name))
                actors.add(actor);
        return actors;
    }

    public void clearActors() {
        actors = new ArrayList<>();
    }

    // Directors

    public void addDirector(Director... directors) {
        for(Director director : directors)
            addDirector(director);
    }

    public void addDirector(Director director) {
        if(getDirector(director.getClass()) != null)
            throw new IllegalArgumentException("Tried to add multiple director instances of " + director.getClass().getSimpleName());
        director.setScene(this);
        directors.add(director);
        Collections.sort(directors);
    }

    public void removeDirector(Director director) {
        if(!directors.contains(director))
            return;
        directors.remove(director);
    }

    public <T extends Director> T getDirector(Class<T> type) {
        for(Director director : directors)
            if(director.getClass() == type)
                return type.cast(director);
        return null;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void clearDirectors() {
        directors = new ArrayList<>();
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "name='" + name + '\'' +
                '}';
    }
}
