package spacefront;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class Launcher implements MouseListener {

    private static JFrame frame;
    private static SpacePanel panel;
    private static DemoController demo;

    public static void main(String[] args) {
        Spacefront spacefront = new Spacefront();
        panel = new SpacePanel(spacefront);
        frame = new JFrame("Spacefront");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        panel.addMouseListener(new Launcher());

        /* Start a demo game behind the menu. */
        demo = new DemoController(spacefront, 0.9);
        new Thread(spacefront).start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        panel.removeMouseListener(this);
        if (demo != null) {
            demo.finalize();
        }
        Spacefront game = panel.getSpacefront();
        if (game != null) {
            game.stop();
        }
        game = new Spacefront();
        panel.setSpacefront(game);
        panel.showTitle(false);
        new Controller(game, panel);
        new Thread(game).start();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
}
