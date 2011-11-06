package spacefront;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Meteoroid extends SpaceObject {

    private static final long serialVersionUID = 1L;

    private static final Random RNG = new Random();

    List<java.lang.Double> edges = new ArrayList<java.lang.Double>();

    private double size;

    public Meteoroid(double startx, double starty) {
        super(startx, starty, 0);
        Path2D.Double shape = new Path2D.Double();
        int count = RNG.nextInt(10) + 10;
        size = RNG.nextGaussian() * 10 + 20;
        size = Math.max(size, 10);

        /* Build up edges. */
        boolean first = true;
        for (int i = 0; i < count; i++) {
            double d = RNG.nextGaussian() * (size / 5) + size;
            double a = i * (Math.PI * 2d / count);
            edges.add(d);
            double px = Math.cos(a) * d;
            double py = Math.sin(a) * d;
            if (first) {
                shape.moveTo(px, py);
                first = false;
            } else {
                shape.lineTo(px, py);
            }
        }
        shape.closePath();
        setShape(shape);

        double speed = Math.abs(RNG.nextGaussian() * 1 + 1);
        double a = Math.atan2(starty, startx);
        double dx = -Math.cos(a) * speed;
        double dy = -Math.sin(a) * speed;
        double da = RNG.nextGaussian() * 0.05;
        setSpeed(dx, dy, da);
    }

    public double getSize() {
        return size;
    }

    public boolean step(Planet home) {
        super.step();
        double radius2 = Math.pow(home.getRadius(), 2);
        int count = edges.size();
        for (int i = 0; i < count; i++) {
            double a = i * (Math.PI * 2d / count);
            double d = edges.get(i);
            double px = Math.cos(a) * d + getX();
            double py = Math.sin(a) * d + getY();
            if (px * px + py * py < radius2) {
                return true;
            }
        }
        return false;
    }

    public List<Debris> breakup() {
        List<Debris> parts = new ArrayList<Debris>();
        int count = edges.size();
        for (int i = 0; i < count; i++) {
            int p = i - 1;
            if (p < 0) {
                p = count - 1;
            }
            double a0 = p * (Math.PI * 2d / count);
            double a1 = i * (Math.PI * 2d / count);
            double d0 = edges.get(p);
            double d1 = edges.get(i);
            double px0 = Math.cos(a0) * d0;
            double py0 = Math.sin(a0) * d0;
            double px1 = Math.cos(a1) * d1;
            double py1 = Math.sin(a1) * d1;
            parts.add(new Debris(new Line2D.Double(px0, py0, px1, py1), this));
        }
        return parts;
    }


}
