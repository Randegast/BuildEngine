package buildengine.math;

import org.joml.Vector2f;

/**
 * This class contains information regarding the bounds and
 * positions of 2D objects and shapes. This class is not attached to any
 * other class and can be instantiated at any point without
 * any further information.
 *
 * @author Kai v. Maurik
 * @since GL 0.1 simplified and added zIndex
 */
public class Transform {

	public Vector2f position;
	public float width, height;
	public float rotation;

	public Transform() {
		this(new Vector2f());
	}

	public Transform(Vector2f position) {
		this(position, 1, 1);
	}

	public Transform(Vector2f position, float width, float height) {
		this(position, width, height, 0);
	}

	public Transform(Vector2f position, float width, float height, float rotation) {
		set(position.get(new Vector2f()), width, height, rotation);
	}

	public Transform set(Vector2f position, float width, float height, float rotation) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		return this;
	}

	public Transform move(Vector2f v) {
		return move(v, this);
	}

	public Transform move(float x, float y) {
		return move(x, y, this);
	}

	public Transform move(Vector2f v, Transform dest) {
		return move(v.x, v.y, dest);
	}

	public Transform move(float x, float y, Transform dest) {
		return dest.set(position.add(x, y), width, height, rotation);
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		setPosition(position.x, position.y);
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	// Class utility

	@Override
	public String toString() {
		return position.toString() + ";" + new Vector2f(width, height) + ";" + rotation;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Transform) {
			Transform b = (Transform) obj;
			return position.equals(b.getPosition()) && width == b.width && height == b.height &&
					rotation == b.getRotation();
		}
		return false;
	}

	public Transform copy() {
		return new Transform(position, width, height, rotation);
	}
}
