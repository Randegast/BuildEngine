package buildengine.game.physics;

import buildengine.core.scene.ActorComponent;

public class RigidBody extends ActorComponent {

    private final boolean immovable;

    public RigidBody(boolean immovable) {
        this.immovable = immovable;
    }

    public boolean isImmovable() {
        return immovable;
    }
}
