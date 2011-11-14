package spacefront;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

/**
 * Represents an object in space. This abstract base manages position
 * and rotation information.
 */
public abstract class SpaceObject {

    /** The default object shape. */
    private static final Shape DEFAULT = new Ellipse2D.Double(-5, -5, 10, 10);

    private boolean alive = true;

    /** This object's shape. */
    private Shape shape = DEFAULT;

    /** This object's position and rotation. */
    private double x, dx, y, dy, a, da;

    /**
     * Create a new object inheriting another object's position and rotation.
     * @param o  the object to inherit from
     */
    public SpaceObject(SpaceObject o) {
        inherit(o);
    }

    /**
     * Create a new object at the given position and rotation.
     * @param x  x-position
     * @param y  y-position
     * @param a  rotation
     */
    public SpaceObject(double x, double y, double a) {
        this.x = x;
        this.y = y;
        this.a = a;
    }

    /**
     * Set this object's position.
     * @param x  x-position
     * @param y  y-position
     * @param a  angle
     */
    public void setPosition(double x, double y, double a) {
        this.x = x;
        this.y = y;
        this.a = a;
    }

    /**
     * Set this object's speeds.
     * @param dx  speed in x
     * @param dy  speed in y
     * @param da  speed in a
     */
    public void setSpeed(double dx, double dy, double da) {
        this.dx = dx;
        this.dy = dy;
        this.da = da;
    }

    /**
     * Modify this object speed by some amount.
     @param ddx  the change in dx
     @param ddy  the change in dy
     @param dda  the change in da
     */
    public void adjustSpeed(double ddx, double ddy, double dda) {
        dx += ddx;
        dy += ddy;
        da += dda;
    }

    /**
     * Advance this object's position by one step.
     */
    public void step() {
        x += dx;
        y += dy;
        a += da;
    }

    /**
     * Inherit another object's position and rotation.
     * @param o  the object to copy
     */
    public void inherit(SpaceObject o) {
        this.x = o.x;
        this.y = o.y;
        this.a = o.a;
        this.dx = o.dx;
        this.dy = o.dy;
        this.da = o.da;
    }

    /**
     * Get the X position of this object.
     * @return the x position
     */
    public double getX() {
        return x;
    }

    /**
     * Get the Y position of this object.
     * @return the y position
     */
    public double getY() {
        return y;
    }

    /**
     * Get the angle of this object.
     * @return the angle
     */
    public double getA() {
        while (a > Math.PI) {
            a -= Math.PI * 2;
        }
        while (a < -Math.PI) {
            a += Math.PI * 2;
        }
        return a;
    }

    /**
     * Get the distance of this object from the player.
     * @return the distance of this shape from the origin
     */
    public double getDistance() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Get the transformed shape for this object.
     * @return the transformed shape
     */
    public Shape get() {
        return getTransform().createTransformedShape(shape);
    }

    /**
     * Get the positional and rotational transform.
     * @return the spatial transform for this object
     */
    public AffineTransform getTransform() {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(a);
        return at;
    }

    /**
     * Get this object's shape.
     * @return this object's shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Set this object's shape.
     * @param shape  this object's new shape
     */
    protected void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * Paint this object onto a graphics context.
     * @param g  the graphics context to be painted to
     */
    public abstract void paint(Graphics2D g);

    /**
     * Get object's living status.
     * @return true if this object is alive and active
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Set object's living status.
     * @param set  the new living status
     */
    public void setAlive(boolean set) {
        alive = set;
    }
}
