package spacefront;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DemoController extends TimerTask {

    private static final Random RNG = new Random();
    private static final long DELAY = 100l;
    private static final long WEAPON_TIME = 5000l;

    private final Spacefront space;
    private final double accuracy;
    private final Timer timer = new Timer();

    private long lastWeaponChange = System.currentTimeMillis();

    public DemoController(Spacefront spacefront, double accuracy) {
        this.space = spacefront;
        this.accuracy = accuracy;
        timer.schedule(this, DELAY, DELAY);
    }

    @Override
    public void run() {
        /* Target the nearest meteoroid. */
        Meteoroid target = null;
        double d = Double.POSITIVE_INFINITY;
        for (Meteoroid m : space.getMeteoroids()) {
            if (m.getDistance() < d) {
                d = m.getDistance();
                target = m;
            }
        }
        if (target != null) {
            space.fire(true);
            /* Nudge the angle a little bit, for inaccuracy. */
            double x = target.getX();
            double y = target.getY();
            double a = Math.atan2(y, x);
            a += a * (1 - accuracy) * RNG.nextGaussian();
            space.fireXY(Math.cos(a) * d, Math.sin(a) * d);
        } else {
            space.fire(false);
        }

        long now = System.currentTimeMillis();
        if (now - lastWeaponChange > WEAPON_TIME) {
            List<Weapon> weapons = space.getWeapons();
            space.selectWeapon(weapons.get(RNG.nextInt(weapons.size())));
            lastWeaponChange = now;
        }
    }

    @Override
    public void finalize() {
        timer.cancel();
    }
}
