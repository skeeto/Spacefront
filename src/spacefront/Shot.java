package spacefront;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Set;

public class Shot {

    private static final double SIZE = 8;
    private static final Shape SHAPE
        = new Ellipse2D.Double(-SIZE / 2, -SIZE / 2, SIZE, SIZE);

    private static final long serialVersionUID = 1L;

    private static final double SPEED = 8;

    private double x, dx, y, dy;

    public Shot(double destx, double desty) {
        double a = Math.atan2(desty, destx);
        x = 0;
        y = 0;
        dx = Math.cos(a) * SPEED;
        dy = Math.sin(a) * SPEED;
    }

    public Meteoroid step(Set<Meteoroid> targets) {
        x += dx;
        y += dy;
        for (Meteoroid m : targets) {
            if (m.get().contains(x, y)) {
                return m;
            }
        }
        return null;
    }

    public double getDistance() {
        return Math.sqrt(x * x + y * y);
    }

    public Shape get() {
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        return at.createTransformedShape(SHAPE);
    }
}
