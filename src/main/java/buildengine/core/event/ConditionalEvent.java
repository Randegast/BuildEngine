package buildengine.core.event;

import buildengine.core.scene.ActorComponent;

public class ConditionalEvent extends ActorComponent {

    protected EventCondition condition;
    protected Runnable event;

    public ConditionalEvent(EventCondition condition, Runnable event) {
        this.condition = condition;
        this.event = event;
    }

    public EventCondition getCondition() {
        return condition;
    }

    public void setCondition(EventCondition condition) {
        this.condition = condition;
    }

    public Runnable getEvent() {
        return event;
    }

    public void setEvent(Runnable event) {
        this.event = event;
    }
}
