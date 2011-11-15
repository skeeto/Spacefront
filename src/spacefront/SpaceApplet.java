package spacefront;

import javax.swing.JApplet;

public class SpaceApplet extends JApplet {

    private static final long serialVersionUID = 1L;

    private SpacePanel panel;
    private DemoController demo;

    @Override
    public void init() {
        /* Prepare a demo game for behind the menu. */
        Spacefront spacefront = new Spacefront();
        demo = new DemoController(spacefront, 0.9);
        new Thread(spacefront).start();

        /* Prepare the GUI. */
        panel = new SpacePanel(spacefront);
        add(panel);
        panel.addMouseListener(new Launcher(panel, demo));
    }

    @Override
    public final void start() {
    }

    @Override
    public final void stop() {
    }

    @Override
    public final void destroy() {
        demo.finalize();
    }
}
