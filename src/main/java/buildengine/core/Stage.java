package buildengine.core;

import buildengine.core.scene.Scene;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.Loader;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.core.scene.director.Renderer;
import org.joml.Vector2f;

import java.io.IOException;

/**
 * Stage responsible for all functionality
 */
public class Stage implements MonoBehaviour, Renderer {

    /** Current scene */
    private Scene scene, newScene;
    /** Camera object */
    protected final Camera camera;

    /**
     * Create a new stage instance
     */
    public Stage() {
        this.camera = new Camera();
    }

    /**
     * Load a new stage. This stage will replace the old one. This methods
     * creates the transition thread, in witch the old state is cleaned up
     * and the new state is initialized. After this process is complete it
     * switches states.
     *
     * @param newScene The state to be loaded and set
     */
    public void queueScene(Scene newScene) {
        this.newScene = newScene;
    }
    private void loadNewScene() {
        cleanUp();
        scene = newScene;
        this.newScene = null;
        begin();
    }

    @Override
    public void begin() {
        if (scene == null)
            return;
        camera.adjustProjection();
        scene.begin();
        for (Director director : scene.getDirectors())
            if(director instanceof Loader)
                ((Loader) director).begin();
    }

    @Override
    public void update(float dt) {
        if (newScene != null)
            loadNewScene();
        if (scene == null)
            return;
        for (Director director : scene.getDirectors())
            if(director instanceof MonoBehaviour)
                ((MonoBehaviour) director).update(dt);
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
        if (scene == null)
            return;
        for (Director director : scene.getDirectors())
            if(director instanceof Renderer)
                ((Renderer) director).render();
    }

    @Override
    public void cleanUp() {
        if (scene == null)
            return;
        for (Director director : scene.getDirectors())
            if(director instanceof Loader)
                ((Loader) director).cleanUp();
    }

    public Scene getScene() {
        return scene;
    }

    public void resize(int width, int height) {
        camera.adjustProjection();
    }

    public Camera getCamera() {
        return camera;
    }
}
