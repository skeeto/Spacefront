package spacefront;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

public class Spacefront extends Observable implements Runnable {

    private static final Random RNG = new Random();
    private static final long DELAY = 33;
    private static final int SIDE = 600;
    private static final Dimension SIZE = new Dimension(SIDE, SIDE);

    private static final long FIRE_DELAY = 200;

    private Set<Meteoroid> meteoroids = new HashSet<Meteoroid>();
    private Set<Shot> shots = new HashSet<Shot>();
    private Set<Debris> debris = new HashSet<Debris>();

    private Planet home = new Planet();

    private Weapon weapon = new BasicWeapon();
    private long lastFire;
    private boolean firing = false;
    private double fireX, fireY;
    private double danger = 0.05;
    private double difficulty = 0.00001;
    private boolean running = false;

    public Spacefront() {
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            synchronized (this) {
                if (firing) {
                    long now = System.currentTimeMillis();
                    long diff = now - lastFire;
                    if (diff >= FIRE_DELAY) {
                        shots.add(weapon.fire(fireX, fireY));
                        lastFire = now;
                    }
                }

                danger += difficulty;
                if (RNG.nextFloat() < danger) {
                    double d = SIDE / 2d * Math.sqrt(2);
                    double a = RNG.nextDouble() * Math.PI * 2;
                    double x = Math.cos(a) * d;
                    double y = Math.sin(a) * d;
                    meteoroids.add(new Meteoroid(x, y));
                }
                Set<Meteoroid> dead = new HashSet<Meteoroid>();
                Set<Shot> spent = new HashSet<Shot>();
                Set<Debris> old = new HashSet<Debris>();
                for (Debris d : debris) {
                    d.step();
                    if (d.getTTL() <= 0f) {
                        old.add(d);
                    }
                }
                for (Meteoroid m : meteoroids) {
                    if (m.step(home)) {
                        dead.add(m);
                        home.absorb(m);
                    }
                }
                for (Shot s : shots) {
                    Meteoroid m = s.step(meteoroids);
                    if (m != null) {
                        dead.add(m);
                        spent.add(s);
                        debris.addAll(m.breakup());
                    } else if (s.getDistance() > SIDE) {
                        spent.add(s);
                    }
                }
                meteoroids.removeAll(dead);
                shots.removeAll(spent);
                debris.removeAll(old);
                if (home.getHealth() <= 0) {
                    System.out.println("Game over");
                    running = false;
                    meteoroids.clear();
                    shots.clear();
                    debris.clear();
                }
            }
            setChanged();
            notifyObservers();
            sleep(DELAY);
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            return;
        }
    }

    public Dimension getSize() {
        return SIZE;
    }

    public Planet getHome() {
        return home;
    }

    public synchronized Set<Meteoroid> getMeteoroids() {
        return new HashSet<Meteoroid>(meteoroids);
    }

    public synchronized Set<Shot> getShots() {
        return new HashSet<Shot>(shots);
    }

    public synchronized Set<Debris> getDebris() {
        return new HashSet<Debris>(debris);
    }

    public void fireXY(double x, double y) {
        if (firing) {
            fireX = x;
            fireY = y;
        }
    }

    public void fire(boolean enabled) {
        firing = enabled;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
