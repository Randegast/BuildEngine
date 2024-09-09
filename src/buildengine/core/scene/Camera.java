package buildengine.core.scene;

import buildengine.Unstable;
import buildengine.graphics.Draw;
import buildengine.input.Input;
import buildengine.math.shape.Rectangle;
import buildengine.math.vector.Vector2f;
import buildengine.utils.MathUtils;

/**
 * A {@code Camera} object contains all the rendering and positioning information for rendering a scene.
 * <p>
 *     The class keeps track and calculates the projection and view matrix uniform variables, used in the shaders.
 *     <br>
 *     The {@link Camera#resolutionUnit resolutionUnit} variable in this class determines the size of the units used
 *     by the engine. For example, a resolution unit of {@code new Vector2f(16, 9)} will have 16 units in width, and 9
 *     units in height. This variable therefore also determines the aspect ratio.
 *     <br>
 *     The {@code Camera} has a {@code Vector2f} position variable, determining the position (or the inverted translation) of
 *     the camera, formally known as the view matrix's position.
 * </p>
 * @author Kai van Maurik
 * @since 1.0
 */
public class Camera {

    /**
     * The default resolution unit, with an aspect ratio of 16:9 and 32 units across.
     */
    public static final Vector2f DEFAULT_RESOLUTION_UNIT = new Vector2f(32, 18);

    /**
     * This variable determines the size of the units used
     * by the engine. For example, a resolution unit of {@code new Vector2f(16, 9)} will have 16 units in width, and 9
     * units in height. This variable therefore also determines the aspect ratio.
     */
    @Unstable(concerning = "Not implemented!")
    private final Vector2f resolutionUnit;

    /**
     * The position determines the of the view matrix position, and thus the camera position relative to the contents of the stage and current scene.
     */
    private Vector2f position;

    private float zoom, roll;

    public Camera() {
        this(new Vector2f());
    }

    public Camera(Vector2f position) {
        this(position, DEFAULT_RESOLUTION_UNIT);
    }

    /**
     * @param position the position of the newly created camera.
     * @param resolutionUnit the unit determining the grid size of the projection.
     */
    public Camera(Vector2f position, Vector2f resolutionUnit) {
        this.position = position;
        this.resolutionUnit = resolutionUnit;

        this.zoom = 1;
        this.roll = 0;
    }

    public Rectangle getBounds() {
        Vector2f cameraPosition = position.sub(Draw.getCenter(), new Vector2f());
        return new Rectangle(cameraPosition.x, cameraPosition.y,
                Draw.getWidth() / zoom,
                Draw.getHeight() / zoom, -roll);
    }

    public Vector2f getMousePosition() {
        return MathUtils.rotatePoint(-getRoll(), getPosition(),
                Input.getMouse().getPosition().div(getZoom(), new Vector2f()).add(getPosition()));
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}