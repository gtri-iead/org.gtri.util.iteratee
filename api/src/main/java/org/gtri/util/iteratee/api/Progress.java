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
 * A class that represents the progress of an Enumerator.
 * 
 * @author lance.gatlin@gmail.com
 */
public final class Progress {
  private final int recoverableErrorItemCount, successItemCount, totalItemCount;

  /**
   * Construct an empty Progress
   */
  public Progress() {
    this.recoverableErrorItemCount = 0;
    this.successItemCount = 0;
    this.totalItemCount = 0;
  }
  
  /**
   * Contract a Progress
   * @param recoverableErrorItemCount
   * @param successItemCount
   * @param totalItemCount 
   */
  public Progress(int recoverableErrorItemCount, int successItemCount, int totalItemCount) {
    this.recoverableErrorItemCount = recoverableErrorItemCount;
    this.successItemCount = successItemCount;
    this.totalItemCount = totalItemCount;
  }
  
  /**
   * Get the count of processed items that resulted in a recoverable error
   * @return the count of processed items that resulted in a recoverable error
   */
  public int recoverableErrorItemCount() { return recoverableErrorItemCount; }
  /**
   * Get the count of processed items that were successfully processed
   * @return the count of processed items that were successfully processed
   */
  public int successItemCount() { return successItemCount; }
  /**
   * Get the total count of items to be processed
   * @return the total count of items to be processed
   */
  public int totalItemCount() { return totalItemCount; }
  /**
   * Get the percentage complete
   * @return the percentage complete
   */
  public double percentComplete() { return (recoverableErrorItemCount + successItemCount) / ((double)totalItemCount); }
  
  /**
   * Sum two Progress objects
   * @param lhs
   * @param rhs
   * @return a new Progress object that is the sum of the supplied Progress objects
   */
  public static Progress sum(Progress lhs, Progress rhs) {
    return new Progress(
            lhs.recoverableErrorItemCount() + rhs.recoverableErrorItemCount(),
            lhs.successItemCount() + rhs.successItemCount(),
            lhs.totalItemCount() + rhs.totalItemCount()
            );
  }
  
  /**
   * Sum three or more Progress objects
   * @param progresses
   * @return a new Progress object that is the sum of the supplied Progress objects
   */
  public static Progress sum(Progress ... progresses) {
    Progress current = new Progress();
    for(Progress status : progresses) {
      current = sum(current, status);
    }
    return current;
  }
  
  /**
   * Sum multiple Progress objects
   * @param progresses
   * @return a new Progress object that is the sum of the supplied Progress objects
   */
  public static Progress sum(Iterable<Progress> progresses) {
    Progress current = new Progress();
    for(Progress progress : progresses) {
      current = sum(current, progress);
    }
    return current;
  }
  
  /**
   * An empty progress
   */
  public static final Progress empty = new Progress();
}
