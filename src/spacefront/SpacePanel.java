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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final int EDGE_PAD = 10;
    private static final Color HUD_BACK = new Color(25, 95, 184, 127);
    private static final Color HUD_FRONT = new Color(123, 169, 226);
    private static final Color HUD_SELECT = new Color(116, 226, 82);
    private static final Rectangle2D RESEARCH
        = new Rectangle2D.Double(0, 0, 100, 3);
    private static final Rectangle2D WEAPON =
        new Rectangle2D.Double(0, 0, 30, 30);
    private static final int WEAPON_PAD = 5;

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
        g.setColor(HUD_FRONT);
        long score = (long) space.getScore();
        paintText(g, "Score: " + score, RIGHT, TOP,
                  getWidth() - EDGE_PAD, EDGE_PAD);

        /* Research */
        paintResearch((Graphics2D) g.create());

        /* Weapon selector. */
        paintWeapons(g);

        /* Draw the title screen. */
        if (showTitle) {
            paintTitle(g);
        } else if (!space.isRunning()) {
            paintGameOver(g);
        }

        /* Display messages. */
        if (!showTitle) {
            paintMessages(g);
        }
    }

    private void paintMessages(Graphics2D g) {
        Font font = g.getFont().deriveFont(26f);
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

    private Color getBlinkColor() {
        long time = System.currentTimeMillis();
        float alpha = (float) Math.abs(Math.sin(time / 512d));
        return new Color(1f, 1f, 1f, alpha);
    }

    private void paintTitle(Graphics2D g) {
        Font title = g.getFont().deriveFont(Font.BOLD, 75f);
        Font subtitle = g.getFont().deriveFont(Font.ITALIC, 50f);
        g.setColor(Color.WHITE);
        g.setFont(title);
        paintText(g, "Spacefront", CENTER, CENTER, getWidth() / 2, 200);
        g.setColor(getBlinkColor());
        g.setFont(subtitle);
        paintText(g, "Click to start!", CENTER, CENTER, getWidth() / 2, 400);
    }

    private void paintGameOver(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 50f));
        paintText(g, "Game Over", CENTER, CENTER, getWidth() / 2, 200);
        g.setFont(g.getFont().deriveFont(Font.ITALIC, 30f));
        g.setColor(getBlinkColor());
        paintText(g, "Press R to try again", CENTER, CENTER,
                  getWidth() / 2, 500);
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

    private void paintResearch(Graphics2D g) {
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        paintResearchBar(g, Research.OFFENSE, "Olvl ", 0);
        paintResearchBar(g, Research.DEFENSE, "Dlvl ", 1);
    }

    private void paintResearchBar(Graphics2D g, int focus, String pre,
                                  int pos) {
        Research r = space.getResearch();
        int level = r.getLevel(focus) + 1;
        double prog = r.getProgress(focus);

        /* String metrics */
        String str = pre + level;
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(str, g);

        g.setColor(HUD_FRONT);
        paintText(g, str, LEFT, TOP,
                  EDGE_PAD, (int) (EDGE_PAD + rect.getHeight() * pos));
        AffineTransform at = new AffineTransform();
        at.translate(65, EDGE_PAD + (rect.getHeight() / 2) -
                     (RESEARCH.getHeight() / 2) + rect.getHeight() * pos);
        g.setColor(HUD_BACK);
        g.fill(at.createTransformedShape(RESEARCH));
        at.scale(prog, 1d);
        g.setColor(HUD_FRONT);
        g.fill(at.createTransformedShape(RESEARCH));
    }

    private Map<Weapon, Shot> weaponMap = new HashMap<Weapon, Shot>();

    private void paintWeapons(Graphics2D g) {
        List<Weapon> weapons = space.getWeapons();
        AffineTransform at = new AffineTransform();
        int width = (int) (WEAPON_PAD + WEAPON.getWidth());
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            at.setToTranslation(EDGE_PAD + width * i,
                                getHeight() - EDGE_PAD - WEAPON.getHeight());
            paintWeapon((Graphics2D) g.create(), w,
                        at.createTransformedShape(WEAPON),
                        i + 1, space.getWeapon() == w);
        }
    }

    private void paintWeapon(Graphics2D g, Weapon w, Shape pos,
                             int i, boolean selected) {
        g.setColor(Color.BLACK);
        g.fill(pos);
        if (!selected) {
            g.setColor(HUD_FRONT);
        } else {
            g.setColor(HUD_SELECT);
        }
        g.draw(pos);
        Rectangle2D rect = pos.getBounds2D();

        /* Paint the numbers. */
        g.setColor(Color.WHITE);
        paintText(g, "" + i, LEFT, TOP,
                  (int) rect.getX() + 2,
                  (int) rect.getY() + 2);

        /* Paint the shot. */
        Shot shot = weaponMap.get(w);
        if (shot == null) {
            shot = w.fire(1, 1);
            weaponMap.put(w, shot);
        }
        g.translate(rect.getCenterX() - shot.getX(),
                    rect.getCenterY() - shot.getY());
        shot.paint(g);
        shot.step(null);
    }
}
