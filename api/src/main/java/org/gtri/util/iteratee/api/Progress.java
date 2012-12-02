/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
}
