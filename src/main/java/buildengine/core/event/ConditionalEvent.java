package buildengine.core.event;

import buildengine.core.scene.ActorComponent;

/**
 * An {@code ActorComponent} able to execute an {@code Runnable} event if a specified condition is met.
 * <p>
 *     Formally, the {@link EventCondition#condition() condition method} will be tested every update call. If the condition
 *     passes (returns {@code true}) the event will be executed (the {@code Runnable.run()} method will be called).
 * </p>
 * @author Kai van Maurik
 * @since 1.0
 */
public class ConditionalEvent extends ActorComponent {

    /**
     * The condition to be met before the event will be executed.
     */
    protected EventCondition condition;

    /**
     * The event to be executed after the condition is met.
     */
    protected Runnable event;

    /**
     * Initializes a ConditionalEvent component, defining the condition and event.
     * @param condition the condition to be met before the event will be executed.
     * @param event the event to be executed after the condition is met.
     */
    public ConditionalEvent(EventCondition condition, Runnable event) {
        this.condition = condition;
        this.event = event;
    }

    /**
     * Gets the {@code EventCondition} to be met before executing the event.
     * @return the condition to be met.
     */
    public EventCondition getCondition() {
        return condition;
    }

    /**
     * Sets the {@code EventCondition} to be met before executing the event.
     * @param condition the condition to be met.
     */
    public void setCondition(EventCondition condition) {
        this.condition = condition;
    }

    /**
     * Gets the {@code Runnable} event to be executed after the condition is met.
     * @return the event to be executed.
     */
    public Runnable getEvent() {
        return event;
    }

    /**
     * Sets the {@code Runnable} event to be executed after the condition is met.
     * @param event the event to be executed.
     */
    public void setEvent(Runnable event) {
        this.event = event;
    }
}
