package spacefront.weapons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Set;
import spacefront.Meteoroid;
import spacefront.Shot;
import spacefront.Spacefront;

public class BasicShot extends Shot {

    private static final long serialVersionUID = 1L;

    /* Appearance */
    private static final double SIZE = 8;
    public static final Color COLOR = Color.RED;
    public static final Shape SHAPE =
        new Ellipse2D.Double(-SIZE / 2, -SIZE / 2, SIZE, SIZE);
    private static final double OSCILLATION_RATE = 32;
    private static final double OSCILLATION_SCALE = 3;

    /* Behavior */
    public static final double SPEED = 8;

    public BasicShot(double startx, double starty, double destx, double desty) {
        super(SPEED, startx, starty, destx, desty);
        setShape(SHAPE);
    }

    @Override
    public Meteoroid step(Spacefront space) {
        Meteoroid m = super.step(space);
        if (m != null) {
            space.removeObject(this);
        }
        return m;
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
