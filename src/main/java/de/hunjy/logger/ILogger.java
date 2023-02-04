package de.hunjy.logger;

import de.hunjy.HyperStand;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ILogger {

    private static Logger logger;
    private static File logFile;

    static {
        logger = Logger.getLogger(HyperStand.getInstance().getDescription().getPrefix());
        logger.setLevel(Level.ALL);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        logFile = new File(HyperStand.getInstance().getDataFolder().getPath() + "/logs", dtf.format(now) + ".log");

        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void log(String msg) {
        logger.info(msg);
    }

    public static void warning(String msg) {
        logger.warning(msg);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void writeToLogFile(String line) {
        FileWriter fileWriter = null;
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            fileWriter = new FileWriter(logFile);
            PrintWriter printWriter = new PrintWriter(dtf.format(now) + ": " + fileWriter);
            printWriter.printf(line);
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
