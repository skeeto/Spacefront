package spacefront;

import java.util.Timer;
import java.util.TimerTask;

public class DemoController extends TimerTask {

    private static final long DELAY = 100l;

    private Spacefront space;
    private final Timer timer = new Timer();

    public DemoController(Spacefront spacefront) {
        this.space = spacefront;
        timer.schedule(this, DELAY, DELAY);
    }

    @Override
    public void run() {
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
            space.fireXY(target.getX(), target.getY());
        } else {
            space.fire(false);
        }
    }
}
