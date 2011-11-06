package spacefront;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Meteoroid extends Path2D.Double {

    private static final long serialVersionUID = 1L;

    private static final Random RNG = new Random();

    List<java.lang.Double> edges = new ArrayList<java.lang.Double>();

    private double x, dx;
    private double y, dy;

    public Meteoroid(double startx, double starty) {
        super();
        int count = RNG.nextInt(15) + 15;
        boolean first = true;
        for (int i = 0; i < count; i++) {
            double d = RNG.nextGaussian() * 3 + 20;
            double a = i * (Math.PI * 2d / count);
            edges.add(d);
            double px = Math.cos(a) * d;
            double py = Math.sin(a) * d;
            if (first) {
                moveTo(px, py);
                first = false;
            } else {
                lineTo(px, py);
            }
        }
        closePath();
        this.x = startx;
        this.y = starty;
        double speed = Math.abs(RNG.nextGaussian() * 1 + 1);
        double a = Math.atan2(starty, startx);
        dx = -Math.cos(a) * speed;
        dy = -Math.sin(a) * speed;
    }

    public boolean step(double home) {
        x += dx;
        y += dy;
        double home2 = home * home;
        int count = edges.size();
        for (int i = 0; i < count; i++) {
            double a = i * (Math.PI * 2d / count);
            double d = edges.get(i);
            double px = Math.cos(a) * d + x;
            double py = Math.sin(a) * d + y;
            if (px * px + py * py < home2) {
                return true;
            }
        }
        return false;
    }

    public double getX() {
        return x;
    }

    public double getDX() {
        return dx;
    }

    public double getY() {
        return y;
    }

    public double getDY() {
        return dy;
    }
}
