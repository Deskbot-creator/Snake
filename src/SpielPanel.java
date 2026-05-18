import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SpielPanel extends JPanel implements ActionListener {
    private static final int BREITE = 600;
    private static final int HOEHE = 600;
    private static final int EINHEIT = 25;
    private static final int ANZAHL_EINHEITEN = (BREITE * HOEHE) / (EINHEIT * EINHEIT);
    private static final int VERZOEGERUNG = 100;

    private final int[] x = new int[ANZAHL_EINHEITEN];
    private final int[] y = new int[ANZAHL_EINHEITEN];

    private int koerperTeile = 6;
    private int punkte = 0;
    private int apfelX;
    private int apfelY;

    private char richtung = 'R';
    private boolean laeuft = false;

    private Timer timer;
    private Random zufall;
    private String spielerName;

    public SpielPanel(String spielerName) {
        this.spielerName = spielerName;
        zufall = new Random();

        setPreferredSize(new Dimension(BREITE, HOEHE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TastenSteuerung());

        spielStarten();
    }

    public void spielStarten() {
        neuerApfel();
        laeuft = true;
        timer = new Timer(VERZOEGERUNG, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        zeichnen(g);
    }

    public void zeichnen(Graphics g) {
        if (laeuft) {
            g.setColor(Color.RED);
            g.fillOval(apfelX, apfelY, EINHEIT, EINHEIT);

            for (int i = 0; i < koerperTeile; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], EINHEIT, EINHEIT);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Punkte: " + punkte, 20, 30);
        } else {
            spielVorbei(g);
        }
    }

    public void neuerApfel() {
        apfelX = zufall.nextInt(BREITE / EINHEIT) * EINHEIT;
        apfelY = zufall.nextInt(HOEHE / EINHEIT) * EINHEIT;
    }

    public void bewegen() {
        for (int i = koerperTeile; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (richtung) {
            case 'O':
                y[0] -= EINHEIT;
                break;
            case 'U':
                y[0] += EINHEIT;
                break;
            case 'L':
                x[0] -= EINHEIT;
                break;
            case 'R':
                x[0] += EINHEIT;
                break;
        }
    }

    public void apfelPruefen() {
        if (x[0] == apfelX && y[0] == apfelY) {
            koerperTeile++;
            punkte++;
            neuerApfel();
        }
    }

    public void kollisionPruefen() {
        for (int i = koerperTeile; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                laeuft = false;
            }
        }

        if (x[0] < 0 || x[0] >= BREITE || y[0] < 0 || y[0] >= HOEHE) {
            laeuft = false;
        }

        if (!laeuft) {
            timer.stop();
            HighscoreManager.speichern(spielerName, punkte);
        }
    }

    public void spielVorbei(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Spiel vorbei", (BREITE - metrics.stringWidth("Spiel vorbei")) / 2, HOEHE / 2 - 80);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        String punkteText = "Punkte: " + punkte;
        g.drawString(punkteText, (BREITE - metrics2.stringWidth(punkteText)) / 2, HOEHE / 2 - 30);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        String info = "Drücke R für Neustart oder H für Highscores";
        g.drawString(info, (BREITE - metrics3.stringWidth(info)) / 2, HOEHE / 2 + 30);
    }

    public void neustart() {
        koerperTeile = 6;
        punkte = 0;
        richtung = 'R';

        for (int i = 0; i < x.length; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        neuerApfel();
        laeuft = true;
        timer.start();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (laeuft) {
            bewegen();
            apfelPruefen();
            kollisionPruefen();
        }
        repaint();
    }

    private class TastenSteuerung extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (richtung != 'R') richtung = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (richtung != 'L') richtung = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (richtung != 'U') richtung = 'O';
                    break;
                case KeyEvent.VK_DOWN:
                    if (richtung != 'O') richtung = 'U';
                    break;
                case KeyEvent.VK_R:
                    if (!laeuft) neustart();
                    break;
                case KeyEvent.VK_H:
                    JOptionPane.showMessageDialog(
                            SpielPanel.this,
                            HighscoreManager.laden(),
                            "Highscores",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;
            }
        }
    }
}
