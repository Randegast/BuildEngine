package buildengine.utils;

import buildengine.math.vector.Vector2f;
import buildengine.math.vector.Vector2i;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A handful of utilities related to math and numbers.
 */
public class MathUtils {

	private MathUtils() {}

	/**
	 * Round any double, to a specific number of decimals
	 * @param value		The value to round
	 * @param places	The number of decimals
	 * @return			The rounded double
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	/**
	 * Safely converts any double/float value to an int. Rounds mathematically.
	 * @param value	The double/float to convert
	 * @return The converted int
	 */
	public static int toInt(double value) {
		return (int) Math.round(value);
	}

	/**
	 * Safely converts any double/float value to an int. Rounds mathematically.
	 * @param value	The double/float to convert
	 * @return The converted int
	 */
	public static float toFloat(double value) {
		return Double.valueOf(value).floatValue();
	}

	/**
	 * Gets the lowest of a number array
	 * @param numbers	Array of numbers
	 * @return			The lowest double.
	 */
	public static double min(double... numbers) {
		double smallest = Double.MAX_VALUE;
        for (double number : numbers) {
            if (smallest > number) {
                smallest = number;
            }
        }
		return smallest;
	}

	/**
	 * Gets the highest of a number array
	 * @param numbers	Array of numbers
	 * @return			The lowest double.
	 */
	public static double max(double... numbers) {
		double biggest = Double.MIN_VALUE;

        for (double number : numbers) {
            if (biggest < number) {
                biggest = number;
            }
        }

		return biggest;
	}

	/**
	 * Rotate a point around a center object
	 * @param theta The amount of rotation in theta
	 * @param center The center point
	 * @param point	The point to rotate
	 * @return A new rotated point
	 */
	public static Vector2f rotatePoint(double theta, Vector2f center, Vector2f point) {
		//Moving the Block to the origin
		Vector2f pos = point.sub(center, new Vector2f());
		//Rotate around the center of this object
		Vector2f newPos = new Vector2f((float) (pos.x * Math.cos(theta) - pos.y * Math.sin(theta)),
				(float) (pos.x * Math.sin(theta) + pos.y * Math.cos(theta)));
		return newPos.add(center);
	}

	/**
	 * Rotate a point around a center object
	 * @param theta The amount of rotation in theta
	 * @param center The center point
	 * @param point	The point to rotate
	 * @return A new rotated point
	 */
	public static Vector2i rotatePoint(double theta, Vector2i center, Vector2i point) {
		//Moving the Block to the origin
		Vector2i pos = point.sub(center, new Vector2i());
		//Rotate around the center of this object
		Vector2i newPos = new Vector2i(MathUtils.toInt(pos.x * Math.cos(theta) - pos.y * Math.sin(theta)),
				MathUtils.toInt(pos.x * Math.sin(theta) + pos.y * Math.cos(theta)));
		return newPos.add(center);
	}

	/**
	 * Restrains a value between two bounds
	 * @param value		The value to be constrained
	 * @param minValue	The floor value
	 * @param maxValue	The ceiling value
	 * @return The min or max if the value falls out of the given bounds. Otherwise, simpy returns the value.
	 */
	public static float constrain(float value, float minValue, float maxValue) {
		if(value <= minValue)
			return minValue;
		return Math.min(value, maxValue);
	}

	/**
	 * Interpolate a positive of negative double, based on a delta time object and goal
	 * @param goal The goal to interpolate to
	 * @param current The current progress toward that goal
	 * @param deltaTime The delta time, or the speed to interpolate with
	 * @return A new double representing the updated current value
	 */
	public static double interpolate(double goal, double current, double deltaTime) {
		double difference = goal - current;

		if(difference > deltaTime)
			return current + deltaTime;
		if(difference < -deltaTime)
			return current - deltaTime;

		return goal;
	}

	/**
	 * Interpolate a positive of negative point, based on a delta time object and goal
	 * @param goal The goal to interpolate to
	 * @param current The current progress toward that goal
	 * @param deltaTime The delta time, or the speed to interpolate with
	 * @return A new Vector representing the updated current point
	 */
	public static Vector2f interpolate(Vector2f goal, Vector2f current, double deltaTime) {
		return new Vector2f((float) interpolate(goal.x, current.x, deltaTime), (float) interpolate(goal.y, current.y, deltaTime));
	}
}
