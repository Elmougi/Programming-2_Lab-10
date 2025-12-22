package Controller_Layer.Logging;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Controller_Layer.Game;

public class LogActions {
    public static final String LOG_FILE_PATH = "AppData/current/logs.txt";
    private Game game;

    public LogActions(Game game) {
        this.game = game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void doAction(String action) {
        if (action.equalsIgnoreCase("start") || action.equalsIgnoreCase("end")) {
            return; // We don't strictly log start/end in the file for this logic, only moves
        }
        if (this.game == null) return;
        logToDisk(action);
    }

    private void logToDisk(String action) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            writer.write(action + "\n");
        } catch (Exception e) {
            System.err.println("Failed to log action: " + e.getMessage());
        }
    }

    public String Undo() {
        File logFile = new File(LOG_FILE_PATH);
        if (!logFile.exists()) return null;

        List<String> lines = new ArrayList<>();
        String lastLine = null;


        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
            return null;
        }

        if (lines.isEmpty()) {
            return null; // Nothing to undo
        }


        lastLine = lines.remove(lines.size() - 1);


        try (FileWriter writer = new FileWriter(logFile, false)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error updating log file: " + e.getMessage());
        }

        return lastLine;
    }
}