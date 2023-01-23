package de.hunjy.logger;

import de.hunjy.HyperStand;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ILogger {

    private static Logger logger;

    static {
        logger = Logger.getLogger(HyperStand.getInstance().getDescription().getPrefix());
        logger.setLevel(Level.ALL);
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
}
