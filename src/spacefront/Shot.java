package spacefront;

import java.util.Set;

public abstract class Shot extends SpaceObject {

    public Shot(double speed,
                double startx, double starty,
                double destx, double desty) {
        super(startx, starty, 0);
        double a = Math.atan2(desty - starty, destx - startx);
        setSpeed(Math.cos(a) * speed, Math.sin(a) * speed, 0);
    }

    protected Meteoroid hit(Set<Meteoroid> targets) {
        for (Meteoroid m : targets) {
            if (m.get().contains(getX(), getY())) {
                return m;
            }
        }
        return null;
    }

    public Meteoroid step(Spacefront universe) {
        super.step();
        return hit(universe.getMeteoroids());
    }
}
