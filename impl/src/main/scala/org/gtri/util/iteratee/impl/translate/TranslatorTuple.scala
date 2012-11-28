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

package org.gtri.util.iteratee.impl.translate

import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api.{StatusCode, Issue, Translatee, Translator}
import org.gtri.util.iteratee.impl.Translatees._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/21/12
 * Time: 1:32 AM
 * To change this template use File | Settings | File Templates.
 */
case class TranslateeTuple2[A,B,C](
  t1 : Translatee[A,B],
  t2 : Translatee[B,C]
) extends Translatee[A,C] {

  def status = StatusCode.CONTINUE

  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    Result(TranslateeTuple2(resultT1.next, resultT2.next), resultT2.output, resultT1.issues ++ resultT2.issues)
  }
}
case class TranslatorTuple2[A,B,C](
  t1 : Translator[A,B],
  t2 : Translator[B,C]
  ) extends Translator[A,C] {
  def translatee = TranslateeTuple2(t1.translatee, t2.translatee)
}

case class TranslateeTuple3[A,B,C,D](
  t1 : Translatee[A,B],
  t2 : Translatee[B,C],
  t3 : Translatee[C,D]
) extends Translatee[A,D] {
  def status = StatusCode.CONTINUE

  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    val resultT3 = t3.apply(resultT2.output)
    Result(TranslateeTuple3(resultT1.next, resultT2.next, resultT3.next), resultT3.output, resultT1.issues ++ resultT2.issues ++ resultT3.issues)
  }
}
case class TranslatorTuple3[A,B,C,D](
  t1 : Translator[A,B],
  t2 : Translator[B,C],
  t3 : Translator[C,D]
) extends Translator[A,D] {
  def translatee = TranslateeTuple3(t1.translatee, t2.translatee, t3.translatee)
}

case class TranslateeTuple4[A,B,C,D,E](
  t1 : Translatee[A,B],
  t2 : Translatee[B,C],
  t3 : Translatee[C,D],
  t4 : Translatee[D,E]
) extends Translatee[A,E] {
  def status = StatusCode.CONTINUE

  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    val resultT3 = t3.apply(resultT2.output)
    val resultT4 = t4.apply(resultT3.output)
    Result(TranslateeTuple4(resultT1.next, resultT2.next, resultT3.next, resultT4.next), resultT4.output, resultT1.issues ++ resultT2.issues ++ resultT3.issues ++ resultT4.issues)
  }
}
case class TranslatorTuple4[A,B,C,D,E](
  t1 : Translator[A,B],
  t2 : Translator[B,C],
  t3 : Translator[C,D],
  t4 : Translator[D,E]
) extends Translator[A,E] {
  def translatee = TranslateeTuple4(t1.translatee, t2.translatee, t3.translatee, t4.translatee)
}

case class TranslateeTuple5[A,B,C,D,E,F](
  t1 : Translatee[A,B],
  t2 : Translatee[B,C],
  t3 : Translatee[C,D],
  t4 : Translatee[D,E],
  t5 : Translatee[E,F]
) extends Translatee[A,F] {
  def status = StatusCode.CONTINUE

  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    val resultT3 = t3.apply(resultT2.output)
    val resultT4 = t4.apply(resultT3.output)
    val resultT5 = t5.apply(resultT4.output)
    Result(TranslateeTuple5(resultT1.next, resultT2.next, resultT3.next, resultT4.next, resultT5.next), resultT5.output, resultT1.issues ++ resultT2.issues ++ resultT3.issues ++ resultT4.issues ++ resultT5.issues)
  }
}
case class TranslatorTuple5[A,B,C,D,E,F](
  t1 : Translator[A,B],
  t2 : Translator[B,C],
  t3 : Translator[C,D],
  t4 : Translator[D,E],
  t5 : Translator[E,F]
) extends Translator[A,F] {
  def translatee = TranslateeTuple5(t1.translatee, t2.translatee, t3.translatee, t4.translatee, t5.translatee)
}
