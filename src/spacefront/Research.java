package spacefront;

import java.util.Observable;

public class Research extends Observable {

    public static final int OFFENSE = 0;
    public static final int DEFENSE = 1;

    public static final int MAX = 9;
    private static final double RATE = 0.001;
    private static final double CURVE = 1.5;

    private double[] research = new double[2];
    private double defense;
    private int focus = OFFENSE;

    public void setFocus(int target) {
        focus = check(target);
    }

    public void step() {
        int orig = getLevel(focus);
        research[focus] += RATE;
        if (Math.pow(research[focus], 1d / CURVE) > MAX) {
            research[focus] = Math.pow(MAX, CURVE);
        }
        if (getLevel(focus) > orig) {
            setChanged();
            notifyObservers(new Event(focus, orig + 1));
        }
    }

    private double getRawLevel(int target) {
        return Math.pow(research[check(target)], 1d / CURVE);
    }

    public int getLevel(int target) {
        return (int) getRawLevel(target);
    }

    public double getProgress(int target) {
        int base = getLevel(target);
        double low = Math.pow(base, CURVE);
        double current = research[target];
        double high = Math.pow(base + 1, CURVE);
        return (current - low) / (high - low);
    }

    private int check(int target) {
        if (target < 0 || target > 1) {
            throw new IllegalArgumentException("Bad focus: " + target);
        }
        return target;
    }

    public static class Event {

        private final int focus;
        private final int level;

        public Event(int focus, int level) {
            this.focus = focus;
            this.level = level;
        }

        public int getFocus() {
            return focus;
        }

        public int getLevel() {
            return level;
        }

        @Override
        public String toString() {
            String name;
            if (focus == OFFENSE) {
                name = "OFFENCE";
            } else {
                name = "DEFENSE";
            }
            return "[" + name + ": " + level + "]";
        }
    }
}
