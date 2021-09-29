package buildengine.core.event;

import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.MonoBehaviour;

/**
 * A {@code Director} that tests and executes all {@link ConditionalEvent ConditionalEvent} components in the scene.
 * @author Kai van Maurik
 * @since 1.0
 */
public class EventExecutor extends Director implements MonoBehaviour {

    @Override
    public void begin() {}

    @Override
    public void update(float dt) {
        for(ConditionalEvent event : scene.getActiveComponents(ConditionalEvent.class))
            if(event.getCondition().condition())
                event.getEvent().run();
    }
}
