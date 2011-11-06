package spacefront;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import javax.swing.JFrame;

public class Spacefront extends Observable implements Runnable {

    private static final Random RNG = new Random();
    private static final long DELAY = 33;
    private static final Dimension SIZE = new Dimension(600, 600);
    private static final double HOME_MAX = 300;

    private Set<Meteoroid> meteoroids = new HashSet<Meteoroid>();

    public static void main(String[] args) {
        new Thread(new Spacefront()).start();
    }

    private double home = 25;

    public Spacefront() {
        JFrame frame = new JFrame("Spacefront");
        SpacePanel panel = new SpacePanel(this);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void run() {
        while (home < HOME_MAX) {
            synchronized (this) {
                if (RNG.nextFloat() < 0.2) {
                    double d = SIZE.getWidth() * 0.75;
                    double a = RNG.nextDouble() * Math.PI * 2;
                    double x = Math.cos(a) * d;
                    double y = Math.sin(a) * d;
                    meteoroids.add(new Meteoroid(x, y));
                }
                Set<Meteoroid> dead = new HashSet<Meteoroid>();
                int hits = 0;
                for (Meteoroid m : meteoroids) {
                    if (m.step(home)) {
                        System.out.println("HIT!!!");
                        hits++;
                        dead.add(m);
                    }
                }
                meteoroids.removeAll(dead);
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
}
