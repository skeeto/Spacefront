package spacefront.weapons;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class MissileShape extends Path2D.Double {

    private static final long serialVersionUID = 1L;

    private static final double LENGTH = 10;
    private static final double WIDTH = 3;
    private static final Shape HULL =
        new Rectangle2D.Double(-LENGTH / 2, -WIDTH / 2, LENGTH, WIDTH);

    private static final Shape DECORATION = new MissileShape();

    public static Shape getHull() {
        return HULL;
    }

    public static Shape getDecoration() {
        return DECORATION;
    }

    protected MissileShape() {
        /* Tail */
        moveTo(-LENGTH / 2 - WIDTH, -WIDTH * 1.5);
        lineTo(-LENGTH / 2 + WIDTH * 2,  0);
        lineTo(-LENGTH / 2 - WIDTH,  WIDTH * 1.5);
        lineTo(-LENGTH / 2, 0);
        closePath();
        /* Cone */
        moveTo(LENGTH / 2, -WIDTH * 0.5);
        lineTo(LENGTH / 2 + WIDTH * 2, 0);
        lineTo(LENGTH / 2,  WIDTH * 0.5);
        closePath();
    }
}
