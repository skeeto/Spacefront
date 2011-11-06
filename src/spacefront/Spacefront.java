package spacefront;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import javax.swing.JFrame;

public class Spacefront extends Observable
    implements Runnable, MouseListener, MouseMotionListener {

    private static final Random RNG = new Random();
    private static final long DELAY = 33;
    private static final int SIDE = 600;
    private static final Dimension SIZE = new Dimension(SIDE, SIDE);
    private static final double HOME_MAX = SIDE / 2;

    private static final long FIRE_DELAY = 200;

    public static void main(String[] args) {
        new Thread(new Spacefront()).start();
    }

    private Set<Meteoroid> meteoroids = new HashSet<Meteoroid>();
    private Set<Shot> shots = new HashSet<Shot>();

    private double home = 25;

    private long lastFire;
    private boolean firing;
    private double fireX, fireY;
    private double danger = 0.05;
    private double difficulty = 0.00001;

    public Spacefront() {
        JFrame frame = new JFrame("Spacefront");
        SpacePanel panel = new SpacePanel(this);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    @Override
    public void run() {
        while (home < HOME_MAX) {
            synchronized (this) {
                if (firing) {
                    long now = System.currentTimeMillis();
                    long diff = now - lastFire;
                    if (diff >= FIRE_DELAY) {
                        double x = fireX - SIDE / 2;
                        double y = fireY - SIDE / 2;
                        shots.add(new Shot(x, y));
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
                int hits = 0;
                for (Meteoroid m : meteoroids) {
                    if (m.step(home)) {
                        hits++;
                        dead.add(m);
                    }
                }
                for (Shot s : shots) {
                    Meteoroid m = s.step(meteoroids);
                    if (m != null) {
                        dead.add(m);
                        spent.add(s);

                    } else if (s.getDistance() > SIDE) {
                        spent.add(s);
                    }
                }
                meteoroids.removeAll(dead);
                shots.removeAll(spent);
                home *= 1d + hits / 10d;
            }
            setChanged();
            notifyObservers();
            sleep(DELAY);
        }
        System.out.println("Game over");
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

    public double getHomeSize() {
        return home;
    }

    public synchronized Set<Meteoroid> getMeteoroids() {
        return new HashSet<Meteoroid>(meteoroids);
    }

    public synchronized Set<Shot> getShots() {
        return new HashSet<Shot>(shots);
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        firing = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        firing = true;
        fireX = e.getX();
        fireY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        fireX = e.getX();
        fireY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        fireX = e.getX();
        fireY = e.getY();
    }
}
