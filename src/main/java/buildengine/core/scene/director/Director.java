package buildengine.core.scene.director;

import buildengine.core.scene.Scene;

/**
 * Directors can be added to a {@see Scene} and work like scripts or systems.
 * They are responsible for all the activity in a scene. Examples of default directors is
 * the {@see DefaultPhysics} and {@see AnimationDirector}.
 *
 * @see Scene
 */
public abstract class Director implements Comparable<Director> {

    protected ExecutionPhase executionPhase = ExecutionPhase.DEFAULT;
    protected Scene scene;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public ExecutionPhase getExecutionPhase() {
        return executionPhase;
    }

    @Override
    public int compareTo(Director o) {
        return Integer.compare(executionPhase.getPriority(), o.getExecutionPhase().getPriority());
    }
}
