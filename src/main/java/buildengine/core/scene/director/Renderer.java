package buildengine.core.scene.director;

/**
 * Responsible for drawing to the screen
 */
public interface Renderer {

    /**
     * Rendering function for OpenGL or ImGui
     */
    void render();

}
