package buildengine.game.physics;

import buildengine.core.scene.ActorComponent;
import buildengine.math.shape.Rectangle;

public class CollisionBox extends ActorComponent {

    private final Rectangle bounds;
    private final boolean adaptive;

    public CollisionBox() {
        adaptive = true;
        this.bounds = null;
    }

    public CollisionBox(Rectangle bounds) {
        adaptive = false;
        this.bounds = bounds;
    }

    public Rectangle getRelativeBounds() {
        if(adaptive)
            return new Rectangle(-owner.getTransform().getWidth() / 2, -owner.getTransform().getHeight() / 2,
                    owner.getTransform().getWidth(), owner.getTransform().getHeight());
        return bounds;
    }

    public Rectangle getAbsoluteBounds() {
        if(adaptive || bounds == null)
            return new Rectangle(owner.getTransform().getPosition().x - owner.getTransform().getWidth() / 2,
                    owner.getTransform().getPosition().y - owner.getTransform().getHeight() / 2,
                    owner.getTransform().getWidth(), owner.getTransform().getHeight());
        return new Rectangle(owner.getTransform().getPosition().x - bounds.getWidth() / 2 + bounds.x,
                owner.getTransform().getPosition().y - bounds.getHeight() / 2 + bounds.y,
                bounds.getWidth(), bounds.getHeight(),
                owner.getTransform().getRotation() + bounds.getRotation());
    }
}
