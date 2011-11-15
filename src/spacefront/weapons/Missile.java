package spacefront.weapons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import spacefront.Meteoroid;
import spacefront.Shot;
import spacefront.Spacefront;

public class Missile extends Shot {

    private static final double SPEED = BasicShot.SPEED * 1.5;
    private static final double TURN_RATE = 0.2;
    private static final double HOMING_DISTANCE = 200;
    public static final Color COLOR1 = new Color(113, 36, 255);
    public static final Color COLOR2 = new Color(255, 81, 241);

    private final double targetX;
    private final double targetY;
    private Meteoroid target;

    public Missile(double startx, double starty, double destx, double desty) {
        super(SPEED, startx, starty, destx, desty);
        setPosition(getX(), getY(), Math.atan2(desty, destx));
        setShape(MissileShape.getHull());
        targetX = destx;
        targetY = desty;
    }

    private void targetNearest(Spacefront space) {
        double best = HOMING_DISTANCE * HOMING_DISTANCE;
        for (Meteoroid m : space.getMeteoroids()) {
            double x = targetX - m.getX();
            double y = targetY - m.getY();
            double d = x * x + y * y;
            if (d < best) {
                target = m;
                best = d;
            }
        }
    }

    @Override
    public Meteoroid step(Spacefront space) {
        if (space == null) {
            return null;
        }
        if (target == null || !target.isAlive()) {
            target = null;
            targetNearest(space);
        }

        /* Target position and angle. */
        double tx = targetX;
        double ty = targetY;
        if (target != null) {
            tx = target.getX();
            ty = target.getY();
        }
        double ta = Math.atan2(ty - getY(), tx - getX());

        /* Behave like a missle. */
        double a = getA();
        double dx = Math.cos(a) * SPEED;
        double dy = Math.sin(a) * SPEED;

        /* Turn at the target. */
        double da = ta - a;
        if (da > Math.PI) {
            da -= Math.PI * 2;
        } else if (da < -Math.PI) {
            da += Math.PI * 2;
        }
        if (da > TURN_RATE) {
            da = TURN_RATE;
        } else if (da < -TURN_RATE) {
            da = -TURN_RATE;
        }

        setSpeed(dx, dy, da);
        return super.step(space);
    }

    public void paint(Graphics2D g) {
        AffineTransform at = getTransform();
        g.setColor(COLOR2);
        g.fill(at.createTransformedShape(MissileShape.getDecoration()));
        g.setColor(COLOR1);
        g.fill(at.createTransformedShape(MissileShape.getHull()));
    }
}
