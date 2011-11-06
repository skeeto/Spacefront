package spacefront;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Controller implements MouseListener, MouseMotionListener {

    private Spacefront space;
    private SpacePanel panel;

    public Controller(Spacefront space, SpacePanel panel) {
        this.space = space;
        this.panel = panel;
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        space.fire(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        space.fire(true);
        double x = e.getX() - panel.getWidth() / 2;
        double y = e.getY() - panel.getHeight() / 2;
        space.fireXY(x, y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        space.fireXY(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        space.fire(true);
        double x = e.getX() - panel.getWidth() / 2;
        double y = e.getY() - panel.getHeight() / 2;
        space.fireXY(x, y);
    }
}
