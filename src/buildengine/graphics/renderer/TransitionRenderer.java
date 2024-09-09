package buildengine.graphics.renderer;

import buildengine.core.scene.Scene;
import buildengine.core.scene.director.Renderer;
import buildengine.graphics.Draw;
import buildengine.math.vector.Vector2f;
import buildengine.time.Time;
import buildengine.utils.MathUtils;

import java.awt.*;

/**
 * Responsible for rendering a scene transition.
 */
public class TransitionRenderer implements Renderer {

    /** The time of the transition in seconds */
    public static final double TRANSITION_TIME = .06;
    /** The transition (fade) color */
    public static final Color TRANSITION_COLOR = Color.BLACK;

    private double alphaIndex = 0.0d;
    private boolean inTransition;

    public synchronized void startTransition(Scene newScene) {
        if(inTransition)
            return;
        inTransition = true;
        alphaIndex = 0;
        // Loading state
        if(newScene == null)
            alphaIndex = 1;
        while (alphaIndex < 1)
            Thread.onSpinWait();
    }

    public synchronized void stopTransition() {
        inTransition = false;
    }

    @Override
    public void render() {
        if(!inTransition && alphaIndex == 0)
            return;
        alphaIndex = MathUtils.interpolate(inTransition ? 1.0d : 0.0d, alphaIndex, Time.deltaTime / TRANSITION_TIME);
        Color color = new Color(TRANSITION_COLOR.getRed(), TRANSITION_COLOR.getBlue(),
                TRANSITION_COLOR.getGreen(), MathUtils.toInt(alphaIndex * 255));
        // Screen
        Draw.drawRect(new Vector2f(), Draw.getWidth(), Draw.getHeight(), color, true);
    }
}
