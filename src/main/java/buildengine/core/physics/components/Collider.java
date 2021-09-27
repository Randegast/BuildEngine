package buildengine.core.physics.components;

import buildengine.core.physics.CollideEvent;
import buildengine.core.scene.ActorComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider extends ActorComponent {

    private final List<CollideEvent> events;

    public Collider(CollideEvent... listeners) {
        this.events = new ArrayList<>();
        events.addAll(List.of(listeners));
    }

    public void registerCollideEvent(CollideEvent collideEvent) {
        if(!events.contains(collideEvent))
            events.add(collideEvent);
    }

    public List<CollideEvent> getEvents() {
        return events;
    }
}
