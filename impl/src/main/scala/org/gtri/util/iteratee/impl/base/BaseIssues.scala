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
    along with org.gtri.util.iteratee library. If not, see <http:www.gnu.org/licenses/>.

*/

package org.gtri.util.iteratee.impl.base

import org.gtri.util.iteratee.api
import org.gtri.util.iteratee.api.ImmutableDiagnosticLocator
import org.gtri.util.iteratee.api.Issue.ImpactCode
import java.lang.StackTraceElement

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/13/12
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
object BaseIssues {
  class FatalError(throwable : Throwable, locator : ImmutableDiagnosticLocator) extends api.Issue {
    def getLocator = locator

    def getMessage = throwable.getMessage

    def getStackTrace = throwable.getStackTrace

    def getImpactCode = ImpactCode.FATAL_ERROR
  }

  class RecoverableError(throwable : Throwable, locator : ImmutableDiagnosticLocator) extends api.Issue {
    def getLocator = locator

    def getMessage = throwable.getMessage

    def getStackTrace = throwable.getStackTrace

    def getImpactCode = ImpactCode.RECOVERABLE_ERROR
  }

  class Warning(message : String, locator : ImmutableDiagnosticLocator, stackTrace : Array[StackTraceElement] = Array()) extends api.Issue {
    def getLocator = locator

    def getMessage = message

    def getStackTrace = stackTrace

    def getImpactCode = ImpactCode.WARNING
  }

  class Info(message : String, locator : ImmutableDiagnosticLocator, stackTrace : Array[StackTraceElement] = Array()) extends api.Issue {
    def getLocator = locator

    def getMessage = message

    def getStackTrace = stackTrace

    def getImpactCode = ImpactCode.INFO
  }

  class Debug(message : String, locator : ImmutableDiagnosticLocator, stackTrace : Array[StackTraceElement] = Array()) extends api.Issue {
    def getLocator = locator

    def getMessage = message

    def getStackTrace = stackTrace

    def getImpactCode = ImpactCode.DEBUG
  }
}
