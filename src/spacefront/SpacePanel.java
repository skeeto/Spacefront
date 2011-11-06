package spacefront;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
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

    public SpacePanel(Spacefront space) {
        this.space = space;
        setPreferredSize(space.getSize());
        setMinimumSize(space.getSize());
        space.addObserver(this);
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
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                           RenderingHints.VALUE_STROKE_PURE);
        g.translate(getWidth() / 2, getHeight() / 2);
        double size = space.getHomeSize();
        Shape home = new Ellipse2D.Double(-size, -size, size * 2, size * 2);
        g.setColor(Color.GREEN);
        g.fill(home);

        g.setColor(Color.RED);
        for (Shot s : space.getShots()) {
            g.fill(s.get());
        }

        /* Meteoroid-drawing. */
        g.setStroke(new BasicStroke(2));
        for (Debris d : space.getDebris()) {
            g.setColor(derive(METEOROID_OUTLINE, d.getTTL()));
            g.draw(d.get());
        }

        g.setColor(Color.WHITE);
        for (Meteoroid m : space.getMeteoroids()) {
            g.setColor(METEOROID_FILL);
            g.fill(m.get());
            g.setColor(METEOROID_OUTLINE);
            g.draw(m.get());
        }
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
