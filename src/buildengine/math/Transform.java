package buildengine.math;

import buildengine.math.shape.Rectangle;
import buildengine.math.vector.Vector2f;

/**
 * This class contains information regarding the bounds of
 * 2D objects and shapes. This class is not attached to any
 * other class and can be instantiated at any point without
 * any further information.
 *
 * @author Kai v. Maurik
 */
public class Transform {

	public Vector2f position;
	public Vector2f size;
	public float rotation;

	public Transform() {
		this(new Vector2f());
	}

	public Transform(float x, float y) {
		this(x, y, 1, 1);
	}

	public Transform(float x, float y, float width, float height) {
		this(x, y, width, height, 0);
	}

	public Transform(float x, float y, float width, float height, float rotation) {
		this(new Vector2f(x, y), width, height, rotation);
	}

	public Transform(Vector2f position, float width, float height) {
		this(position, width, height, 0);
	}

	public Transform(Vector2f position, float width, float height, float rotation) {
		this(position, new Vector2f(width, height), rotation);
	}

	public Transform(Vector2f position) {
		this(position, new Vector2f(1));
	}

	public Transform(Vector2f position, Vector2f size) {
		this(position.x, position.y, size.x, size.y);
	}

	public Transform(Vector2f position, Vector2f size, float rotation) {
		set(position, size, rotation);
	}

	public Transform set(Vector2f position, Vector2f size, float rotation) {
		this.position = position;
		this.size = size;
		this.rotation = rotation;
		return this;
	}

	public Rectangle toRectangle() {
		return new Rectangle(position.x, position.y, size.x, size.y, rotation);
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
		return dest.set(position.add(x, y), size, rotation);
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	public float getWidth() {
		return size.x;
	}

	public float getHeight() {
		return size.y;
	}

	public Vector2f getSize() {
		return size;
	}

	public void setSize(Vector2f size) {
		this.size = size;
	}

	public void setSize(float width, float height) {
		setSize(width, height);
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	// Class utility

	public static Transform valueOf(String s) {
		String[] sub = s.split(";", 2);
		if(sub.length < 3)
			throw new NumberFormatException(s + " is not a valid bounds.");
		return new Transform(Vector2f.valueOf(sub[0]), Vector2f.valueOf(sub[1]), Float.parseFloat(sub[2]));
	}

	@Override
	public String toString() {
		return position.toString() + ";" + getSize().toString() + ";" + rotation;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Transform b) {
			return position.equals(b.getPosition()) && size.equals(b.getSize()) && rotation == b.getRotation();
		}
		return false;
	}

	@Override
	public Transform clone() {
		return new Transform(position, size, rotation);
	}
}
