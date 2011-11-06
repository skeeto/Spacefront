package spacefront;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Planet {

    private static final double DEAD_MASS = 300 * 300 * Math.PI;
    private double mass = 2500;
    private float health = 1f;

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
        Shape home = new Ellipse2D.Double(-r, -r, r * 2, r * 2);
        g.setColor(Color.GREEN);
        g.fill(home);
    }
}
