package buildengine.core.scene.director;

/**
 * Functionality to load and unload assets or variables.
 */
public interface Loader {

    /**
     * Initialisation method
     */
    void begin();
    /**
     * Exit method
     */
    default void cleanUp() {}

}
