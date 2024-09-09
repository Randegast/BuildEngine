package buildengine.core;

import buildengine.audio.Music;
import buildengine.core.scene.Scene;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.Loader;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.core.scene.director.Renderer;
import buildengine.graphics.Draw;
import buildengine.graphics.renderer.TransitionRenderer;
import buildengine.input.Input;

import java.awt.*;

/**
 * Stage holding the current scene. The stage also holds a TransitionRenderer, which can be customised.
 * The Stage is responsible for all the functionality, like updating and rendering all components of a scene.
 * It also holds a music object for music :).
 *
 * @see Scene
 * @see Music
 * @see TransitionRenderer
 */
public class Stage implements MonoBehaviour, Renderer {

    /** Current scene */
    private Scene scene;

    // Systems
    private final Music music;
    private TransitionRenderer transitionRenderer;

    /**
     * Creates a new stage instance with no scene.
     * @param transitionRenderer The transition renderer to be used for scene transition.
     * @see #loadScene(Scene)
     * @see TransitionRenderer
     */
    public Stage(TransitionRenderer transitionRenderer) {
        this.transitionRenderer = transitionRenderer;
        this.music = new Music();
    }

    /**
     * Creates a new stage instance with the default transition renderer and loads a scene.
     * @param scene The scene to be loaded
     * @see Stage#Stage(TransitionRenderer)
     */
    public Stage(Scene scene) {
        this(new TransitionRenderer());
        loadScene(scene);
    }

    /**
     * Load a new stage. This stage will replace the old one. This method
     * creates the transition thread, in witch the old state is cleaned up
     * and the new state is initialized. After this process is complete it
     * switches states.
     * @param newScene The state to be loaded and set
     */
    public synchronized void loadScene(Scene newScene) {
        Thread thread = new Thread(() -> {
            transitionRenderer.startTransition(newScene);
            cleanUp();
            scene = newScene;
            begin();
            transitionRenderer.stopTransition();
        }, "stage_loader");
        thread.start();
    }

    @Override
    public void begin() {
        if (scene == null)
            return;
        scene.begin();
        for (Director director : scene.getDirectors())
            if(director instanceof Loader)
                ((Loader) director).begin();
    }

    @Override
    public void update() {
        if (scene == null)
            return;
        for (Director director : scene.getDirectors())
            if(director instanceof MonoBehaviour)
                ((MonoBehaviour) director).update();
    }

    @Override
    public void fixedUpdate() {
        if (scene == null)
            return;
        for (Director director : scene.getDirectors())
            if(director instanceof MonoBehaviour)
                ((MonoBehaviour) director).fixedUpdate();
    }

    @Override
    public void render() {
        if (scene != null)
            for (Director director : scene.getDirectors())
                if(director instanceof Renderer)
                    ((Renderer) director).render();
        Draw.resetTransform();
        transitionRenderer.render();
        Draw.resetTransform();
        Debug.draw();
    }

    @Override
    public void cleanUp() {
        if (scene == null)
            return;
        for (Director director : scene.getDirectors())
            if(director instanceof Loader)
                ((Loader) director).cleanUp();
        Input.reset();
        Input.getMouse().setCursor(Cursor.DEFAULT_CURSOR);
    }

    public Scene getScene() {
        return scene;
    }

    public Music getMusic() {
        return music;
    }

    public TransitionRenderer getTransitionRenderer() {
        return transitionRenderer;
    }

    public void setTransitionRenderer(TransitionRenderer transitionRenderer) {
        this.transitionRenderer = transitionRenderer;
    }
}
