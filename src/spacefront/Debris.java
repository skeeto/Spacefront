package spacefront;

import java.awt.Shape;
import java.util.Random;

public class Debris extends SpaceObject {

    private static final Random RNG = new Random();

    private int ttl, maxttl;
    private int x, dx, y, dy;

    public Debris(Shape shape, SpaceObject source) {
        super(source);
        setShape(shape);
        adjustSpeed(RNG.nextGaussian() * 0.5,
                    RNG.nextGaussian() * 0.5,
                    RNG.nextGaussian() * 0.01);
        maxttl = ttl = (int) (RNG.nextGaussian() * 20 + 45);
        if (maxttl <= 0) {
            maxttl = 1;
        }
    }

    public float getTTL() {
        ttl--;
        float clamped = Math.min(1f, Math.max(0f, ttl * 1f / maxttl));
        return (float) Math.sqrt(clamped);
    }
}
