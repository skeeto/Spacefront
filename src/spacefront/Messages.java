package spacefront;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Messages {

    private static final double DISPLAY_TIME = 2200;

    private static final ScheduledExecutorService EXEC =
        Executors.newSingleThreadScheduledExecutor();

    private Queue<String> queue = new ConcurrentLinkedQueue<String>();
    private String current;
    private long time;

    public void write(String message) {
        queue.add(message);
    }

    public void write(final String message, long delay) {
        EXEC.schedule(new Runnable() {
                public void run() {
                    write(message);
                }
            }, delay, TimeUnit.MILLISECONDS);
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
