package org.gtri.util.iteratee.impl.translate

import org.gtri.util.iteratee.api.{Iteratee, Translator}

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/21/12
 * Time: 1:32 AM
 * To change this template use File | Settings | File Templates.
 */
case class TranslatorTuple2[A,B,C](
  t1 : Translator[A,B],
  t2 : Translator[B,C]
  ) extends Translator[A,C] {
  def translatee[S](i: Iteratee[C, S]) = {
    t1.translatee(t2.translatee(i))
  }
}

case class TranslatorTuple3[A,B,C,D](
   t1 : Translator[A,B],
   t2 : Translator[B,C],
   t3 : Translator[C,D]
   ) extends Translator[A,D] {
  def translatee[S](i: Iteratee[D, S]) = {
    t1.translatee(t2.translatee(t3.translatee(i)))
  }
}

case class TranslatorTuple4[A,B,C,D,E](
  t1 : Translator[A,B],
  t2 : Translator[B,C],
  t3 : Translator[C,D],
  t4 : Translator[D,E]
) extends Translator[A,E] {
  def translatee[S](i: Iteratee[E, S]) = {
    t1.translatee(t2.translatee(t3.translatee(t4.translatee(i))))
  }
}

case class TranslatorTuple5[A,B,C,D,E,F](
  t1 : Translator[A,B],
  t2 : Translator[B,C],
  t3 : Translator[C,D],
  t4 : Translator[D,E],
  t5 : Translator[E,F]
) extends Translator[A,F] {
  def translatee[S](i: Iteratee[F, S]) = {
    t1.translatee(t2.translatee(t3.translatee(t4.translatee(t5.translatee(i)))))
  }
}
