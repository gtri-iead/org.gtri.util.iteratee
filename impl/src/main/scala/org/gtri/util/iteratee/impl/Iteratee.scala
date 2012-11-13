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

import collection.immutable.Iterable
import org.gtri.util.iteratee.api.Issue

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/11/12
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
object Iteratee {

  type Chunk[A] = Iterable[A]
  type Result[V] = (Option[V], List[Issue])
  type In[A] = (Chunk[A], List[Issue])
  type Iteratee[A,V] = scalaz.IterV[In[A], Result[V]]
  type Input[A] = scalaz.Input[In[A]]

  object EOF {
    def apply[A] : Input[A] = scalaz.IterV.EOF[In[A]]
    def unapply[A](r: Input[A]): Boolean = scalaz.IterV.EOF.unapply(r)
  }

  object Empty {
    def apply[A] : Input[A] = scalaz.IterV.Empty[In[A]]
    def unapply[A](r: Input[A]): Boolean = scalaz.IterV.Empty.unapply(r)
  }

  object El {
    def apply[A](input : => Chunk[A], issues : => List[Issue]) : Input[A] = scalaz.IterV.El[In[A]]((input, issues))
    def apply[A](input : => Chunk[A]) : Input[A] = scalaz.IterV.El[In[A]]((input, Nil))
    def unapply[A](r : Input[A]): Option[In[A]] = scalaz.IterV.El.unapply(r)
  }

  object Done {
    def apply[A, V](valueOption : Option[V], issues : List[Issue], i: => Input[A]) : Iteratee[A,V] = scalaz.IterV.Done((valueOption, issues),i)
    def unapply[A, V](r: Iteratee[A, V]): Option[((Option[V], List[Issue]), Input[A])] = scalaz.IterV.Done.unapply(r)
  }

  object Success {
    def apply[A, V](value : V, issues : List[Issue], i: => Input[A]) : Iteratee[A,V] = {
      val optionValue : Option[V] = Some(value)
      scalaz.IterV.Done((optionValue, issues),i)
    }
    def unapply[A, V](r: Iteratee[A, V]): Option[(V, List[Issue], Input[A])] = {
        r.fold[Option[(V,List[Issue],Input[A])]](
        done = {
          (result, i) => {
            val (valueOption, issues) = result
            if(valueOption.isDefined)
              Some((valueOption.get, issues, i))
            else
              None
          }
        },
        cont = f => None)
    }
  }

  object Failure {
    def apply[A, V](issues : List[Issue], i: => Input[A]) : Iteratee[A,V] = scalaz.IterV.Done((None, issues),i)
    def unapply[A, V](r: Iteratee[A, V]): Option[(List[Issue], Input[A])] = {
      r.fold[Option[(List[Issue],Input[A])]](
        done = {
          (result, i) => {
            val (valueOption, issues) = result
            if(valueOption.isEmpty)
              Some((issues, i))
            else
              None
          }
        },
        cont = f => None)
    }
  }

  object Cont {
    def apply[A, V](f: Input[A] => Iteratee[A,V]) : Iteratee[A,V] = scalaz.IterV.Cont.apply(f)
    def unapply[A, V](r: Iteratee[A,V]): Option[Input[A] => Iteratee[A,V]] = scalaz.IterV.Cont.unapply(r)
  }

  trait Enumerator[A] {
    def enumerate[V](i : Iteratee[A,V]) : Iteratee[A,V]
  }

  trait Enumeratee[A,B] {
    def transform[V](i : Iteratee[B,V]) : Iteratee[A,V]
  }
}
