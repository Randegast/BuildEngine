package buildengine.core.scene.director;

import buildengine.core.scene.Scene;

/**
 * An abstract template for a {@code Director}. The {@code Director} is responsible for a specific functionality in
 * a scene. Formally, it is responsible for changing values of {@code Actor} objects and their {@code ActorComponent}'s.
 * <p>
 *
 * </p>
 * @author Kai van Maurik
 * @since 1.0
 *
 * @implNote A director (a.k.a. system) in this ECS design should store only a minimal number of variables. Its main
 * responsibility is to alter existing variables. The storage of data in a scene is the responsibility of {@code ActorComponent}'s.
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
