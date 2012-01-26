/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.krohm.birtstuff;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arnaud
 */
public class Slf4jHandler extends Handler {

    public void close() throws SecurityException {
    }

    public void flush() {
    }

    public void publish(LogRecord record) {

        Logger logger = LoggerFactory.getLogger(record.getLoggerName());
        Level level = record.getLevel();
        String message = record.getMessage();
        if (Level.SEVERE.equals(level)) {
            logger.error(message);
        } else if (Level.INFO.equals(level)) {
            logger.info(message);
        } else if (Level.WARNING.equals(level)) {
            logger.warn(message);
        }
    }
}
