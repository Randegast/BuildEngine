package buildengine.graphics;

import buildengine.BuildEngine;
import buildengine.math.Transform;
import buildengine.math.shape.Line;
import buildengine.math.shape.Rectangle;
import buildengine.math.vector.Vector2f;
import buildengine.math.vector.Vector2i;
import buildengine.utils.MathUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Utility class for java graphics drawing.
 *
 * <p>
 *     This class has the {@code unit} object, used to translate and scale the Draws.
 *     The screen therefore consists of units. The amount of units depends on the aspect
 *     ratio, and the standard unit product. Every time you use methods from this class
 *     it expects unit coordinates, with the exception of the {@code convert()} methods,
 *     witch are used to convert units to pixels on the screen, and vise versa.
 * </p>
 * <p>
 *     The class also contains a Transform, to apply to the drawing. Use the methods {@code
 *     Draw.translate(), Draw.scale() and Draw.rotate()} before drawing to apply transformations.
 * </p>
 */
public class Draw {

    public static final float UNIT = .5f;
    public static final Vector2f ASPECT_RATIO = new Vector2f(16, 9);

    public static final float DRAW_POINT_SIZE_MUL = 3;
    public static final Color DEFAULT_COLOR = Color.WHITE;

    private static boolean stretch;
    private static Dimension size;
    private static float drawOpacity = 1;

    /**
     * The Graphics object is used to draw things to
     * the screen though java's atw. This graphics
     * object is to be updated every frame.
     * This variable uses {@code java.awt.Graphics2D}.
     *
     * @see #prepare(Graphics2D)
     */
    private static Graphics2D g;

    /**
     * The Unit is the coordinate system.
     * This Vector2 object holds the information how
     * to interpret these units and how many pixels
     * one unit is.
     *
     * @see #resize(Dimension)
     * @see #getUnit
     * @see #setUnit
     */
    private static Vector2f unit;

    /**
     * The transform decides how the pixels are drawn
     * to the screen, and applies transformations before
     * drawing. Use the transform methods to alter this.
     * Directly altering it can have bad results.
     */
    private static Transform transform = new Transform();

    /**
     * Sets a new {@code Graphics2D} object for the Draw to use.
     * Always call this first in the render method of the Draw.
     *
     * @param graphics2D	up-to-date {@code Graphics2D} object.
     * @see #g
     */
    public static void prepare(Graphics2D graphics2D) {
        g = graphics2D;
        transform = new Transform();
    }
    private Draw() {}

    /**
     * Transform the Draw. After altering applies to all Draw calls.
     * @param transform the transform to apply
     */
    public static void applyTransform(Transform transform) {
        translate(transform.position);
        scale(transform.size);
        rotate(transform.rotation);
    }

    /**
     * Translate the transform of Draw. After altering applies to all Draw calls.
     * @param vector2f translation position in units
     */
    public static void translate(Vector2f vector2f) {
        transform.setPosition(vector2f);
    }

    /**
     * Scale the transform of Draw. After altering applies to all Draw calls.
     * @param size translation scale in units
     */
    public static void scale(Vector2f size) {
        transform.setSize(size);
    }

    /**
     * Rotate the transform of Draw. After altering applies to all Draw calls.
     * @param theta the rotation in radians
     */
    public static void rotate(float theta) {
        Vector2i center = new Vector2i(getCenter().mul(unit));
        g.rotate(theta, center.x, center.y);
        transform.setRotation(theta);
    }

    /**
     * Resets the transform of draw. After calling applies to all Draw calls.
     */
    public static void resetTransform() {
        g.setTransform(new AffineTransform());
        transform = new Transform();
    }

    /**
     * DrawImage method. This is the main method for rendering
     * graphics/images to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param image		image to draw.
     * @param position  the position to draw in units.
     * @param width     the width stretch in units.
     * @param height    the height stretch in units.
     */
    public static void drawImage(BufferedImage image, Vector2f position, float width, float height) {
        drawImage(image, position, width, height, 0, position);
    }

    /**
     * DrawImage method. This is the main method for rendering
     * graphics/images to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param image		image to draw.
     * @param position  the position to draw in units.
     * @param width     the width stretch in units.
     * @param height    the height stretch in units.
     * @param theta     the rotation
     * @param center	center, around witch the image should be rotated.
     */
    public static void drawImage(BufferedImage image, Vector2f position, float width, float height, double theta, Vector2f center) {
        Vector2i pos = convertPositionToPixel(position);
        Vector2i size = convertSizeToPixel(width, height);
        Vector2i cen = convertPositionToPixel(center);

        // Rotation
        AffineTransform backup = g.getTransform();
        g.rotate(theta, cen.x, cen.y);
        g.drawImage(image, MathUtils.toInt(pos.x), MathUtils.toInt(pos.y), MathUtils.toInt(size.x), MathUtils.toInt(size.y), null);
        g.setTransform(backup);
    }

    /**
     * Draws a specified string to a specified position on
     * the screen using the specified color, using the
     * default font. This method uses {@code java.awt.Graphics2D}.
     *
     * @param string	the string to draw.
     * @param position	position of the string in units.
     */
    public static void drawString(String string, Vector2f position) {
        drawString(string, position, DEFAULT_COLOR);
    }

    /**
     * Draws a specified string to a specified position on
     * the screen using the specified color, using the
     * default font. This method uses {@code java.awt.Graphics2D}.
     *
     * @param string	the string to draw.
     * @param position	position of the string in units.
     * @param color		color to draw in.
     */
    public static void drawString(String string, Vector2f position, Color color) {
        drawString(string, position, color, BuildEngine.FONT);
    }

    /**
     * Draws a string to the screen, with specified position,
     * color and font. This method uses {@code java.awt.Graphics2D}.
     *
     * @param string	the string to draw.
     * @param position	position of the string in units.
     * @param color		color to draw in.
     * @param font		font to use.
     */
    public static void drawString(String string, Vector2f position, Color color, Font font) {
        Vector2i pos = convertPositionToPixel(position);

        g.setColor(color);
        font.deriveFont(Font.PLAIN, (float) (font.getSize()));
        g.setFont(font);
        g.drawString(string, pos.x, pos.y);
    }

    /**
     * Draws a centered string to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param string	the string to draw.
     * @param position	position of the string in units(CENTERED).
     * @see #drawString
     */
    public static void drawCenteredString(String string, Vector2f position) {
        drawCenteredString(string, position, DEFAULT_COLOR);
    }

    /**
     * Draws a centered string to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param string	the string to draw.
     * @param position	position of the string in units(CENTERED).
     * @param color		color to draw in.
     * @see #drawString
     */
    public static void drawCenteredString(String string, Vector2f position, Color color) {
        drawCenteredString(string, position, color, BuildEngine.FONT);
    }

    /**
     * Draws a centered string to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param string	the string to draw.
     * @param position	position of the string in units(CENTERED).
     * @param color		color to draw in.
     * @param font		font to use.
     * @see #drawString
     */
    public static void drawCenteredString(String string, Vector2f position, Color color, Font font) {
        Vector2i pos = convertPositionToPixel(position);

        g.setColor(color);
        font.deriveFont(Font.PLAIN, (float) (font.getSize()));
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int xPos = pos.x - fontMetrics.stringWidth(string) / 2;
        int yPos = (pos.y - fontMetrics.getHeight() / 2) + fontMetrics.getAscent();

        g.setFont(font);
        g.drawString(string, xPos, yPos);
    }

    /**
     * Draws a rectangular shape to the screen.
     * This method uses {@code buildengine.math.shape.Rectangle}.
     *
     * @param rectangle The rectangle object to draw.
     * @param fill		Whether the rectangle should be filled or not.
     */
    public static void drawRect(Rectangle rectangle, boolean fill) {
        drawRect(rectangle, DEFAULT_COLOR, fill);
    }

    /**
     * Draws a rectangular shape to the screen.
     * This method uses {@code buildengine.math.shape.Rectangle}.
     *
     * @param rectangle The rectangle object to draw.
     * @param color		The color of the rectangle.
     * @param fill		Whether the rectangle should be filled or not.
     */
    public static void drawRect(Rectangle rectangle, Color color, boolean fill) {
        drawRect(new Vector2f(rectangle.x, rectangle.y), rectangle.getWidth(), rectangle.getHeight(), color, fill, rectangle.getRotation(), rectangle.getCenter());
    }

    /**
     * Draws a rectangular shape to the screen.
     * This method uses {@code buildengine.math.shape.Rectangle}.
     *
     * @param rectangle The rectangle object to draw.
     * @param position  Position to draw the rectangle.
     * @param color		The color of the rectangle.
     * @param fill		Whether the rectangle should be filled or not.
     */
    public static void drawRect(Rectangle rectangle, Vector2f position, Color color, boolean fill) {
        drawRect(position, rectangle.getWidth(), rectangle.getHeight(), color, fill, rectangle.getRotation(), rectangle.getCenter());
    }

    /**
     * Draws a rectangular shape to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param position  Position in units.
     * @param width     Width in units.
     * @param height    Height in units.
     * @param fill		Whether the rectangle should be filled or not.
     */
    public static void drawRect(Vector2f position, float width, float height, boolean fill) {
        drawRect(position, width, height, DEFAULT_COLOR, fill);
    }

    /**
     * Draws a rectangular shape to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param position  Position in units.
     * @param width     Width in units.
     * @param height    Height in units.
     * @param color		Color of the rectangle.
     * @param fill		Whether the rectangle should be filled or not.
     */
    public static void drawRect(Vector2f position, float width, float height, Color color, boolean fill) {
        drawRect(position, width, height, color, fill, 0, position);
    }

    /**
     * Draws a rectangular shape to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param position  the position to draw in units.
     * @param width     the width stretch in units.
     * @param height    the height stretch in units.
     * @param fill		whether the rectangle should be filled or not.
     * @param theta     the rotation
     * @param center    if you're working with rotations, different center point
     */
    public static void drawRect(Vector2f position, float width, float height, boolean fill, double theta, Vector2f center) {
        drawRect(position, width, height, DEFAULT_COLOR, fill, theta, center);
    }

    /**
     * Draws a rectangular shape to the screen.
     * This method uses {@code java.awt.Graphics2D}.
     *
     * @param position  the position to draw in units.
     * @param width     the width stretch in units.
     * @param height    the height stretch in units.
     * @param color		the color of the rectangle.
     * @param fill		whether the rectangle should be filled or not.
     * @param theta     the rotation
     * @param center    if you're working with rotations, different center point
     */
    public static void drawRect(Vector2f position, float width, float height, Color color, boolean fill, double theta, Vector2f center) {
        // Translate
        Vector2i pos = convertPositionToPixel(position);
        Vector2i size = convertSizeToPixel(width, height);
        Vector2i cen = convertPositionToPixel(center);
        // Drawing
        AffineTransform backup = g.getTransform();
        g.rotate(theta, cen.x, cen.y);
        g.setColor(color);
        g.drawRect(MathUtils.toInt(pos.x), MathUtils.toInt(pos.y), MathUtils.toInt(size.x), MathUtils.toInt(size.y));
        if(fill)
            g.fillRect(MathUtils.toInt(pos.x), MathUtils.toInt(pos.y), MathUtils.toInt(size.x), MathUtils.toInt(size.y));
        g.setTransform(backup);
    }

    /**
     * Draws a line to the screen, between two specified
     * points. This method uses {@code java.awt.Graphics2D}.
     *
     * @param a		starting point of the line.
     * @param b		end point of the line.
     */
    public static void drawLine(Vector2f a, Vector2f b) {
        drawLine(a, b, DEFAULT_COLOR);
    }

    /**
     * Draws a line to the screen, between two specified
     * points. This method uses {@code java.awt.Graphics2D}.
     *
     * @param a		starting point of the line.
     * @param b		end point of the line.
     * @param color	color of the line
     */
    public static void drawLine(Vector2f a, Vector2f b, Color color) {
        Vector2i posA = convertPositionToPixel(a);
        Vector2i posB = convertPositionToPixel(b);

        g.setColor(color);
        g.drawLine(MathUtils.toInt(posA.x), MathUtils.toInt(posA.y), MathUtils.toInt(posB.x), MathUtils.toInt(posB.y));
    }

    /**
     * Draws a line to the screen, between two specified
     * points. This method uses {@code java.awt.Graphics2D}.
     *
     * @param line	Line to draw.
     * @param color	color of the line
     */
    public static void drawLine(Line line, Color color) {
        Vector2i a = convertPositionToPixel(line.getVertices()[0]);
        Vector2i b = convertPositionToPixel(line.getVertices()[1]);

        g.setColor(color);
        g.drawLine(MathUtils.toInt(a.x), MathUtils.toInt(a.y), MathUtils.toInt(b.x), MathUtils.toInt(b.y));
    }

    /**
     * Draws a point to the screen, on a specified point.
     *
     * @param point The point to draw
     */
    public static void drawPoint(Vector2f point) {
        drawPoint(point, DEFAULT_COLOR);
    }

    /**
     * Draws a point to the screen, on a specified point.
     *
     * @param point The point to draw
     * @param color The color to draw it in
     */
    public static void drawPoint(Vector2f point, Color color) {
        Vector2i p = convertPositionToPixel(point);

        g.setColor(color);
        g.fillOval(MathUtils.toInt(p.x - DRAW_POINT_SIZE_MUL / 2), MathUtils.toInt(p.y  - DRAW_POINT_SIZE_MUL / 2),
                MathUtils.toInt(DRAW_POINT_SIZE_MUL), MathUtils.toInt(DRAW_POINT_SIZE_MUL));
    }

    /**
     * Sets the draw opacity for all following Draw calls.
     *
     * @param alpha A float value between 0 and 1 representing an opacity percentage
     * @throws IllegalArgumentException If the alpha value given is not withing bounds
     */
    public static void setDrawOpacity(float alpha) {
        if(alpha > 1 || alpha < 0)
            throw new IllegalArgumentException("Alpha must be a float between 0 and 1");
        drawOpacity = alpha;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    /**
     * Gets the current draw opacity.
     *
     * @return the current opacity as a float value between 0 and 1
     */
    public static float getDrawOpacity() {
        return drawOpacity;
    }

    /**
     * Recalculates the unit relative to the {@code #size}
     * and the {@code aspectRatio}.
     */
    public static void resize(Dimension newSize) {
        size = newSize;
        setUnit(new Vector2f((float) size.getWidth() / (ASPECT_RATIO.x / UNIT),
                (float) size.getHeight() / (ASPECT_RATIO.y / UNIT)));
    }

    /**
     * Converts unit coordinates to screen pixel coordinates and applies the draw transform.
     * @param vector2F   unit coordinate
     * @return          pixel coordinate
     */
    public static Vector2i convertPositionToPixel(Vector2f vector2F) {
        return new Vector2i(vector2F.add(transform.getPosition(), new Vector2f())
                .mul(transform.getSize()).mul(unit));
    }

    /**
     * Converts unit sizes to screen pixel coordinates and applies the draw transform.
     * @param width     Width in units
     * @param height    Height in units
     * @return          Pixel coordinate
     */
    public static Vector2i convertSizeToPixel(float width, float height) {
        return new Vector2i(new Vector2f(width, height).mul(transform.getSize()).mul(unit));
    }

    /**
     * Converts pixel coordinates to draw unit coordinates and takes the draw transform into account.
     * @param vector2i  pixel coordinate
     * @return          unit coordinate
     */
    public static Vector2f convertToUnit(Vector2i vector2i) {
        return MathUtils.rotatePoint(-transform.getRotation(), getCenter().div(transform.getSize()).sub(transform.getPosition()),
                new Vector2f(vector2i).div(unit).div(transform.getSize()).sub(transform.getPosition()));
    }

    /**
     * Gets the Unit size of a specified string.
     * @param font      The font the string is in
     * @param string    The string width to be calculated
     * @return A Vector representing with the X value being the string width,
     *         and Y value being the height of the font in Units.
     */
    public static Vector2f getStringSize(Font font, String string) {
        return Draw.convertToUnit(new Vector2i(g.getFontMetrics(font).stringWidth(string), g.getFontMetrics(font).getHeight()));
    }

    /**
     * Check if horizontal stretching is active. The screen will
     * resize horizontally too with this enabled.
     * @return          true if vStretch is enabled
     */
    public static boolean isStretch() {
        return stretch;
    }

    /**
     * Allows you to enable horizontal stretching. The screen will
     * resize horizontal too with this enabled.
     * @param stretch  true - enables vStretch
     *                  false(default) - disables vStretch; only resizes vertically
     */
    public static void setStretch(boolean stretch) {
        Draw.stretch = stretch;
        Draw.resize(size);
    }

    /**
     * Changes the unit
     * @param newUnit	new unit
     * @see #unit
     */
    public static void setUnit(Vector2f newUnit) {
        // If stretch is disabled, scale based on height
        if(!stretch)
            unit = new Vector2f(newUnit.y);
        else
            unit = newUnit;
    }

    /**
     * @return the unit object.
     * @see #unit
     */
    public static Vector2f getUnit() {
        return unit;
    }

    /**
     * Gets the current transformation of the Draw
     * @return a copy of the current Transform object
     */
    public static Transform getTransform() {
        return transform.duplicate();
    }

    public static Graphics2D getGraphics() {
        return g;
    }

    /**
     * @return the screen width in units
     */
    public static float getWidth() {
        return size.width / unit.x;
    }
    /**
     * @return the screen height in units
     */
    public static float getHeight() {
        return size.height / unit.y;
    }
    /**
     * @return the screen center in units
     */
    public static Vector2f getCenter() {
        return new Vector2f(getWidth() / 2, getHeight() / 2);
    }

}
