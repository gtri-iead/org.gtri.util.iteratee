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

import org.gtri.util.iteratee.api.{Issue, Translatee, Iteratee, Translator}

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
  def apply(input: List[A], output: List[C], issues: List[Issue]) = {
    val (nextT1, output1, nextIssues1) = t1.apply(input, Nil, issues)
    val (nextT2, output2, nextIssues2) = t2.apply(output1, output, nextIssues1)
    (TranslateeTuple2(nextT1, nextT2), output2, nextIssues2)
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
  def apply(input: List[A], output: List[D], issues: List[Issue]) = {
    val (nextT1, output1, nextIssues1) = t1.apply(input, Nil, issues)
    val (nextT2, output2, nextIssues2) = t2.apply(output1, Nil, nextIssues1)
    val (nextT3, output3, nextIssues3) = t3.apply(output2, output, nextIssues2)
    (TranslateeTuple3(nextT1, nextT2, nextT3), output3, nextIssues3)
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
  def apply(input: List[A], output: List[E], issues: List[Issue]) = {
    val (nextT1, output1, nextIssues1) = t1.apply(input, Nil, issues)
    val (nextT2, output2, nextIssues2) = t2.apply(output1, Nil, nextIssues1)
    val (nextT3, output3, nextIssues3) = t3.apply(output2, Nil, nextIssues2)
    val (nextT4, output4, nextIssues4) = t4.apply(output3, output, nextIssues3)
    (TranslateeTuple4(nextT1, nextT2, nextT3, nextT4), output4, nextIssues4)
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
  def apply(input: List[A], output: List[F], issues: List[Issue]) = {
    val (nextT1, output1, nextIssues1) = t1.apply(input, Nil, issues)
    val (nextT2, output2, nextIssues2) = t2.apply(output1, Nil, nextIssues1)
    val (nextT3, output3, nextIssues3) = t3.apply(output2, Nil, nextIssues2)
    val (nextT4, output4, nextIssues4) = t4.apply(output3, Nil, nextIssues3)
    val (nextT5, output5, nextIssues5) = t5.apply(output4, output, nextIssues4)
    (TranslateeTuple5(nextT1, nextT2, nextT3, nextT4, nextT5), output5, nextIssues5)
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
