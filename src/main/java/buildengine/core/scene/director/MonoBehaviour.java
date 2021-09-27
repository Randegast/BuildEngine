package buildengine.core.scene.director;

/**
 * Providing update and fixed update behaviour
 */
public interface MonoBehaviour extends Loader {

    /**
     * Update function within the game loop
     * @param dt delta time variable used for fixed updates
     */
    void update(float dt);

    /**
     * Fixed update function used for physics and movement
     */
    default void fixedUpdate() {}

}
