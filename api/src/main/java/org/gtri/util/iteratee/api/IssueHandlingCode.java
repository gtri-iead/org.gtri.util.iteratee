/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

/**
 *
 * @author Lance
 */
public enum ErrorHandlingCode {
  NORMAL, // stop on any error
  LAX, // recover from recoverable errors when possible - stop only on fatal errors
  STRICT; // stop on any warning or error
}
