import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HighscoreManager {
    private static final String DATEI_NAME = "highscores.txt";

    public static void speichern(String spielerName, int punkte) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATEI_NAME, true))) {
            String datum = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            writer.write(spielerName + " - " + punkte + " Punkte - " + datum);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Highscores: " + e.getMessage());
        }
    }

    public static String laden() {
        File datei = new File(DATEI_NAME);

        if (!datei.exists()) {
            return "Noch keine Highscores vorhanden.";
        }

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(datei))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                builder.append(zeile).append("\n");
            }
        } catch (IOException e) {
            return "Fehler beim Laden der Highscores.";
        }

        return builder.toString();
    }
}
