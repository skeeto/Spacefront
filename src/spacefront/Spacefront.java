package spacefront;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Spacefront extends Observable implements Runnable, Observer {

    private static final Random RNG = new Random();

    /* Operational parameters. */
    private static final long DELAY = 33;
    private static final int SIDE = 600;
    private static final Dimension SIZE = new Dimension(SIDE, SIDE);

    /* Collections */
    private Set<Meteoroid> meteoroids = new HashSet<Meteoroid>();
    private Set<Shot> shots = new HashSet<Shot>();
    private Set<Debris> debris = new HashSet<Debris>();
    private Queue<SpaceObject> incoming =
        new ConcurrentLinkedQueue<SpaceObject>();
    private Queue<SpaceObject> outgoing =
        new ConcurrentLinkedQueue<SpaceObject>();

    /* Status info. */
    private double score = 0;
    private Planet home = new Planet();
    private Messages messages = new Messages();
    private Research research = new Research();

    /* Weapon information. */
    private List<Weapon> weapons = new ArrayList<Weapon>();
    private Weapon weapon = new BasicWeapon();
    private long lastFire;
    private boolean firing = false;
    private double fireX, fireY;

    /* Running info. */
    private double danger = 0.05;
    private double difficulty = 0.00001;
    private boolean running = false;

    public Spacefront() {
        research.addObserver(this);
        weapons.add(weapon);
    }

    @Override
    public void run() {
        running = true;
        messages.write("Defend your home planet!", 500);
        messages.write("Press O and D to set research focus.", 600);
        while (running) {
            synchronized (this) {
                research.step();

                /* Shoot the weapon. */
                if (firing) {
                    long now = System.currentTimeMillis();
                    long diff = now - lastFire;
                    if (diff >= weapon.getSpeed()) {
                        shots.add(weapon.fire(fireX, fireY));
                        lastFire = now;
                    }
                }

                /* Spawn meteoroids. */
                danger += difficulty;
                double spawn = danger;
                while (spawn > 0) {
                    if (RNG.nextFloat() < danger) {
                        double d = SIDE / 2d * Math.sqrt(2);
                        double a = RNG.nextDouble() * Math.PI * 2;
                        double x = Math.cos(a) * d;
                        double y = Math.sin(a) * d;
                        meteoroids.add(new Meteoroid(x, y));
                    }
                    spawn -= 1d;
                }

                /* Step debris forward. */
                Set<Debris> old = new HashSet<Debris>();
                for (Debris d : debris) {
                    d.step();
                    if (d.getTTL() <= 0f) {
                        old.add(d);
                    }
                }
                debris.removeAll(old);

                /* Step meteoroids forward. */
                Set<Meteoroid> dead = new HashSet<Meteoroid>();
                for (Meteoroid m : meteoroids) {
                    if (m.step(home)) {
                        dead.add(m);
                        home.absorb(m);
                    }
                }

                /* Step weapon shots forward. */
                Set<Shot> spent = new HashSet<Shot>();
                for (Shot s : shots) {
                    Meteoroid m = s.step(this);
                    if (m != null && !dead.contains(m)) {
                        dead.add(m);
                        spent.add(s);
                        debris.addAll(m.breakup());
                        score += m.getSize();
                    } else if (s.getDistance() > SIDE) {
                        spent.add(s);
                    }
                }
                shots.removeAll(spent);
                meteoroids.removeAll(dead);

                /* Manage incoming and outgoing objects. */
                while (outgoing.peek() != null) {
                    SpaceObject o = outgoing.poll();
                    if (o instanceof Shot) {
                        shots.remove((Shot) o);
                    } else if (o instanceof Meteoroid) {
                        meteoroids.remove((Meteoroid) o);
                    } else if (o instanceof Debris) {
                        debris.remove((Debris) o);
                    }
                }
                while (incoming.peek() != null) {
                    SpaceObject o = incoming.poll();
                    if (o instanceof Shot) {
                        shots.add((Shot) o);
                    } else if (o instanceof Meteoroid) {
                        meteoroids.add((Meteoroid) o);
                    } else if (o instanceof Debris) {
                        debris.add((Debris) o);
                    }
                }

                /* Check game over status. */
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

    public void addObject(SpaceObject o) {
        incoming.add(o);
    }

    public void removeObject(SpaceObject o) {
        outgoing.add(o);
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

    public double getScore() {
        return score;
    }

    public Messages getMessageHandler() {
        return messages;
    }

    public Research getResearch() {
        return research;
    }

    @Override
    public void update(Observable o, Object arg) {
        /* Research level complete. */
        Research.Event event = (Research.Event) arg;
        if (event.getFocus() == Research.OFFENSE) {
            switch (event.getLevel()) {
            case 1:
                messages.write("New weapon: grenades");
                messages.write("Press number keys to select your weapon.");
                weapons.add(new GrenadeWeapon());
                break;
            }
        } else {
            switch (event.getLevel()) {
            }
        }
    }

    public void selectWeapon(Weapon w) {
        weapon = w;
    }

    public synchronized List<Weapon> getWeapons() {
        return weapons;
    }
}
