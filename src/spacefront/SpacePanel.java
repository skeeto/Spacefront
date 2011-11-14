package spacefront;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.swing.JComponent;

public class SpacePanel extends JComponent implements Observer {

    private static final long serialVersionUID = 1L;

    private static final int STAR_COUNT = 100;
    private static final Color[] STAR_COLORS = {
        Color.WHITE, Color.GRAY, Color.LIGHT_GRAY
    };

    private static final int EDGE_PAD = 20;

    private Spacefront space;
    private int starseed = (int) (Math.random() * 100);
    private boolean showTitle = true;

    public SpacePanel(Spacefront space) {
        setSpacefront(space);
    }

    public void setSpacefront(Spacefront space) {
        this.space = space;
        if (space != null) {
            setPreferredSize(space.getSize());
            //setMinimumSize(space.getSize());
            space.addObserver(this);
        }
    }

    public Spacefront getSpacefront() {
        return space;
    }

    public void showTitle(boolean enabled) {
        showTitle = enabled;
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        paintStars(graphics);

        /* Prepare Graphics2D. */
        Graphics2D g = (Graphics2D) graphics;
        AffineTransform original = g.getTransform();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                           RenderingHints.VALUE_STROKE_PURE);
        g.translate(getWidth() / 2, getHeight() / 2);

        if (space == null) {
            return;
        }

        /* Draw the home planet. */
        space.getHome().paint((Graphics2D) g.create());

        g.setStroke(new BasicStroke(2));

        /* Draw rounds fired by the player. */
        for (Shot s : space.getShots()) {
            s.paint(g);
        }

        /* Draw the debris. */
        for (Debris d : space.getDebris()) {
            d.paint(g);
        }

        /* Draw the meteoroids. */
        for (Meteoroid m : space.getMeteoroids()) {
            m.paint(g);
        }

        /* Score */
        g.setTransform(original);
        g.setColor(Color.WHITE);
        if (true && !showTitle) {
            long score = (long) space.getScore();
            paintText(g, "Score: " + score, RIGHT, TOP,
                      getWidth() - EDGE_PAD, EDGE_PAD);
        }

        /* Draw the title screen. */
        if (showTitle) {
            paintTitle(g);
        } else if (!space.isRunning()) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(Font.BOLD, 50f));
            paintText(g, "Game Over", CENTER, CENTER, getWidth() / 2, 200);
        }

        /* Display messages. */
        if (!showTitle) {
            paintMessages(g);
        }
    }

    private void paintMessages(Graphics2D g) {
        Font font = g.getFont().deriveFont(30f);
        g.setFont(font);
        Messages messages = space.getMessageHandler();
        String msg = messages.getMessage();
        float age = messages.getAge();
        if (msg != null && age >= 0) {
            g.setColor(new Color(1f, 1f, 1f, 1f - age));
            paintText(g, msg, CENTER, TOP,
                      getWidth() / 2, getHeight() / 2 + 100);
        }
    }

    private void paintTitle(Graphics2D g) {
        Font title = g.getFont().deriveFont(Font.BOLD, 75f);
        Font subtitle = g.getFont().deriveFont(Font.ITALIC, 50f);
        g.setColor(Color.WHITE);
        g.setFont(title);
        paintText(g, "Spacefront", CENTER, CENTER, getWidth() / 2, 200);
        g.setFont(subtitle);
        long time = System.currentTimeMillis();
        float alpha = (float) Math.abs(Math.sin(time / 512d));
        g.setColor(new Color(1f, 1f, 1f, alpha));
        paintText(g, "Click to start!", CENTER, CENTER, getWidth() / 2, 400);
    }

    private static final int LEFT = 0;
    private static final int CENTER = 1;
    private static final int RIGHT = 2;
    private static final int TOP = 3;
    private static final int BOTTOM = 4;

    private void paintText(Graphics2D g, String text, int halign, int valign,
                           int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(text, g);
        int actualx;
        if (halign == CENTER) {
            actualx = x - (int) rect.getWidth() / 2;
        } else if (halign == LEFT) {
            actualx = x;
        } else if (halign == RIGHT) {
            actualx = x - (int) rect.getWidth();
        } else {
            throw new IllegalArgumentException("Bad align value: " + halign);
        }
        int actualy;
        if (valign == CENTER) {
            actualy = y - (int) rect.getHeight() / 2;
        } else if (valign == BOTTOM) {
            actualy = y;
        } else if (valign == TOP) {
            actualy = y + (int) rect.getHeight();
        } else {
            throw new IllegalArgumentException("Bad align value: " + valign);
        }
        actualy -= fm.getDescent();
        g.drawString(text, actualx, actualy);
    }

    private void paintStars(Graphics g) {
        Random stars = new Random(starseed);
        for (int i = 0; i < STAR_COUNT; i++) {
            int x = stars.nextInt(getWidth());
            int y = stars.nextInt(getHeight());
            g.setColor(STAR_COLORS[stars.nextInt(STAR_COLORS.length)]);
            g.drawLine(x, y, x, y);
        }
    }
}
