package spacefront;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class Launcher implements MouseListener {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        /* Prepare a demo game for behind the menu. */
        Spacefront spacefront = new Spacefront();
        DemoController demo = new DemoController(spacefront, 0.9);
        new Thread(spacefront).start();

        /* Prepare GUI. */
        SpacePanel panel = new SpacePanel(spacefront);
        JFrame frame = new JFrame("Spacefront");
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        panel.addMouseListener(new Launcher(panel, demo));
    }

    private DemoController demo;
    private SpacePanel panel;

    public Launcher(SpacePanel panel, DemoController demo) {
        this.panel = panel;
        this.demo = demo;
    }

    public void launch() {
        if (demo != null) {
            demo.finalize();
            demo = null;
        }
        Spacefront game = panel.getSpacefront();
        if (game != null) {
            game.stop();
        }
        game = new Spacefront();
        panel.setSpacefront(game);
        panel.showTitle(false);
        new Controller(game, panel);
        game.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        panel.removeMouseListener(this);
        launch();
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
