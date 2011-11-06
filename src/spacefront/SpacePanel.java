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

    private static final Color METEOROID_FILL = Color.DARK_GRAY;
    private static final Color METEOROID_OUTLINE = Color.LIGHT_GRAY;

    private static final int STAR_COUNT = 100;
    private static final Color[] STAR_COLORS = {
        Color.WHITE, Color.GRAY, Color.LIGHT_GRAY
    };

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

        /* Draw the home planet. */
        double size;
        if (space != null) {
            size = space.getHomeSize();
        } else {
            size = 75;
        }
        Shape home = new Ellipse2D.Double(-size, -size, size * 2, size * 2);
        g.setColor(Color.GREEN);
        g.fill(home);

        if (space == null) {
            return;
        }

        /* Draw rounds fired by the player. */
        g.setColor(Color.RED);
        for (Shot s : space.getShots()) {
            g.fill(s.get());
        }

        /* Draw the debirs. */
        g.setStroke(new BasicStroke(2));
        for (Debris d : space.getDebris()) {
            g.setColor(derive(METEOROID_OUTLINE, d.getTTL()));
            g.draw(d.get());
        }

        /* Draw the meteoroids. */
        g.setColor(Color.WHITE);
        for (Meteoroid m : space.getMeteoroids()) {
            g.setColor(METEOROID_FILL);
            g.fill(m.get());
            g.setColor(METEOROID_OUTLINE);
            g.draw(m.get());
        }

        /* Draw the title screen. */
        g.setTransform(original);
        if (showTitle) {
            paintTitle(g);
        } else if (!space.isRunning()) {
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(Font.BOLD, 50f));
            paintTitle(g, "Game Over", 200);
        }
    }

    private void paintTitle(Graphics2D g) {
        Font title = g.getFont().deriveFont(Font.BOLD, 75f);
        Font subtitle = g.getFont().deriveFont(Font.ITALIC, 50f);
        g.setColor(Color.WHITE);
        g.setFont(title);
        paintTitle(g, "Spacefront", 200);
        g.setFont(subtitle);
        long time = System.currentTimeMillis();
        float alpha = (float) Math.abs(Math.sin(time / 512d));
        g.setColor(new Color(1f, 1f, 1f, alpha));
        paintTitle(g, "Click to start!", 400);
    }

    private void paintTitle(Graphics2D g, String title, int y) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(title, g);
        g.drawString(title,
                     getWidth() / 2 - (int) rect.getWidth() / 2, y);
    }

    private Color derive(Color c, float alpha) {
        return new Color(c.getRed() / 255f,
                         c.getGreen() / 255f,
                         c.getBlue() / 255f,
                         alpha);
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
