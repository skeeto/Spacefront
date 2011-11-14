package spacefront;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Grenade extends Shot {

    private static final Random RNG = new Random();

    /* Appearance */
    private static final double SIZE = 8;
    private static final double SPIN = Math.PI / 8;
    public static final Color COLOR = new Color(1f, 0.5f, 0f);
    public static final Shape SHAPE =
        new Rectangle2D.Double(-SIZE / 2, -SIZE / 2, SIZE, SIZE);
    private static final double OSCILLATION_RATE = 32;
    private static final double OSCILLATION_SCALE = 3;

    /* Behavior */
    private static final double SPEED = BasicShot.SPEED;
    private static final int COUNT = 8;
    private static final double THRESHOLD = SPEED * SPEED;

    private double explodeX;
    private double explodeY;

    public Grenade(double startx, double starty, double destx, double desty) {
        super(SPEED, startx, starty, destx, desty);
        adjustSpeed(0, 0, SPIN);
        setShape(SHAPE);
        explodeX = destx;
        explodeY = desty;
    }

    public Meteoroid step(Spacefront universe) {
        Meteoroid hit = super.step(universe);
        if (universe == null) {
            return null;
        }
        double dx = getX() - explodeX;
        double dy = getY() - explodeY;
        double offset = RNG.nextDouble() * Math.PI;
        if (hit != null || (dx * dx + dy * dy) < THRESHOLD) {
            for (int i = 0; i < COUNT; i++) {
                double a = i * (Math.PI * 2d / COUNT) + offset;
                double x = Math.cos(a) + getX();
                double y = Math.sin(a) + getY();
                Shot s = new BasicShot(getX(), getY(), x, y);
                universe.addObject(s);
            }
            universe.removeObject(this);
        }
        return hit;
    }

    public void paint(Graphics2D g) {
        g.setColor(COLOR);
        g.draw(get());
    }
}
