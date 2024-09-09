package buildengine.core.scene.director;

/**
 * Providing update and fixed update behaviour
 */
public interface MonoBehaviour extends Loader {

    /**
     * Update function within the game loop
     */
    void update();

    /**
     * Fixed update function used for physics and movement
     */
    default void fixedUpdate() {}

}
