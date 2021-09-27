package buildengine.input.component;

import buildengine.core.Engine;
import buildengine.core.scene.Actor;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.input.Input;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MoveController extends Director implements MonoBehaviour {

    public static final float DEFAULT_SPEED = 7;

    protected boolean up, down, right, left;
    private Actor controlling;
    private float speed;

    public MoveController(Actor controlling) {
        this(controlling, DEFAULT_SPEED);
    }

    public MoveController(Actor controlling, float speed) {
        this.controlling = controlling;
        this.speed = speed;

        // Change to dynamic if not already set to make sure you can see movement
        if(!controlling.isDynamic())
            controlling.setDynamic(true);
    }

    @Override
    public void begin() {
    }

    @Override
    public void update(float dt) {
        up = Input.getKeyboard().isKeyDown(GLFW.GLFW_KEY_W);
        down = Input.getKeyboard().isKeyDown(GLFW.GLFW_KEY_S);
        right = Input.getKeyboard().isKeyDown(GLFW.GLFW_KEY_D);
        left = Input.getKeyboard().isKeyDown(GLFW.GLFW_KEY_A);
    }

    @Override
    public void fixedUpdate() {
        Vector2f move = new Vector2f(0);
        if(up != down)
            move.y = up ? 1 : -1;
        if(right != left)
            move.x = left ? -1 : 1;
        if(move.x == 0 && move.y == 0)
            return;
        move.normalize().mul(speed / Engine.FIXED_UPDATES_PER_SECOND);
        controlling.getTransform().getPosition().add(move);
    }

    public Actor getControlling() {
        return controlling;
    }

    public void setControlling(Actor controlling) {
        this.controlling = controlling;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
