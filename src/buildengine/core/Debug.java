package buildengine.core;

import buildengine.BuildEngine;
import buildengine.graphics.Draw;
import buildengine.math.vector.Vector2f;

import java.awt.*;

/**
 * Static class containing debug menu, debug drawing and messages handling.
 * <p>
 *     The Debug menu is drawn on the top-right and drawing
 *     information depending on the mode. It also always draws
 *     the {@code Object.toString()} of the trackers currently
 *     active.
 * </p>
 * <p>
 *     Debug lines are lines drawn to represent certain aspects
 *     that are invisible by default (like bounding boxes). These
 *     can be regulated in this class.
 * </p>
 * <p>
 *     Debug messages are regulated messages and only show up
 *     if debug messages are enabled.
 * </p>
 */
public class Debug {

    // Settings for drawing
    private static final float DRAW_X = .1f, DRAW_Y_DISTANCE = .4f;
    // Collisions
    public static final Color VISIBLE_COLOR = new Color(47, 163, 70),
            COLLISION_COLOR = new Color(59, 112, 229);

    // Draw modes
    /** Draw nothing */
    public static final int NO_DRAW = 0;
    /** Draw the fps */
    public static final int FPS_ONLY = 1;
    /** Draw the fps and scene */
    public static final int FPS_AND_SCENE = 2;

    /**
     * The draw mode
     */
    private static int debugDrawMode;
    /**
     * Enable debug mode
     */
    private static boolean enableDrawing;
    /**
     * Enable debug messages
     */
    private static boolean enableMessages;
    /**
     * Mute music for runtime
     */
    private static boolean musicMuted;

    /**
     * Trackers
     */
    private static Object[] tracking;

    private Debug() {}

    /**
     * Enable debugging and set the settings
     * @param drawMode The items to track on top of the screen.
     *                 Use {@code NO_DRAW}, {@code FPS_ONLY} or {@code FPS_AND_SCENE}.
     *                 Debug always draws custom trackers. {@see track(string...)}
     * @param debugLines Enable the debug lines like collision and visibility bounds.
     * @param messages {@code true} - Enable debug messages in the console.
     *                 {@code false} - Don't use console for debug messages
     * @param muteMusic Mute all game music
     */
    public static void enable(int drawMode, boolean debugLines, boolean messages, boolean muteMusic) {
        debugDrawMode = drawMode;
        enableDrawing = debugLines;
        enableMessages = messages;
        musicMuted = muteMusic;
    }

    /**
     * Put a debug message in the console. Only works if debug messages are enabled
     * @param s the object, of any kind that allows {@code toString()}. Including string objects.
     */
    public static void msg(Object s) {
        if(enableMessages)
            System.out.println(s.toString());
    }

    /**
     * Track certain stats in the debug menu.
     * @param track the strings to track. Make sure you update this list every time it changes.
     */
    public static void track(Object... track) {
        tracking = track;
    }

    /**
     * Draws the debug window. This method is called in the Engine class and is not recommended
     * to use outside of it.
     */
    public static void draw() {
        // Draw mode
        for (int i = 0; i < debugDrawMode; i++) {
            if(i == 0)
                Draw.drawString("FPS: " + Engine.getFpsCount(), new Vector2f(DRAW_X, DRAW_Y_DISTANCE), BuildEngine.COLOR);
            if(i == 1)
                Draw.drawString("Scene: " + (BuildEngine.getEngine().getStage().getScene() == null ? "null" :
                                BuildEngine.getEngine().getStage().getScene().getName()),
                        new Vector2f(DRAW_X, DRAW_Y_DISTANCE * debugDrawMode), BuildEngine.COLOR);
        }
        // Custom trackers
        if(tracking != null)
            for (int i = 0; i < tracking.length; i++)
                Draw.drawString(String.valueOf(tracking[i]), new Vector2f(DRAW_X, debugDrawMode * DRAW_Y_DISTANCE + (i + 1) * DRAW_Y_DISTANCE), BuildEngine.COLOR);
    }

    public static boolean isEnableDrawing() {
        return enableDrawing;
    }

    public static int getDebugDrawMode() {
        return debugDrawMode;
    }

    public static boolean isEnableMessages() {
        return enableMessages;
    }

    public static boolean isMusicMuted() {
        return musicMuted;
    }
}
