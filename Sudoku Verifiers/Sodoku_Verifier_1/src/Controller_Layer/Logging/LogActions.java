package Controller_Layer.Logging;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LogActions {
    private static final String LOG_FILE_PATH = "AppData/current/logs.txt";

    public LogActions() {
    }

    public static void LogAction(String action) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            writer.write(action + "\n");
        } catch (Exception e) {
            System.err.println("Failed to log action: " + e.getMessage());
        }

        System.out.println("Logging action: " + action); // for testing
    }

    public static String Undo() {
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
