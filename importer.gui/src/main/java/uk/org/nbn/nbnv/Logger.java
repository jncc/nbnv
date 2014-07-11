package uk.org.nbn.nbnv;

/**
 * @author stephen batty
 *         Date: 11/07/14
 *         Time: 11:09
 */
import java.util.logging.Level;

public class Logger {
    public static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("importer.gui");

    /**
     * Logs the given content at debug level.
     *
     * @param content
     *            String content to log
     * @see #willDebug()
     */
    public static void debug(String content) {
        if (willDebug()) {
            logger.fine(content);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at debug level.
     *
     * @param format
     *            String format, see {@link String#format(String, Object...)}
     * @param args
     *            the arguments to format
     * @see String#format(String, Object...)
     * @see #willDebug()
     */
    public static void debug(String format, Object... args) {
        if (willDebug()) {
            logger.fine(String.format(format, args));
        }
    }

    /**
     * Logs the given content at info level
     *
     * @param content
     *            String content to log
     * @see #willInfo()
     */
    public static void info(String content) {
        if (willInfo()) {
            logger.info(content);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at info level
     *
     * @param format
     *            String format, see {@link String#format(String, Object...)}
     * @param args
     *            the arguments to format
     * @see String#format(String, Object...)
     * @see #willInfo()
     */
    public static void info(String format, Object... args) {
        if (willInfo()) {
            logger.info(String.format(format, args));
        }
    }

    /**
     * Logs the content at warn level
     *
     * @param content
     *            String content to log
     * @see #willWarn()
     */
    public static void warn(String content) {
        if (willWarn()) {
            logger.warning(content);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at warn level
     *
     * @param format
     *            String format, see {@link String#format(String, Object...)}
     * @param args
     *            the arguments to format
     * @see String#format(String, Object...)
     * @see #willWarn()
     */
    public static void warn(String format, Object... args) {
        if (willWarn()) {
            logger.warning(String.format(format, args));
        }
    }

    /**
     * Logs the content at error level
     *
     * @param content
     *            String content to log
     * @see #willError()
     */
    public static void error(String content) {
        if (willError()) {
            logger.severe(content);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at error level
     *
     * @param format
     *            String format, see {@link String#format(String, Object...)}
     * @param args
     *            the arguments to format
     * @see String#format(String, Object...)
     * @see #willError()
     */
    public static void error(String format, Object... args) {
        if (willError()) {
            logger.severe(String.format(format, args));
        }
    }

    /**
     * Returns true if the logger will log at debug level or above, false if not
     */
    public static boolean willDebug() {
        return logger.isLoggable(Level.FINE);
    }

    /**
     * Returns true if the logger will log at info level or above, false if not
     */
    public static boolean willInfo() {
        return logger.isLoggable(Level.INFO);
    }

    /**
     * Returns true if the logger will log at warn level or above, false if not
     */
    public static boolean willWarn() {
        return logger.isLoggable(Level.WARNING);
    }

    /**
     * Returns true if the logger will log at error level or above, false if not
     */
    public static boolean willError() {
        return logger.isLoggable(Level.SEVERE);
    }
}

