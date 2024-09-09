package buildengine.game;

import buildengine.BuildEngine;
import buildengine.core.scene.Actor;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.graphics.renderer.Animation;
import buildengine.input.Input;
import buildengine.math.vector.Vector2f;

import java.awt.event.KeyEvent;

/**
 *  Director containing a basic character controller functionality.
 */
public class BasicCharacterController extends Director implements MonoBehaviour {

    private boolean up, down, right, left;
    /** Animation images */
    private final Animation[] characterAnimations;
    private final Actor controlling;

    public static final float DEFAULT_SPEED = 5;

    private float speed; // Units per second
    private boolean moving;
    private Mode controlMode;

    public enum Mode { WASD, AD_SPACE, ARROWS }

    /**
     * Creates a basic character controller
     * @param controlling   The actor controlling
     * @param controlMode   The keys to use
     * @param images        The animations depending on direction as laid out bellow:
     *                      [0] Down
     *                      [1] Up
     *                      [2] Left
     *                      [3] Right
     */
    public BasicCharacterController(Actor controlling, Mode controlMode, Animation... images) {
        this(controlling, controlMode, DEFAULT_SPEED, images);
    }

    /**
     * Creates a basic character controller
     * @param controlling   The actor controlling
     * @param controlMode   The keys to use
     * @param speed         The speed of the actor in units/second
     * @param images        The animations depending on direction as laid out bellow:
     *                      [0] Down
     *                      [1] Up
     *                      [2] Left
     *                      [3] Right
     */
    public BasicCharacterController(Actor controlling, Mode controlMode, float speed, Animation... images) {
        this.controlling = controlling;
        this.controlMode = controlMode;
        this.speed = speed;

        this.characterAnimations = images;
    }

    @Override
    public void begin() {

    }

    @Override
    public void update() {
        switch (controlMode) {
            case WASD -> {
                up = Input.getKeyboard().isDown(KeyEvent.VK_W);
                down = Input.getKeyboard().isDown(KeyEvent.VK_S);
                right = Input.getKeyboard().isDown(KeyEvent.VK_D);
                left = Input.getKeyboard().isDown(KeyEvent.VK_A);
            }
            case AD_SPACE -> {
                up = Input.getKeyboard().isDown(KeyEvent.VK_SPACE);
                right = Input.getKeyboard().isDown(KeyEvent.VK_D);
                left = Input.getKeyboard().isDown(KeyEvent.VK_A);
            }
            case ARROWS -> {
                up = Input.getKeyboard().isDown(KeyEvent.VK_UP);
                down = Input.getKeyboard().isDown(KeyEvent.VK_DOWN);
                right = Input.getKeyboard().isDown(KeyEvent.VK_RIGHT);
                left = Input.getKeyboard().isDown(KeyEvent.VK_LEFT);
            }
        }
    }

    @Override
    public void fixedUpdate() {
        Vector2f move = new Vector2f();
        if(up != down)
            move.setY(up ? -1 : 1);
        if(right != left)
            move.setX(left ? -1 : 1);
        controlling.getTransform().getPosition().add(move.normalize().mul(speed / BuildEngine.getEngine().getTargetFixedUpdates()));

        // Animations
        if(characterAnimations != null && characterAnimations.length > 3) {
            if(move.x == 0 && move.y == 0) {
                moving = false;
                controlling.getSprite().getAnimation().setPlaying(false);
                controlling.getSprite().getAnimation().setIndex(0);
                return;
            }
            // Start moving
            if(!moving) {
                moving = true;
                controlling.getSprite().getAnimation().setPlaying(true);
            }
            // Change
            if(move.x < 0)
                controlling.getSprite().set(characterAnimations[2]);
            else if(move.x > 0) controlling.getSprite().set(characterAnimations[3]);
            else if(move.y < 0) controlling.getSprite().set(characterAnimations[1]);
            else if(move.y > 0) controlling.getSprite().set(characterAnimations[0]);
        }
    }

    public float getSpeed() {
        return speed;
    }

    /**
     * Change the speed of the controller
     * @param speed speed in units per seconds
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Mode getControlMode() {
        return controlMode;
    }

    public void setControlMode(Mode controlMode) {
        this.controlMode = controlMode;
    }
}
