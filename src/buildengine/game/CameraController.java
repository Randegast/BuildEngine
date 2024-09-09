package buildengine.game;

import buildengine.BuildEngine;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.graphics.Draw;
import buildengine.input.ClickType;
import buildengine.input.Input;
import buildengine.math.vector.Vector2f;
import buildengine.utils.MathUtils;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Director containing full debug camera controlling functionality.
 * Able to move the camera (arrows), zoom (scroll wheel) and roll (Q and E).
 * You can also change the camera position by clicking and dragging the screen.
 */
public class CameraController extends Director implements MonoBehaviour {

    public static final float DEFAULT_SPEED = 10;
    public static final float ZOOM_MIN = 0.05f, ZOOM_MAX = 10.0f;

    private boolean up, down, right, left, rollLeft, rollRight, reset;
    private Vector2f mouseDragPoint;
    private int zooming;
    private float moveSpeed, rollSpeed; // Units per second
    private float zoomSpeed; // Exponential: zoom = zoomSpeed^(1/camera.getZoom())

    @Override
    public void begin() {
        moveSpeed = DEFAULT_SPEED;
        zoomSpeed = 7f;
        rollSpeed = 2f;
    }

    @Override
    public void update() {
        up = Input.getKeyboard().isDown(KeyEvent.VK_UP);
        down = Input.getKeyboard().isDown(KeyEvent.VK_DOWN);
        right = Input.getKeyboard().isDown(KeyEvent.VK_RIGHT);
        left = Input.getKeyboard().isDown(KeyEvent.VK_LEFT);

        zooming = Input.getMouse().getMouseWheelRotation();
        rollLeft = Input.getKeyboard().isDown(KeyEvent.VK_Q);
        rollRight = Input.getKeyboard().isDown(KeyEvent.VK_E);

        reset = Input.getKeyboard().isPressed(KeyEvent.VK_R);

        if(Input.getMouse().isClicked(MouseEvent.BUTTON1, ClickType.PRESSED))
            mouseDragPoint = Draw.convertToUnit(Input.getMouse().getScreenPosition());

        if(Input.getMouse().isHold(1)) {
            Vector2f newMouseDragPoint = Draw.convertToUnit(Input.getMouse().getScreenPosition());
            Vector2f move = newMouseDragPoint.sub(mouseDragPoint, new Vector2f());
            scene.getCamera().getPosition().sub(move.div(scene.getCamera().getZoom()).rotate(scene.getCamera().getRoll()));
            mouseDragPoint = newMouseDragPoint;
        }
    }

    @Override
    public void fixedUpdate() {
        // Calculating
        Vector2f move = new Vector2f();
        if(up != down)
            move.setY(up ? -1 : 1);
        if(right != left)
            move.setX(left ? -1 : 1);
        // Roll
        float roll = 0;
        if(rollLeft != rollRight)
            roll = rollLeft ? -1 : 1;
        // Setting
        scene.getCamera().getPosition().add(move.normalize().mul(moveSpeed / BuildEngine.getEngine().getTargetFixedUpdates()));
        if(reset) {
            scene.getCamera().setZoom(1);
            scene.getCamera().setRoll(0);
            return;
        }
        if(zooming != 0) {
            float newZoom = scene.getCamera().getZoom() - zooming * (MathUtils.toFloat(
                    Math.pow(zoomSpeed / BuildEngine.getEngine().getTargetFixedUpdates(), 1 / (scene.getCamera().getZoom() + .5f))));
            if(newZoom > ZOOM_MAX) newZoom = ZOOM_MAX;
            if(newZoom < ZOOM_MIN) newZoom = ZOOM_MIN;
            Vector2f zoomMoveAdjust = new Vector2f(Draw.getWidth(), Draw.getHeight()).div(scene.getCamera().getZoom())
                    .sub(new Vector2f(Draw.getWidth(), Draw.getHeight()).div(newZoom)).div(2);
            scene.getCamera().getPosition().add(zoomMoveAdjust);
            scene.getCamera().setZoom(newZoom);
        }
        scene.getCamera().setRoll(scene.getCamera().getRoll() + roll * rollSpeed / BuildEngine.getEngine().getTargetFixedUpdates());
    }
}
