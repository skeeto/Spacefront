package spacefront;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Messages {

    private static final double DISPLAY_TIME = 2200;

    private Queue<String> queue = new ConcurrentLinkedQueue<String>();
    private String current;
    private long time;

    public void write(String message) {
        queue.add(message);
    }

    public String getMessage() {
        long now = System.currentTimeMillis();
        if (now - time > DISPLAY_TIME) {
            current = null;
        }
        if (current == null) {
            current = queue.poll();
            time = now;
        }
        return current;
    }

    public float getAge() {
        double v = ((System.currentTimeMillis() - time) / DISPLAY_TIME);
        return (float) Math.pow(v, 4);
    }
}
