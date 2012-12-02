/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gtri.util.iteratee.api;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public final class Status {
  private final StatusCode statusCode;
  private final int recoverableErrorItemCount, successItemCount, totalItemCount;

  public Status(StatusCode statusCode) {
    this.statusCode = statusCode;
    this.recoverableErrorItemCount = 0;
    this.successItemCount = 0;
    this.totalItemCount = 0;
  }
  
  public Status(StatusCode statusCode, int recoverableErrorItemCount, int successItemCount, int totalItemCount) {
    this.statusCode = statusCode;
    this.recoverableErrorItemCount = recoverableErrorItemCount;
    this.successItemCount = successItemCount;
    this.totalItemCount = totalItemCount;
  }
  
  public StatusCode statusCode() { return statusCode; }
  public int recoverableErrorItemCount() { return recoverableErrorItemCount; }
  public int successItemCount() { return successItemCount; }
  public int totalItemCount() { return totalItemCount; }
  
  public boolean isDone() { return statusCode.isDone(); }
  public boolean isSuccess() { return statusCode.isSuccess(); }
  public boolean isError() { return statusCode.isError(); }
  
  public static Status sumAnd(Status lhs, Status rhs) {
    return new Status(
            StatusCode.and(lhs.statusCode(), rhs.statusCode()),
            lhs.recoverableErrorItemCount() + rhs.recoverableErrorItemCount(),
            lhs.successItemCount() + rhs.successItemCount(),
            lhs.totalItemCount() + rhs.totalItemCount()
            );
  }
  
  public static Status sumAnd(Status ... statuses) {
    Status current = new Status(StatusCode.CONTINUE);
    for(Status status : statuses) {
      current = sumAnd(current, status);
    }
    return current;
  }
  
  public static Status sumOr(Status lhs, Status rhs) {
    return new Status(
            StatusCode.or(lhs.statusCode(), rhs.statusCode()),
            lhs.recoverableErrorItemCount() + rhs.recoverableErrorItemCount(),
            lhs.successItemCount() + rhs.successItemCount(),
            lhs.totalItemCount() + rhs.totalItemCount()
            );
  }
  
  public static Status sumOr(Status ... statuses) {
    Status current = new Status(StatusCode.CONTINUE);
    for(Status status : statuses) {
      current = sumOr(current, status);
    }
    return current;
  }
  
  public static Status and(Status lhs, Status rhs) {
    return new Status(
            StatusCode.and(lhs.statusCode(), rhs.statusCode()),
            lhs.recoverableErrorItemCount(),
            lhs.successItemCount(),
            lhs.totalItemCount()
            );
  }
  
  public static Status and(Status ... statuses) {
    Status current = new Status(StatusCode.CONTINUE);
    for(Status status : statuses) {
      current = and(current, status);
    }
    return current;
  }

  public static Status or(Status lhs, Status rhs) {
    return new Status(
            StatusCode.or(lhs.statusCode(), rhs.statusCode()),
            lhs.recoverableErrorItemCount(),
            lhs.successItemCount(),
            lhs.totalItemCount()
            );
  }
  
  public static Status or(Status ... statuses) {
    Status current = new Status(StatusCode.CONTINUE);
    for(Status status : statuses) {
      current = and(current, status);
    }
    return current;
  }
  
  public static final Status CONTINUE = new Status(StatusCode.CONTINUE);
  public static final Status RECOVERABLE_ERROR = new Status(StatusCode.RECOVERABLE_ERROR);
  public static final Status FATAL_ERROR = new Status(StatusCode.FATAL_ERROR);
  public static final Status SUCCESS = new Status(StatusCode.SUCCESS);
  
}
