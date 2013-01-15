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
package org.gtri.util.iteratee.impl.iteratees

import org.gtri.util.scala.exelog.noop._
import org.gtri.util.iteratee.api.{ImmutableBuffer, Iteratee}
import org.gtri.util.iteratee.impl.ImmutableBufferConversions._

object BaseDone {
  implicit val thisclass = classOf[BaseDone[_,_]]
  implicit val log : Log = Logger.getLog(thisclass)
}
abstract class BaseDone[I,O] extends Iteratee.State[I,O]  {
  import BaseDone._

  def apply(items: ImmutableBuffer[I]) = {
    log.block("apply",Seq("items#" -> items.length)) {
      +"Iteratee is done, returning all input as overflow"
      Result(next = this, overflow = items)
    }
  }
  def apply(item: I) = {
    log.block("apply",Seq("item" ->  item)) {
      +"Iteratee is done, returning input item as overflow"
      Result(next = this, overflow = Chunk(item))
    }
  }

  def endOfInput() = {
    log.block("endOfInput") {
      +"Iteratee is done, received eoi - no change in state"
      Result(next = this)
    }
  }
}
