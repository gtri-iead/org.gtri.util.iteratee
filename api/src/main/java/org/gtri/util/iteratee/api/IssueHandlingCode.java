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
 * A code that determines whether processing should continue after an issue 
 * occurs.
 * 
 * @author lance.gatlin@gmail.com
 */
public enum IssueHandlingCode {
  /**
   * Stop on any invalid input (including recoverable issues) or fatal issues
   */
  NORMAL {

    ;

    @Override
    public boolean canContinue(Issue issue) {
      switch(issue.impactCode()) {
        case RECOVERABLE :
        case FATAL :
          return false;
        case NONE :
        case WARNING :
      }
      return true;
    }
    
  }, 
  /**
   * Ignore invalid input or replace invalid input with known good input (fix recoverable issues) - stop only on fatal issues
   */
  RECOVER {

    ;

    @Override
    public boolean canContinue(Issue issue) {
      switch(issue.impactCode()) {
        case FATAL :
          return false;
        case NONE :
        case RECOVERABLE :
        case WARNING :
      }
      return true;
    }
    
  },
  /**
   * Stop on any invalid input (including recoverable issues), "suspected" invalid input (warnings) or fatal issues
   */
  STRICT {

    ;

    @Override
    public boolean canContinue(Issue issue) {
      switch(issue.impactCode()) {
        case RECOVERABLE :
        case WARNING :
        case FATAL :
          return false;
        case NONE :
      }
      return true;
    }
    
  };
  
  abstract public boolean canContinue(Issue issue);
}
