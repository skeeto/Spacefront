package spacefront;

public class Research {

    public static final int OFFENSE = 0;
    public static final int DEFENSE = 1;

    private static final double RATE = 0.001;

    private double[] research = new double[2];
    private double defense;
    private int focus = OFFENSE;

    public void setFocus(int target) {
        focus = check(target);
    }

    public void step() {
        research[focus] += RATE * 4;
    }

    public int getLevel(int target) {
        return (int) research[check(target)];
    }

    public double getProgress(int target) {
        return research[check(target)] - getLevel(target);
    }

    private int check(int target) {
        if (target < 0 || target > 1) {
            throw new IllegalArgumentException("Bad focus: " + target);
        }
        return target;
    }
}
