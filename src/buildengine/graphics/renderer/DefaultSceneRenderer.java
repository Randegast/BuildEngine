package buildengine.graphics.renderer;

import buildengine.core.Debug;
import buildengine.core.scene.Actor;
import buildengine.core.scene.Camera;
import buildengine.graphics.Draw;
import buildengine.graphics.Sprite;
import buildengine.math.vector.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A default implementation of a scene renderer
 *
 * @config bool render-bounds               (default: true)    Render the debug lines. {@see SceneRenderer#drawDebugLines(Actor)}
 * @config bool y-index-rendering           (default: true)    Enable rendering higher y-positions first
 * @config bool y-index-collision-center    (default: true)    Use collision body position as center when y-position rendering
 */
public class DefaultSceneRenderer extends SceneRenderer {


    public DefaultSceneRenderer() {
        // Configuration
        super();
        configuration.set("render-bounds", true);
        configuration.set("y-index-rendering", true);
        configuration.set("y-index-collision-center", true);
    }

    @Override
    public void render() {
        // Apply camera transform
        Camera camera = scene.getCamera();
        Draw.translate(camera.getPosition().sub(Draw.getCenter(), new Vector2f()).flip());
        Draw.scale(new Vector2f(camera.getZoom()));
        Draw.rotate(camera.getRoll());
        // Z-index sorting
        HashMap<Integer, List<Sprite>> sprites = new HashMap<>();
        Actor[] out_actorArray = getDrawableActors().toArray(new Actor[0]);
        bubbleSort(out_actorArray);
        for(Actor actor : out_actorArray) {
            if(sprites.containsKey(actor.getSprite().getzIndex()))
                sprites.get(actor.getSprite().getzIndex()).add(actor.getSprite());
            else {
                ArrayList<Sprite> spriteArrayList = new ArrayList<>();
                spriteArrayList.add(actor.getSprite());
                sprites.put(actor.getSprite().getzIndex(), spriteArrayList);
            }
        }

        // Y-index sorting & drawing
        for(int i : sprites.keySet()) {
            Sprite[] out_drawArray = sprites.get(i).toArray(new Sprite[0]);
            bubbleSort(out_drawArray);
            for(Sprite sprite : out_drawArray)
                drawSprite(sprite);
        }

        // Drawing Debug
        for(Actor actor : scene.getActors())
            if(Debug.isEnableDrawing() && configuration.getBoolean("render-bounds"))
                drawDebugLines(actor);
    }

    public void bubbleSort(Actor[] a) {
        boolean sorted = false;
        Actor temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i].getSprite().getzIndex()
                        > a[i+1].getSprite().getzIndex()) {
                    temp = a[i];
                    a[i] = a[i+1];
                    a[i+1] = temp;
                    sorted = false;
                }
            }
        }
    }

    public void bubbleSort(Sprite[] a) {
        boolean sorted = false;
        Sprite temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i].getOwner().getTransform().getPosition().getY() + a[i].getOwner().getAnchorPoint().getY()
                        > a[i+1].getOwner().getTransform().getPosition().getY() + a[i+1].getOwner().getAnchorPoint().getY()) {
                    temp = a[i];
                    a[i] = a[i+1];
                    a[i+1] = temp;
                    sorted = false;
                }
            }
        }
    }

    public List<Actor> getDrawableActors() {
        List<Actor> actors = new ArrayList<>();
        for(Actor actor : scene.getActors()) {
            if(actor.getSprite() == null || actor.getSprite().getCurrentDrawableImage() == null)
                continue;
            if(actor.getConfiguration().getBoolean("invisible"))
                continue;
            actors.add(actor);
        }
        return actors;
    }
}
