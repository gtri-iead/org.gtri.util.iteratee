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
 *
 * @author lance.gatlin@gmail.com
 */
public enum StatusCode {
  CONTINUE,
  SUCCESS,
  RECOVERABLE_ERROR,
  FATAL_ERROR;

  public boolean isDone() { 
    switch(this) {
      case RECOVERABLE_ERROR:
      case CONTINUE :
        return false;
      case SUCCESS :
      case FATAL_ERROR :
    }
    return true;
  }
  public boolean isSuccess() {
    switch(this) {
      case RECOVERABLE_ERROR:
      case CONTINUE :
      case FATAL_ERROR :
        return false;
      case SUCCESS :
    }
    return true;
  }
  public boolean isError() {
    switch(this) {
      case CONTINUE :
      case SUCCESS :
        return false;
      case RECOVERABLE_ERROR:
      case FATAL_ERROR :
    }
    return true;    
  }
  
  public static StatusCode and(Iterable<StatusCode> statusCodes) {
    StatusCode current = StatusCode.CONTINUE;
    for(StatusCode status : statusCodes) {
      current = and(current, status);
    }
    return current;
  }
  
  public static StatusCode and(StatusCode ... statusCodes) {
    StatusCode current = StatusCode.CONTINUE;
    for(StatusCode status : statusCodes) {
      current = and(current, status);
    }
    return current;
  }
  
  public static StatusCode and(StatusCode lhs, StatusCode rhs) {
    switch(lhs) {
      case CONTINUE :
        return rhs;
      case SUCCESS :
        switch(rhs) {
          case CONTINUE :
            return SUCCESS;
          case SUCCESS :
            return SUCCESS;
          case RECOVERABLE_ERROR:
            return SUCCESS;
          case FATAL_ERROR :         
            return FATAL_ERROR;
        }
      case RECOVERABLE_ERROR:
        switch(rhs) {
          case CONTINUE :
            return RECOVERABLE_ERROR;
          case SUCCESS :
            return SUCCESS;
          case RECOVERABLE_ERROR:
            return RECOVERABLE_ERROR;
          case FATAL_ERROR :         
            return FATAL_ERROR;
        }
      case FATAL_ERROR :
    }
    return FATAL_ERROR;
  }

  public static StatusCode or(Iterable<StatusCode> statusCodes) {
    StatusCode current = StatusCode.CONTINUE;
    for(StatusCode status : statusCodes) {
      current = or(current, status);
    }
    return current;
  }
  
  public static StatusCode or(StatusCode ... statusCodes) {
    StatusCode current = StatusCode.CONTINUE;
    for(StatusCode status : statusCodes) {
      current = or(current, status);
    }
    return current;
  }
  
  public static StatusCode or(StatusCode lhs, StatusCode rhs) {
    switch(lhs) {
      case CONTINUE :
        switch(rhs) {
          case CONTINUE :
            return CONTINUE;
          case SUCCESS :
            return CONTINUE;
          case RECOVERABLE_ERROR:
            return RECOVERABLE_ERROR;
          case FATAL_ERROR :
            return FATAL_ERROR;
        }
      case SUCCESS :
        switch(rhs) {
          case CONTINUE :
            return CONTINUE;
          case SUCCESS :
            return SUCCESS;
          case RECOVERABLE_ERROR:
            return RECOVERABLE_ERROR;
          case FATAL_ERROR :
            return FATAL_ERROR;
        }
      case RECOVERABLE_ERROR:
        switch(rhs) {
          case CONTINUE :
            return RECOVERABLE_ERROR;
          case SUCCESS :
            return RECOVERABLE_ERROR;
          case RECOVERABLE_ERROR:
            return RECOVERABLE_ERROR;
          case FATAL_ERROR :
            return FATAL_ERROR;
        }
      case FATAL_ERROR :
    }
    return FATAL_ERROR;
  }
}  
