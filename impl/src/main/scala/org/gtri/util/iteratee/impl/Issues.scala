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

package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api
import org.gtri.util.iteratee.api.ImmutableDiagnosticLocator
import org.gtri.util.iteratee.api.Issue.ImpactCode

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/13/12
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
object Issues {
  private val nowhere = new ImmutableDiagnosticLocator {
    override def toString = ""
  }
  case class FatalError(throwable : java.lang.Throwable, locator : ImmutableDiagnosticLocator = nowhere) extends api.Issue {

    def message = throwable.getMessage

    def stackTrace = throwable.getStackTrace

    def impactCode = ImpactCode.FATAL_ERROR
  }

  case class RecoverableError(throwable : java.lang.Throwable, locator : ImmutableDiagnosticLocator = nowhere) extends api.Issue {
    def message = throwable.getMessage

    def stackTrace = throwable.getStackTrace

    def impactCode = ImpactCode.RECOVERABLE_ERROR
  }

  case class Warning(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) extends api.Issue {
    def impactCode = ImpactCode.WARNING
  }

  case class Info(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) extends api.Issue {
    def impactCode = ImpactCode.INFO
  }

  case class Debug(message : String, locator : ImmutableDiagnosticLocator = nowhere, stackTrace : Array[java.lang.StackTraceElement] = Array()) extends api.Issue {
    def impactCode = ImpactCode.DEBUG
  }
}
