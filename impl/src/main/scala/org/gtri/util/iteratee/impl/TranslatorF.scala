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

import org.gtri.util.scala.exelog.sideeffects._
import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.iteratees._
import ImmutableBufferConversions._

object TranslatorF {
  implicit val classlog = ClassLog(classOf[TranslatorF[_,_]])
}
class TranslatorF[A,B](f: A => B) extends Iteratee[A,B] {
  import TranslatorF._

  object Cont {
    implicit val classlog = ClassLog(classOf[Cont])
  }
  class Cont extends SingleItemCont[A,B] {
    import Cont._

    def apply(item: A) = {
      implicit val log = enter("apply") { "item" -> item :: Nil }
      +"Translate the item and return result"
      Result(this, Chunk(f(item))) <~: log
    }

    def endOfInput = Success()
  }

  def initialState() = new Cont
}
