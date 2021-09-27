package buildengine.core.event;

import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.MonoBehaviour;

/**
 * Executes all conditional events in a scene.
 */
public class EventExecutor extends Director implements MonoBehaviour {

    @Override
    public void begin() {

    }

    @Override
    public void update(float dt) {
        for(ConditionalEvent event : scene.getComponents(ConditionalEvent.class))
            if(event.getCondition().condition())
                event.getEvent().run();
    }
}
