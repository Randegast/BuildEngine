package buildengine.graphics.renderer;

import buildengine.configuration.Configuration;
import buildengine.core.Debug;
import buildengine.core.scene.Actor;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.Renderer;
import buildengine.graphics.Draw;
import buildengine.graphics.Sprite;
import buildengine.math.shape.Rectangle;
import buildengine.math.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Scene renderer is responsible for rendering a scene and its components. Default implementation provided.
 *
 * @see DefaultSceneRenderer
 *
 * @config bool enable-visibility-cutoff    {default: true}     Enable sprites disappearing when outside the camera bounds.
 */
public abstract class SceneRenderer extends Director implements Renderer {

    protected final Configuration configuration;

    public SceneRenderer() {
        this.configuration = new Configuration();
        configuration.set("enable-visibility-cutoff", true);
    }

    /**
     * Draw a sprite to the screen, taking counterpoint-perspective and camera into consideration.
     * To be more specific, using the actor's location, subtracting half the size, and using that as the draw position.
     * If the sprite or parts of the sprite (in case of REPEAT draw mode) are not visible to the camera object, it will not be drawn
     * @param sprite    The sprite to be drawn
     */
    protected void drawSprite(Sprite sprite) {
        // If there are no frames to render, return
        if(sprite.getCurrentDrawableImage() == null)
            return;
        // Shortening variables
        Vector2f position = sprite.getOwner().getTransform().getPosition().sub(sprite.getOwner().getTransform().getWidth() / 2,
                sprite.getOwner().getTransform().getHeight() / 2, new Vector2f());
        Vector2f size = sprite.getOwner().getTransform().getSize();
        // Stores current draw opacity, to restore after
        float previousDrawOpacity = Draw.getDrawOpacity();
        Draw.setDrawOpacity(sprite.getDrawOpacity());
        // Rendering
        switch (sprite.getDrawMode()) {
            // Stretch
            case STRETCH -> Draw.drawImage(sprite.getCurrentDrawableImage(), position, size.x * sprite.getWidth(), size.y * sprite.getHeight(),
                    sprite.getOwner().getTransform().getRotation(), sprite.getOwner().getTransform().getPosition());
            // Repeat
            case REPEAT -> {
                for (int y = 0; y < size.getY() / sprite.getHeight(); y++)
                    for(int x = 0; x < size.getX() / sprite.getWidth(); x++) {
                        if(!isVisible(new Rectangle(position.x + x * sprite.getWidth(),
                                position.y + y * sprite.getHeight(), sprite.getWidth() * scene.getCamera().getZoom(),
                                sprite.getHeight() * scene.getCamera().getZoom(), sprite.getOwner().getTransform().getRotation())))
                            continue;
                        Draw.drawImage(sprite.getCurrentDrawableImage(), position.add(x * sprite.getWidth(),
                                y * sprite.getHeight(), new Vector2f()), sprite.getWidth(), sprite.getHeight(),
                                sprite.getOwner().getTransform().getRotation(), sprite.getOwner().getTransform().getPosition());
                    }
            }
        }
        Draw.setDrawOpacity(previousDrawOpacity);
    }


    /**
     * Draw the default debug lines around actors for Visibility Bounds and Collision Bounds
     * @see Debug#VISIBLE_COLOR
     * @see Debug#COLLISION_COLOR
     */
    protected void drawDebugLines(Actor actor) {
        // Drawing Visible Bounds
        Draw.drawRect(actor.getVisibilityBounds(), Debug.VISIBLE_COLOR, false);
        // Drawing Collision Bounds
        Draw.drawRect(actor.getCollisionBounds(), Debug.COLLISION_COLOR, false);
    }

    /**
     * List the actors of the current scene visible to the camera
     * @return A list the actors of the current scene visible to the camera
     */
    protected List<Actor> getVisibleActors() {
        List<Actor> actorList = new ArrayList<>();
        if(!configuration.getBoolean("enable-visibility-cutoff")) {
            for(Actor actor : scene.getActors())
                if(!actor.getConfiguration().getBoolean("invisible"))
                    actorList.add(actor);
            return actorList;
        }
        Vector2f cameraPosition = scene.getCamera().getPosition().sub(Draw.getCenter(), new Vector2f());
        Rectangle screen = new Rectangle(cameraPosition.x, cameraPosition.y,
                Draw.getWidth() / scene.getCamera().getZoom(),
                Draw.getHeight() / scene.getCamera().getZoom(), -scene.getCamera().getRoll());
        for(Actor actor : scene.getActors()) {
            Rectangle visibleBound = actor.getVisibilityBounds();
            if(!screen.intersectsLazy(visibleBound))
                continue;
            if(screen.intersects(visibleBound))
                actorList.add(actor);
        }
        return actorList;
    }

    /**
     * Checking if a rectangle intersects with the camera using lazy intersect.
     * @param rectangle The rectangle to check
     * @return  {@code true} - if the rectangle intersects.
     */
    protected boolean isVisible(Rectangle rectangle) {
        Rectangle screen = new Rectangle(scene.getCamera().getPosition().x, scene.getCamera().getPosition().y,
                Draw.getWidth() / scene.getCamera().getZoom(),
                Draw.getHeight() / scene.getCamera().getZoom(), -scene.getCamera().getRoll());
        return screen.intersectsLazy(rectangle);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
