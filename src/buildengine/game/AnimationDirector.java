package buildengine.game;

import buildengine.core.scene.Actor;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.graphics.Sprite;
import buildengine.graphics.renderer.Animation;

/**
 * Scene director responsible for updating animations
 */
public class AnimationDirector extends Director implements MonoBehaviour {

    private long lastTime;

    @Override
    public void begin() {
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        long elapsedTime = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        for(Actor actor : scene.getActors()) {
            // Check if actor is animation
            if(actor.getSprite().getType() != Sprite.Type.ANIMATION || actor.getSprite().getAnimation() == null)
                continue;
            // Check if playing
            Animation animation = actor.getSprite().getAnimation();
            if(!animation.isPlaying())
                continue;
            // Increase internal timer, if not reached return
            animation.setInternalTimer(animation.getInternalTimer() + elapsedTime);
            if(animation.getInternalTimer() < animation.getFrameInterval())
                continue;
            // Internal timer is reached
            animation.setInternalTimer(0);
            animation.setIndex(animation.getIndex() + 1);
            if(animation.getIndex() >= animation.getFrames().length) {
                // Animation has no more frames
                animation.setIndex(0);
                // If animation is one time only
                if(animation.isPlayingOnlyOnce()) {
                    animation.setPlaying(false); // Stop playing animation
                    animation.setIndex(animation.getFrames().length - 1); // Set animation to last frame
                }
            }
        }
    }
}
