package spacefront;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public abstract class SpaceObject {

    private static final Shape DEFAULT = new Ellipse2D.Double(-5, -5, 10, 10);

    private Shape shape = DEFAULT;
    private double x, dx, y, dy, a, da;

    public SpaceObject(SpaceObject o) {
        inherit(o);
    }

    public SpaceObject(double x, double y, double a) {
        this.x = x;
        this.y = y;
        this.a = a;
    }

    public void setSpeed(double dx, double dy, double da) {
        this.dx = dx;
        this.dy = dy;
        this.da = da;
    }

    public void adjustSpeed(double ddx, double ddy, double dda) {
        dx += ddx;
        dy += ddy;
        da += dda;
    }

    public void step() {
        x += dx;
        y += dy;
        a += da;
    }

    public void inherit(SpaceObject o) {
        this.x = o.x;
        this.y = o.y;
        this.a = o.a;
        this.dx = o.dx;
        this.dy = o.dy;
        this.da = o.da;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistance() {
        return Math.sqrt(x * x + y * y);
    }

    public Shape get() {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(a);
        return at.createTransformedShape(shape);
    }

    public abstract void paint(Graphics2D g);

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
