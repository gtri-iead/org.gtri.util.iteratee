/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.gtri.util.iteratee.api.Issue.ImpactCode;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public class Issues {

  public static abstract class BaseIssue implements Issue {
    private final Thread thread;
    private final long timeMillis, nanoTime;

    public BaseIssue() {
      this.timeMillis = System.currentTimeMillis();
      this.nanoTime = System.nanoTime();
      this.thread = Thread.currentThread();
    }
    
    @Override
    public long timeMillis() {
      return timeMillis;
    }

    @Override
    public long nanoTime() {
      return nanoTime;
    }

    @Override
    public Thread thread() {
      return thread;
    }
    
  }
  
  public static ConfigIssue configFatalError(String message) {
    return new ConfigIssue(message, ImpactCode.FATAL);
  }

  public static ConfigIssue configRecoverableError(String message) {
    return new ConfigIssue(message, ImpactCode.RECOVERABLE);
  }

  public static ConfigIssue configWarning(String message) {
    return new ConfigIssue(message, ImpactCode.WARNING);
  }

  public static class ConfigIssue extends BaseIssue {

    private final String message;
    private final Issue.ImpactCode impactCode;

    public ConfigIssue(String message, Issue.ImpactCode impactCode) {
      this.message = message;
      this.impactCode = impactCode;
    }

    @Override
    public String message() {
      return message;
    }

    @Override
    public Issue.ImpactCode impactCode() {
      return impactCode;
    }

    @Override
    public String toString() {
      return message();
    }
  }

  public static InternalIssue internalFatalError(Throwable cause) {
    return new InternalIssue(cause);
  }

  public static InternalIssue internalRecoverableError(Throwable cause) {
    return new InternalIssue(cause, ImpactCode.RECOVERABLE);
  }

  public static InternalIssue internalWarning(Throwable cause) {
    return new InternalIssue(cause, ImpactCode.WARNING);
  }

  public static class InternalIssue extends BaseIssue {

    private final Throwable cause;
    private final Issue.ImpactCode impactCode;

    public InternalIssue(Throwable cause, Issue.ImpactCode impactCode) {
      this.cause = cause;
      this.impactCode = impactCode;
    }

    public InternalIssue(Throwable cause) {
      this.cause = cause;
      this.impactCode = Issue.ImpactCode.FATAL;
    }

    @Override
    public String message() {
      return cause.getMessage();
    }

    @Override
    public Issue.ImpactCode impactCode() {
      return impactCode;
    }

    @Override
    public String toString() {
      return message();
    }
  }

  public static InputLocationReport reportInputLocation(ImmutableDiagnosticLocator locator) {
    return new InputLocationReport(locator);
  }
  
  public static class InputLocationReport extends BaseIssue {
    private final ImmutableDiagnosticLocator locator;

    public InputLocationReport(ImmutableDiagnosticLocator locator) {
      this.locator = locator;
    }
    
    public ImmutableDiagnosticLocator locator() {
      return locator;
    }

    @Override
    public String message() {
      return locator.toString();
    }

    @Override
    public ImpactCode impactCode() {
      return ImpactCode.NONE;
    }
  }
  
  public static InputIssue inputFatalError(String message) {
    return new InputIssue(message, ImpactCode.FATAL);
  }

  public static InputIssue inputRecoverableError(String message) {
    return new InputIssue(message, ImpactCode.RECOVERABLE);
  }

  public static InputIssue inputWarning(String message) {
    return new InputIssue(message, ImpactCode.WARNING);
  }

  public static class InputIssue extends BaseIssue {

    private final String message;
    private final Issue.ImpactCode impactCode;

    public InputIssue(String message, Issue.ImpactCode impactCode) {
      this.message = message;
      this.impactCode = impactCode;
    }

    @Override
    public String message() {
      return new StringBuilder(256).append(message).toString();
    }

    @Override
    public Issue.ImpactCode impactCode() {
      return impactCode;
    }

    @Override
    public String toString() {
      return message();
    }
  }

  public static LogIssue log(String message, Level logLevel) {
    return new LogIssue(message, logLevel);
  }
  
  public static LogIssue info(String message) {
    return new LogIssue(message, Level.INFO);
  }
  
  public static LogIssue fine(String message) {
    return new LogIssue(message, Level.FINE);
  }
  
  public static LogIssue finer(String message) {
    return new LogIssue(message, Level.FINER);
  }
  
  public static LogIssue finest(String message) {
    return new LogIssue(message, Level.FINEST);
  }
  
  public static class LogIssue extends BaseIssue {
//    private final String message;
//    private final Level logLevel;

    private final LogRecord logRecord;

    public LogIssue(String message, Level logLevel) {
      this.logRecord = new LogRecord(logLevel, message);
//      this.message = message;
//      this.logLevel = logLevel;
    }

    public Level logLevel() {
      return logRecord.getLevel();
//      return logLevel;
    }

    @Override
    public String message() {
      return logRecord.getMessage();
//      return message;
    }

    @Override
    public Issue.ImpactCode impactCode() {
      return Issue.ImpactCode.NONE;
    }

    // From http://stackoverflow.com/questions/194765/how-do-i-get-java-logging-output-to-appear-on-a-single-line
    private static final class LogFormatter extends java.util.logging.Formatter {

//      private static final String LINE_SEPARATOR = System.getProperty("line.separator");

      @Override
      public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();

        sb.append(new Date(record.getMillis()))
          .append(" ")
          .append(record.getLevel().getLocalizedName())
          .append(": ")
          .append(formatMessage(record));

//              .append(LINE_SEPARATOR);

        if (record.getThrown() != null) {
          sb.append(" ")
            .append(record.getThrown().getClass())
            .append(" ")
            .append(record.getThrown().getMessage());
//              try {
//                  StringWriter sw = new StringWriter();
//                  PrintWriter pw = new PrintWriter(sw);
//                  record.getThrown().printStackTrace(pw);
//                  pw.close();
//                  sb.append(sw.toString());
//              } catch (Exception ex) {
//                  // ignore
//              }
        }

        return sb.toString();
      }
    }

    @Override
    public String toString() {
      Formatter formatter = new LogFormatter();
      return formatter.format(logRecord);
    }
  }
}
