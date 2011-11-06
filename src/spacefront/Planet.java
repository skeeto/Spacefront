package spacefront;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Random;

public class Planet {

    private final Random RNG = new Random(19);

    private static final Color BASE_COLOR = Color.BLUE;
    private static final Color LAND_COLOR = new Color(0f, 0.7f, 0f);
    private static final Color CLOUD_COLOR = new Color(1f, 1f, 1f, 0.75f);

    private static final double DEAD_MASS = 300 * 300 * Math.PI;
    private double mass = 2500;
    private float health = 1f;

    private final Shape base;
    private final Path2D.Double land;
    private final Path2D.Double clouds;

    public Planet() {
        base = new Ellipse2D.Double(-1, -1, 2, 2);

        land = blob(3);
        int count = RNG.nextInt(2) + 3;
        for (int i = 0; i < count; i++) {
            land.append(blob(3), false);
        }

        clouds = blob(3);
        count = RNG.nextInt(2) + 3;
        for (int i = 0; i < count; i++) {
            clouds.append(blob(3), false);
        }
    }

    private Path2D.Double blob(int points) {
        Path2D.Double path = new Path2D.Double();
        int count = (int) (RNG.nextGaussian() * (points / 2d) + points);
        boolean first = true;
        for (int i = 0; i < count; i++) {
            if (first) {
                double[] xy = circlePair();
                path.moveTo(xy[0], xy[1]);
                first = false;
            } else {
                double[] xy1 = circlePair();
                double[] xy2 = circlePair();
                path.quadTo(xy1[1], xy1[1], xy2[0], xy2[1]);
            }
        }
        return path;
    }

    private double[] circlePair() {
        double a = RNG.nextDouble() * Math.PI * 2;
        double d = RNG.nextDouble() * 2;
        double x = Math.cos(a) * d;
        double y = Math.sin(a) * d;
        return new double[] {x, y};
    }

    public double getRadius() {
        return Math.sqrt(mass) / Math.PI;
    }

    public void absorb(Meteoroid m) {
        mass += m.getSize() * 100;
        health -= m.getSize() / 100f;
    }

    public float getHealth() {
        return health;
    }

    public void paint(Graphics2D g) {
        double r = getRadius();
        AffineTransform at = AffineTransform.getScaleInstance(r, r);
        Shape clip = at.createTransformedShape(base);
        g.clip(clip);
        g.setColor(BASE_COLOR);
        g.fill(clip);
        g.setColor(LAND_COLOR);
        g.fill(at.createTransformedShape(land));
        g.setColor(CLOUD_COLOR);
        g.fill(at.createTransformedShape(clouds));
    }
}
