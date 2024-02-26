package com.rubyphantasia.cubic_chunks_compatibility_helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger {
    private static Logger logger = LogManager.getLogger(ModInfo.NAME);

    public static void info(String message) {
        logger.info(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void fatal(String message) {
        logger.fatal(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }
}
