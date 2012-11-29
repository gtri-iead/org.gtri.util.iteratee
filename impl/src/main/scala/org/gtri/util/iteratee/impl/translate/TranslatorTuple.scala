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
import org.gtri.util.iteratee.api.Translator
import org.gtri.util.iteratee.impl.TranslatorStates._
import org.gtri.util.iteratee.impl.TranslatorStates

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/21/12
 * Time: 1:32 AM
 * To change this template use File | Settings | File Templates.
 */
case class TranslatorStateTuple2[A,B,C](
  t1 : Translator.State[A,B],
  t2 : Translator.State[B,C]
) extends TranslatorStates.Cont[A,C] {

  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    Result(TranslatorStateTuple2(resultT1.next, resultT2.next), resultT2.output, resultT1.overflow, resultT1.issues ++ resultT2.issues)
  }

  def endOfInput() = Result(Success())
}

case class TranslatorTuple2[A,B,C](
  t1 : Translator[A,B],
  t2 : Translator[B,C]
  ) extends Translator[A,C] {
  def initialState = TranslatorStateTuple2(t1.initialState, t2.initialState)
}

case class TranslatorStateTuple3[A,B,C,D](
  t1 : Translator.State[A,B],
  t2 : Translator.State[B,C],
  t3 : Translator.State[C,D]
) extends TranslatorStates.Cont[A,D] {
  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    val resultT3 = t3.apply(resultT2.output)
    Result(TranslatorStateTuple3(resultT1.next, resultT2.next, resultT3.next), resultT3.output, resultT1.overflow, resultT1.issues ++ resultT2.issues ++ resultT3.issues)
  }

  def endOfInput() = Result(Success())
}
case class TranslatorTuple3[A,B,C,D](
  t1 : Translator[A,B],
  t2 : Translator[B,C],
  t3 : Translator[C,D]
) extends Translator[A,D] {
  def initialState = TranslatorStateTuple3(t1.initialState, t2.initialState, t3.initialState)
}

case class TranslatorStateTuple4[A,B,C,D,E](
  t1 : Translator.State[A,B],
  t2 : Translator.State[B,C],
  t3 : Translator.State[C,D],
  t4 : Translator.State[D,E]
) extends TranslatorStates.Cont[A,E] {

  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    val resultT3 = t3.apply(resultT2.output)
    val resultT4 = t4.apply(resultT3.output)
    Result(TranslatorStateTuple4(resultT1.next, resultT2.next, resultT3.next, resultT4.next), resultT4.output, resultT1.overflow, resultT1.issues ++ resultT2.issues ++ resultT3.issues ++ resultT4.issues)
  }

  def endOfInput() = Result(Success())
}
case class TranslatorTuple4[A,B,C,D,E](
  t1 : Translator[A,B],
  t2 : Translator[B,C],
  t3 : Translator[C,D],
  t4 : Translator[D,E]
) extends Translator[A,E] {
  def initialState = TranslatorStateTuple4(t1.initialState, t2.initialState, t3.initialState, t4.initialState)
}

case class TranslatorStateTuple5[A,B,C,D,E,F](
  t1 : Translator.State[A,B],
  t2 : Translator.State[B,C],
  t3 : Translator.State[C,D],
  t4 : Translator.State[D,E],
  t5 : Translator.State[E,F]
) extends TranslatorStates.Cont[A,F] {
  def apply(input: Traversable[A]) = {
    val resultT1 = t1.apply(input)
    val resultT2 = t2.apply(resultT1.output)
    val resultT3 = t3.apply(resultT2.output)
    val resultT4 = t4.apply(resultT3.output)
    val resultT5 = t5.apply(resultT4.output)
    Result(TranslatorStateTuple5(resultT1.next, resultT2.next, resultT3.next, resultT4.next, resultT5.next), resultT5.output, resultT1.overflow, resultT1.issues ++ resultT2.issues ++ resultT3.issues ++ resultT4.issues ++ resultT5.issues)
  }

  def endOfInput() = Result(Success())
}
case class TranslatorTuple5[A,B,C,D,E,F](
  t1 : Translator[A,B],
  t2 : Translator[B,C],
  t3 : Translator[C,D],
  t4 : Translator[D,E],
  t5 : Translator[E,F]
) extends Translator[A,F] {
  def initialState = TranslatorStateTuple5(t1.initialState, t2.initialState, t3.initialState, t4.initialState, t5.initialState)
}
