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
 * An interface for an issue that occurred during a computation.
 * 
 * @author lance.gatlin@gmail.com
 */
public interface Issue {
  /**
   * A code to indicate the impact of the issue. 
   */
  enum ImpactCode {
    /**
     * An issue occurred that must result in the immediate termination of all 
     * processing
     */
    FATAL,
    /**
     * An issue occurred that can be fixed by ignoring or substituting 
     * incorrect values with correct values though the output will differ from
     * expectations
     */
    RECOVERABLE,
    /**
     * An issue occurred which may result in output that differs from 
     * expectations
     */
    WARNING,
    /**
     * An issue occurred which has no impact on processing (e.g. a log message)
     */
    NONE,
  }
  /**
   * Get the message
   * @return the message
   */
  String message();
  
  /**
   * Get the impact of the issue
   * @return the impact code of the issue
   */
  ImpactCode impactCode();
 
}
