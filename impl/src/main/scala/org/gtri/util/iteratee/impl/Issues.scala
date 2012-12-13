///*
//    Copyright 2012 Georgia Tech Research Institute
//
//    Author: lance.gatlin@gtri.gatech.edu
//
//    This file is part of org.gtri.util.iteratee library.
//
//    org.gtri.util.iteratee library is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    org.gtri.util.iteratee library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with org.gtri.util.iteratee library. If not, see <http://www.gnu.org/licenses/>.
//
//*/
//
//package org.gtri.util.iteratee.impl
//
//import org.gtri.util.iteratee.api
//import org.gtri.util.iteratee.api.ImmutableDiagnosticLocator
//import org.gtri.util.iteratee.api.ImmutableDiagnosticLocator.nowhere
//import api.Issue.{ReasonCode, ImpactCode}
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/13/12
// * Time: 12:01 AM
// * To change this template use File | Settings | File Templates.
// */
//object Issues {
//  case class Issue(
//                    message : String,
//                    impactCode : ImpactCode,
//                    reasonCode : ReasonCode,
//                    locator : ImmutableDiagnosticLocator = nowhere,
//                    cause : java.lang.Throwable = new java.lang.Throwable()
//                    ) extends api.Issue
//  object Issue {
//    def cfgFatal(message : String) =
//      new Issue(message, ImpactCode.FATAL, ReasonCode.CONFIGURATION)
//    def cfgRecoverable(message : String) =
//      new Issue(message, ImpactCode.RECOVERABLE, ReasonCode.CONFIGURATION)
//    def cfgWarning(message : String) =
//      new Issue(message, ImpactCode.WARNING, ReasonCode.CONFIGURATION)
//
//    def fatal(cause : java.lang.Exception) =
//      new Issue(message = cause.getMessage, impactCode = ImpactCode.FATAL, reasonCode = ReasonCode.INTERNAL, cause = cause)
//    def recoverable(cause : java.lang.Exception) =
//      new Issue(message = cause.getMessage, impactCode = ImpactCode.FATAL, reasonCode = ReasonCode.INTERNAL, cause = cause)
//    def warning(cause : java.lang.Exception) =
//      new Issue(message = cause.getMessage, impactCode = ImpactCode.FATAL, reasonCode = ReasonCode.INTERNAL, cause = cause)
//
//    def inputFatal(message : String, locator : ImmutableDiagnosticLocator = nowhere, cause: java.lang.Throwable = new Throwable()) =
//      new Issue(message, ImpactCode.FATAL, ReasonCode.INPUT, locator, cause)
//    def inputRecoverable(message : String, locator : ImmutableDiagnosticLocator = nowhere, cause: java.lang.Throwable = new Throwable()) =
//      new Issue(message, ImpactCode.RECOVERABLE, ReasonCode.INPUT, locator, cause)
//    def inputWarning(message : String, locator : ImmutableDiagnosticLocator = nowhere, cause: java.lang.Throwable = new Throwable()) =
//      new Issue(message, ImpactCode.WARNING, ReasonCode.INPUT, locator, cause)
//
//    def debug()
//  }
//
//  case class CfgErr(throwable : java.lang.Throwable, locator : ImmutableDiagnosticLocator = nowhere) extends api.Issue {
//
//    def message = throwable.getMessage
//
//    def stackTrace = throwable.getStackTrace
//
//    def impactCode = ImpactCode.FATAL_ERROR
//  }
//
//  case class RecoverableError(throwable : java.lang.Throwable, locator : ImmutableDiagnosticLocator = nowhere) extends api.Issue {
//    def message = throwable.getMessage
//
//    def stackTrace = throwable.getStackTrace
//
//    def impactCode = ImpactCode.RECOVERABLE_ERROR
//  }
//
//  case class Warning(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) extends api.Issue {
//    def impactCode = ImpactCode.WARNING
//  }
//
//  case class Info(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) extends api.Issue {
//    def impactCode = ImpactCode.INFO
//  }
//
//  case class Debug(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) extends api.Issue {
//    def impactCode = ImpactCode.DEBUG
//  }
//}
