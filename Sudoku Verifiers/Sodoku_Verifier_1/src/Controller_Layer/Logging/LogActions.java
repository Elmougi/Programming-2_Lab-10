package Controller_Layer.Logging;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import Controller_Layer.Game;

public class LogActions {
    public static final String LOG_FILE_PATH = "AppData/current/logs.txt";
    private Game game;

    public LogActions(Game game) {
        this.game = game;
    }

    public void doAction(String action) {
        if (action.equalsIgnoreCase("end")) {
            game.endGame();
            game.endGame();
            return;
        }
        if (action.equalsIgnoreCase("undo")) {
            Undo();
            return;
        }

        String[] parts = action.split(", ");
        char x = parts[0].charAt(0);
        char y = parts[1].charAt(0);
        char val = parts[2].charAt(0);

        game.addInput(x - '0', y - '0', val - '0');

        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            writer.write(action + "\n");
        } catch (Exception e) {
            System.err.println("Failed to log action: " + e.getMessage());
        }

        System.out.println("Logging action: " + action); // for testing
    }

    public String Undo() {
        String LastAction = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (reader.ready()) { // (in other words, if there is more lines to read)
                    content.append(line).append("\n");
                } else {
                    LastAction = line;
                }
            }

            if (LastAction == null) {
                System.out.println("No actions to undo.");
                return null;
            }

            String[] parts = LastAction.split(", ");
            char x = parts[0].charAt(0);
            char y = parts[1].charAt(0);
            char prev = parts[3].charAt(0);

            game.addInput(x - '0', y - '0', prev - '0');

            try (FileWriter writer = new FileWriter(LOG_FILE_PATH, false)) {
                writer.write(content.toString());
            } catch (Exception e) {
                System.err.println("Failed to update log file after undo: " + e.getMessage());
            }

            System.out.println("logged undo: " + LastAction); // for testing
            return LastAction;

        } catch (IOException e) {
            System.err.println("Failed to read log file: " + e.getMessage());
            return null;
        }
    }
}
