package buildengine.core.scene.director;

/**
 * Responsible for drawing to the screen
 */
public interface Renderer {

    /**
     * Rendering function for calling {@link buildengine.graphics.Draw}.
     */
    void render();

}
