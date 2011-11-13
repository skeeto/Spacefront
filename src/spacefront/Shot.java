package spacefront;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Set;

public class Shot extends SpaceObject {

    private static final long serialVersionUID = 1L;

    /* Appearance */
    private static final double SIZE = 8;
    public static final Color COLOR = Color.RED;
    public static final Shape SHAPE =
        new Ellipse2D.Double(-SIZE / 2, -SIZE / 2, SIZE, SIZE);
    private static final double OSCILLATION_RATE = 32;
    private static final double OSCILLATION_SCALE = 3;

    /* Behavior */
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

    public void paint(Graphics2D g) {
        g.setColor(COLOR);
        Shape self = get();
        long time = System.currentTimeMillis();
        double scale = Math.sin(time / OSCILLATION_RATE) /
            OSCILLATION_SCALE + 1d;
        AffineTransform at = getTransform();
        at.scale(scale, scale);
        g.fill(at.createTransformedShape(getShape()));
    }
}
