package buildengine.input;

import buildengine.BuildEngine;
import buildengine.core.Debug;
import buildengine.graphics.Draw;
import buildengine.math.vector.Vector2f;
import buildengine.math.vector.Vector2i;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * All mouse movement and click handling.
 * @see Input for the static instance
 */
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	private static final int BUTTON_COUNT = 20;

	private boolean[] released, pressed, hold;
	private int mouseWheelRotation;
	private Vector2i position;

	/** Initialisation */
	public void init() {
		released = new boolean[BUTTON_COUNT];
		pressed = new boolean[BUTTON_COUNT];
		hold = new boolean[BUTTON_COUNT];
		reset();
	}

	/** Updates after input circle */
	public void pollEvents() {
		for (int i = 0; i < BUTTON_COUNT; i++) {
			if(released[i])
				released[i] = false;
			if(pressed[i])
				pressed[i] = false;
		}
		mouseWheelRotation = 0;
	}

	/** Resets all values */
	public void reset() {
		Arrays.fill(released, false);
		Arrays.fill(pressed, false);
		Arrays.fill(hold, false);
		mouseWheelRotation = 0;
		position = new Vector2i();
	}

	// Retrieving Data

	/**
	 * Checks if a key is currently being clicked in any form.
	 * @param key	The keycode of the key that's possibly being clicked.
	 * @param type	The type of the click. For example {@code ClickType.HOLD}
	 * @return If the key is being pressed or not.
	 */
	public boolean isClicked(int key, ClickType type) {
		switch (type) {
			case NO_ACTION -> { return !pressed[key] && !released[key] && !hold[key]; }
			case PRESSED -> {
				return pressed[key];
			}
			case RELEASED -> {
				return released[key];
			}
			case HOLD -> {
				return hold[key];
			}
		}
		return false;
	}

	// Setting cursor shortcut
	public void setCursor(int type) {
		BuildEngine.getEngine().getDisplay().setCursor(new Cursor(type));
	}

	// Getting cursor shortcut
	public Cursor getCursor() {
		return BuildEngine.getEngine().getDisplay().getCursor();
	}

	public boolean isHold(int button) {
		return isClicked(button, ClickType.HOLD);
	}

	public boolean isPressed(int button) {
		return isClicked(button, ClickType.PRESSED);
	}

	public boolean isReleased(int button) {
		return isClicked(button, ClickType.RELEASED);
	}

	/**
	 * @return the current mouse position (pixels)
	 */
	public Vector2i getScreenPosition() {
		return position.duplicate();
	}

	/**
	 * Gets the cursor position in Draw units. Remember: Draw is static and doesn't take a scene camera intro account.
	 * @see #getScreenPosition() for exact camera pixel position
	 * @return The cursor position relative to the stage.
	 */
	public Vector2f getPosition() {
		return Draw.convertToUnit(Input.getMouse().getScreenPosition());
	}

	/**
	 * Gets the rotation of the mouse wheel. Each rotation "flick" returns
	 * another value.
	 * @return -1 when turning away from the user, 1 when turning
	 * 			towards and 0 if there is no rotation.
	 */
	public int getMouseWheelRotation() {
		return mouseWheelRotation;
	}

	/**
	 * Checks for mouse wheel movement
	 * @return if the mouse wheel is rotating
	 */
	public boolean isMouseWheelRotating() {
		return mouseWheelRotation != 0;
	}

	// Listeners

	@Override
	public void mousePressed(MouseEvent e) {
		if(isOutOfScope(e.getButton()))
			return;
		hold[e.getButton()] = true;
		pressed[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isOutOfScope(e.getButton()))
			return;
		hold[e.getButton()] = false;
		released[e.getButton()] = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		position.set(e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		position.set(e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	private boolean isOutOfScope(int button) {
		if(button >= BUTTON_COUNT) {
			Debug.msg("Did not handle mouse event (out of scope) for button " + button);
			return true;
		}
		return false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		position.set(e.getX(), e.getY());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		position.set(0, 0);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelRotation = e.getWheelRotation();
	}

}
