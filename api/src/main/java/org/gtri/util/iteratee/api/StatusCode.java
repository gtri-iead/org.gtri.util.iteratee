/*
    Copyright 2012 Georgia Tech Research Institute

    Author: lance.gatlin@gtri.gatech.edu

    This file is part of org.gtri.util.iteratee library.

    org.gtri.util.iteratee library is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    org.gtri.util.iteratee library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.gtri.util.iteratee library. If not, see <http://www.gnu.org/licenses/>.

*/

package org.gtri.util.iteratee.api;

/**
 * A code that represents the status of some finite state machine (FSM), such as 
 * an Enumerator or an Iteratee.
 * 
 * @author lance.gatlin@gmail.com
 */
public enum StatusCode {
  /**
   * The FSM is not done
   */
  CONTINUE,
  /**
   * The FSM is done and processing of all input was successful.
   */
  SUCCESS,
  /**
   * The FSM is done and processing failed.
   */
  FAILURE
  ;

  /**
   * Test if done
   * @return TRUE if done FALSE otherwise
   */
  public boolean isDone() { 
    return this != CONTINUE;
  }

  /**
   * Tests if SUCCESS.
   * @return TRUE if SUCCESS FALSE otherwise. 
   */
  public boolean isSuccess() {
    return this == SUCCESS;
  }
  
  /**
   * Test if failure
   * @return TRUE if failure FALSE otherwise
   */
  public boolean isFailure() {
    switch(this) {
      case FAILURE :
        return true;
    }
    return false;
  }
  
  /**
   * "AND" multiple StatusCodes 
   * @param statusCodes
   * @return a new StatusCode representing the "AND" of the supplied StatusCodes
   */
  public static StatusCode and(Iterable<StatusCode> statusCodes) {
    StatusCode current = StatusCode.CONTINUE;
    for(StatusCode status : statusCodes) {
      current = and(current, status);
    }
    return current;
  }
  
  /**
   * "AND" three or more StatusCodes 
   * @param statusCodes
   * @return a new StatusCode representing the "AND" of the supplied StatusCodes
   */
  public static StatusCode and(StatusCode ... statusCodes) {
    StatusCode current = StatusCode.CONTINUE;
    for(StatusCode status : statusCodes) {
      current = and(current, status);
    }
    return current;
  }
  
  /**
   * "AND" two StatusCodes where FSM lhs AND FSM rhs are both necessary to 
   * complete some operation.
   * @param lhs
   * @param rhs
   * @return a new StatusCode representing the "AND" of the supplied StatusCodes
   */
  public static StatusCode and(StatusCode lhs, StatusCode rhs) {
    if(lhs.isDone() || rhs.isDone()) {
      if(lhs.isFailure() || rhs.isFailure()) {
        return FAILURE;
      }
      return SUCCESS;
    } else {
      return CONTINUE;
    }    
  }

  /**
   * "OR" multiple StatusCodes 
   * @param statusCodes
   * @return a new StatusCode representing the "OR" of the supplied StatusCodes
   */
  public static StatusCode or(Iterable<StatusCode> statusCodes) {
    StatusCode current = StatusCode.CONTINUE;
    for(StatusCode status : statusCodes) {
      current = or(current, status);
    }
    return current;
  }
  
  /**
   * "OR" three or more StatusCodes 
   * @param statusCodes
   * @return a new StatusCode representing the "AND" of the supplied StatusCodes
   */
  public static StatusCode or(StatusCode ... statusCodes) {
    StatusCode current = StatusCode.CONTINUE;
    for(StatusCode status : statusCodes) {
      current = or(current, status);
    }
    return current;
  }
  
  /**
   * "OR" two StatusCodes where FSM rhs OR FSM lhs could be used to complete 
   * some operation
   * @param rhs
   * @param lhs
   * @return a new StatusCode representing the "OR" of the supplied StatusCodes
   */
  public static StatusCode or(StatusCode lhs, StatusCode rhs) {
    if(lhs == SUCCESS || rhs == SUCCESS) {
      return SUCCESS;
    } else if(lhs.isFailure() && rhs.isFailure()) {
      return FAILURE;
    } else {
      return CONTINUE;
    }
  }
}  
