import javax.swing.*;

public class SpielFenster extends JFrame {
    public SpielFenster(String spielerName) {
        setTitle("Snake Spiel - Spieler: " + spielerName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        SpielPanel panel = new SpielPanel(spielerName);
        add(panel);
        pack();

        setLocationRelativeTo(null);
    }
}
