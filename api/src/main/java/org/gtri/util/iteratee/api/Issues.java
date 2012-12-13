/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

import java.util.logging.Level;
import org.gtri.util.iteratee.api.Issue.ImpactCode;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public class Issues {
  public static ConfigIssue cfgFatal(String message) {
    return new ConfigIssue(message, ImpactCode.FATAL);
  }
  public static ConfigIssue cfgRecoverable(String message) {
    return new ConfigIssue(message, ImpactCode.RECOVERABLE);
  }
  public static ConfigIssue cfgWarning(String message) {
    return new ConfigIssue(message, ImpactCode.WARNING);
  }
  public static class ConfigIssue implements Issue {
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
    
  }
  
  public static InternalIssue fatal(Throwable cause) {
    return new InternalIssue(cause);
  }
  public static InternalIssue recoverable(Throwable cause) {
    return new InternalIssue(cause, ImpactCode.RECOVERABLE);
  }
  public static InternalIssue warning(Throwable cause) {
    return new InternalIssue(cause, ImpactCode.WARNING);
  }
  public static class InternalIssue implements Issue {
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
  }
  
  public static InputIssue inputFatal(String message, ImmutableDiagnosticLocator locator) {
    return new InputIssue(message, locator, ImpactCode.FATAL);
  }
  public static InputIssue inputRecoverable(String message, ImmutableDiagnosticLocator locator) {
    return new InputIssue(message, locator, ImpactCode.RECOVERABLE);
  }
  public static InputIssue inputWarning(String message, ImmutableDiagnosticLocator locator) {
    return new InputIssue(message, locator, ImpactCode.WARNING);
  }
  public static class InputIssue implements Issue {
    private final String message;
    private final ImmutableDiagnosticLocator locator;
    private final Issue.ImpactCode impactCode;

    public InputIssue(String message, ImmutableDiagnosticLocator locator, Issue.ImpactCode impactCode) {
      this.message = message;
      this.locator = locator;
      this.impactCode = impactCode;
    }

    public ImmutableDiagnosticLocator locator() {
      return locator;
    }

    @Override
    public String message() {
      return new StringBuilder(256).append(locator).append(message).toString();
    }

    @Override
    public Issue.ImpactCode impactCode() {
      return impactCode;
    }
  }
  
  public static LogIssue log(String message, Level logLevel) {
    return new LogIssue(message, logLevel);
  }
  public static class LogIssue implements Issue {
    private final String message;
    private final Level logLevel;

    public LogIssue(String message, Level logLevel) {
      this.message = message;
      this.logLevel = logLevel;
    }

    public Level logLevel() {
      return logLevel;
    }
    
    @Override
    public String message() {
      return message;
    }

    @Override
    public Issue.ImpactCode impactCode() {
      return Issue.ImpactCode.NONE;
    }
    
  }
  
}
