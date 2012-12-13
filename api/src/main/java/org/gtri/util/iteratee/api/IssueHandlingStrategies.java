///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.gtri.util.iteratee.api;
//
///**
// *
// * @author lance.gatlin@gmail.com
// */
//public class IssueHandlingStrategies {
//  public static class Normal implements IssueHandlingStrategy {
//
//    @Override
//    public boolean canContinue(Issue issue) {
//      switch(issue.impactCode()) {
//        case RECOVERABLE :
//        case FATAL :
//          return false;
//        case NONE :
//        case WARNING :
//      }
//      return true;
//    }
//    
//  }
//  
//  public static class Recover implements IssueHandlingStrategy {
//    
//    @Override
//    public boolean canContinue(Issue issue) {
//      switch(issue.impactCode()) {
//        case FATAL :
//          return false;
//        case NONE :
//        case RECOVERABLE :
//        case WARNING :
//      }
//      return true;
//    }
//  }
//  
//  public static class Strict implements IssueHandlingStrategy {
//    
//    @Override
//    public boolean canContinue(Issue issue) {
//      switch(issue.impactCode()) {
//        case RECOVERABLE :
//        case WARNING :
//        case FATAL :
//          return false;
//        case NONE :
//      }
//      return true;
//    }
//    
//  }
//}
