package buildengine.core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {

    public static final Vector2f DEFAULT_RESOLUTION_UNIT = new Vector2f(32, 18);

    private final Matrix4f projectionMatrix, viewMatrix;
    private final Matrix4f invertedProjectionMatrix, invertedViewMatrix;
    private final Vector2f resolutionUnit;

    private Vector2f position;

    public Camera() {
        this(new Vector2f());
    }

    public Camera(Vector2f position) {
        this(position, DEFAULT_RESOLUTION_UNIT);
    }

    /**
     * @param resolutionUnit is the unit determining the grid size of the projection.
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
