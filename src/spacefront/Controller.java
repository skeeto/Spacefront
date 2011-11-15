package spacefront;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class Controller
    implements KeyListener, MouseListener, MouseMotionListener {

    private Spacefront space;
    private SpacePanel panel;

    public Controller(Spacefront space, SpacePanel panel) {
        this.space = space;
        this.panel = panel;
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.addKeyListener(this);
        panel.requestFocusInWindow();
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

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
        case 'o':
            space.getResearch().setFocus(Research.OFFENSE);
            break;
        case 'd':
            space.getResearch().setFocus(Research.DEFENSE);
            break;
        case 'r':
            if (!space.isRunning()) {
                new Launcher(panel, null).launch();
            }
            break;
        default:
            /* Try a number key. */
            try {
                String str = new String(new char[] {e.getKeyChar()});
                int num = Integer.parseInt(str);
                List<Weapon> ws = space.getWeapons();
                Weapon w = ws.get(num - 1);
                space.selectWeapon(w);
            } catch (NumberFormatException exception) {
                break;
            } catch (IndexOutOfBoundsException exception) {
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}
}
