package buildengine.core.event;

/**
 * An abstract condition interface containing one method returning a {@code boolean}.
 */
public interface EventCondition {

    /**
     * A condition method returning a {@code boolean}
     * @return {@code true} if the condition is met, otherwise returns {@code false}.
     */
    boolean condition();
}
