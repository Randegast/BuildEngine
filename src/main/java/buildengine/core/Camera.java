package buildengine.core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
 * @see buildengine.graphics.rendering.BatchRenderer
 * @author Kai van Maurik
 * @since 1.0
 */
public class Camera {

    /**
     * The default resolution unit, with an aspect ratio of 16:9 and 32 units across.
     */
    public static final Vector2f DEFAULT_RESOLUTION_UNIT = new Vector2f(32, 18);

    /**
     * The projection and view matrix responsible for calculating the vertex shader position.
     */
    private final Matrix4f projectionMatrix, viewMatrix;
    /**
     * The inverted projection and view matrix used for reverse engineering screen position.
     */
    private final Matrix4f invertedProjectionMatrix, invertedViewMatrix;

    /**
     * This variable determines the size of the units used
     * by the engine. For example, a resolution unit of {@code new Vector2f(16, 9)} will have 16 units in width, and 9
     * units in height. This variable therefore also determines the aspect ratio.
     */
    private final Vector2f resolutionUnit;

    /**
     * The position determines the of the view matrix position, and thus the camera position relative to the contents of the stage and current scene.
     */
    private Vector2f position;

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

        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        invertedProjectionMatrix = new Matrix4f();
        invertedViewMatrix = new Matrix4f();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(-resolutionUnit.x / 2, resolutionUnit.x / 2, -resolutionUnit.y / 2, resolutionUnit.y / 2, 0.0f, 100.0f);
        projectionMatrix.invert(invertedProjectionMatrix);
    }

    public Matrix4f getViewMatrix() {
        Vector3f camaraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position, 20.0f),
                camaraFront.add(position.x, position.y, 0.0f), cameraUp);
        viewMatrix.invert(invertedViewMatrix);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Converts screen coordinates to world coordinates viewed by the camera. Uses the same translation
     * as used by the default vertex shader.
     * @param screenCoordinates the (normalized) screen coordinates to convert
     * @return Coordinates as viewed by this camera object.
     */
    public Vector2f convertToWorldCoordinates(Vector2f screenCoordinates) {
        Vector4f temp = new Vector4f(screenCoordinates, 0, 1);
        temp.mul(invertedProjectionMatrix).mul(invertedViewMatrix);
        return new Vector2f(temp.x, temp.y);
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }
}
