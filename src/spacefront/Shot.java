package spacefront;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Set;

public class Shot extends SpaceObject {

    private static final double SIZE = 8;
    private static final Shape SHAPE
        = new Ellipse2D.Double(-SIZE / 2, -SIZE / 2, SIZE, SIZE);

    private static final long serialVersionUID = 1L;

    private static final double SPEED = 8;

    public Shot(double destx, double desty) {
        super(0, 0, 0);
        double a = Math.atan2(desty, destx);
        setSpeed(Math.cos(a) * SPEED, Math.sin(a) * SPEED, 0);
        setShape(SHAPE);
    }

    public Meteoroid step(Set<Meteoroid> targets) {
        super.step();
        for (Meteoroid m : targets) {
            if (m.get().contains(getX(), getY())) {
                return m;
            }
        }
        return null;
    }
}
