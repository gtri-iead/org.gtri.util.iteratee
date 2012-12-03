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
public final class Progress {
  private final int recoverableErrorItemCount, successItemCount, totalItemCount;

  public Progress() {
    this.recoverableErrorItemCount = 0;
    this.successItemCount = 0;
    this.totalItemCount = 0;
  }
  
  public Progress(int recoverableErrorItemCount, int successItemCount, int totalItemCount) {
    this.recoverableErrorItemCount = recoverableErrorItemCount;
    this.successItemCount = successItemCount;
    this.totalItemCount = totalItemCount;
  }
  
  public int recoverableErrorItemCount() { return recoverableErrorItemCount; }
  public int successItemCount() { return successItemCount; }
  public int totalItemCount() { return totalItemCount; }
  public double percentComplete() { return (recoverableErrorItemCount + successItemCount) / ((double)totalItemCount); }
  
  public static Progress sum(Progress lhs, Progress rhs) {
    return new Progress(
            lhs.recoverableErrorItemCount() + rhs.recoverableErrorItemCount(),
            lhs.successItemCount() + rhs.successItemCount(),
            lhs.totalItemCount() + rhs.totalItemCount()
            );
  }
  
  public static Progress sum(Progress ... progresses) {
    Progress current = new Progress();
    for(Progress status : progresses) {
      current = sum(current, status);
    }
    return current;
  }
  
  public static Progress sum(Iterable<Progress> progresses) {
    Progress current = new Progress();
    for(Progress progress : progresses) {
      current = sum(current, progress);
    }
    return current;
  }
  
  public static final Progress empty = new Progress();
}
