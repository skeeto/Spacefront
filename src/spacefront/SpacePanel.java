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
import javax.swing.JComponent;

public class SpacePanel extends JComponent implements Observer {

    private static final long serialVersionUID = 1L;

    Spacefront space;

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
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(getWidth() / 2, getHeight() / 2);
        double size = space.getHomeSize();
        Shape home = new Ellipse2D.Double(-size, -size, size * 2, size * 2);
        g.setColor(Color.GREEN);
        g.fill(home);

        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        for (Meteoroid m : space.getMeteoroids()) {
            double x = m.getX();
            double y = m.getY();
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            g.draw(at.createTransformedShape(m));
        }
    }
}
