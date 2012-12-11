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
 * A code that identifies the strategy to be used when handling issues as they 
 * occur.
 * 
 * @author lance.gatlin@gmail.com
 */
public enum IssueHandlingCode {
  /**
   * Stop on any invalid input (including recoverable errors) or exceptions
   */
  NORMAL, 
  /**
   * Ignore invalid input or replace invalid input with known good input (fix recoverable errors) - stop only on exceptions
   */
  LAX,
  /**
   * Stop on any invalid input (including recoverable errors), "suspected" invalid input (warnings) or exceptions
   */
  STRICT; 
}
