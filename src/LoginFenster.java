import javax.swing.*;
import java.awt.*;

public class LoginFenster extends JFrame {
    private JTextField nameFeld;

    public LoginFenster() {
        setTitle("Snake Spiel - Login");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titel = new JLabel("Willkommen zum Snake Spiel", SwingConstants.CENTER);
        titel.setFont(new Font("Arial", Font.BOLD, 16));

        nameFeld = new JTextField();
        nameFeld.setHorizontalAlignment(SwingConstants.CENTER);

        JButton startenButton = new JButton("Spiel starten");
        startenButton.addActionListener(e -> starten());

        panel.add(titel);
        panel.add(new JLabel("Spielername:", SwingConstants.CENTER));
        panel.add(nameFeld);
        panel.add(startenButton);

        add(panel);
    }

    private void starten() {
        String name = nameFeld.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte gib einen Spielernamen ein.");
            return;
        }

        dispose();
        SpielFenster spiel = new SpielFenster(name);
        spiel.setVisible(true);
    }
}
