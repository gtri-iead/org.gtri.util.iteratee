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

import Iteratee._
import org.gtri.util.iteratee.api
import org.gtri.util.iteratee.api.Issue
import collection.immutable.Queue

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/4/12
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
trait Translatee[A,B] {

  def translate[U,V](input : Chunk[A], u: U, v: V, out : (U,B) => U, onIssue: (V,Issue) => V) : (U, V, Translatee[A,B]) = {
    input.foldLeft((u, v, this)) {
      (tuple, a) => {
        val (u, v, translatee) = tuple
        translatee.translate(a, u, v, out, onIssue)
      }
    }
  }

  def translate[U, V](input : A, u : U, v : V, out : (U,B) => U, onIssue : (V,Issue) => V) : (U, V, Translatee[A,B])
}

object Translatee {
  object Error {
    def apply[A,B]() = {
      new Translatee[A,B] {
        def translate[U, V](input : A, y : U, z : V, out : (U,B) => U, onIssue : (V,Issue) => V) : (U, V, Translatee[A,B]) = {
          (y, z, this)
        }
      }
    }
  }
}

trait Translator[A,B] extends api.Translator[A,B] {
  private val self = this
  def translatee : Translatee[A,B]

  def enumeratee : Enumeratee[A,B] = new Enumeratee[A,B] {

    def transform[V](initialInnerIteratee: Iteratee[B,V]) : Iteratee[A,V] = {

      def resolveDoneOrContBasedOnInnerIteratee(
                                                 nextInnerIteratee : Iteratee[B,V],
                                                 nextTranslatee : Translatee[A,B]
                                                 ) : Iteratee[A,V] = {
        nextInnerIteratee match {
          case Failure(issues, remaining : Input[B]) =>
            remaining match {
              case EOF() =>
                Failure(issues, EOF[A])
              case Empty() =>
                Failure(issues, Empty[A])
              case El(input, newIssues) =>
                // If inner iteratee returned some B's have to just toss them
                Failure(newIssues ::: issues, Empty[A])
            }
          case Success(result,issues,remaining) =>
            // Inner iteratee is done - preserve EOF/Empty if it is passing EOF/Empty on
            remaining match {
              case EOF() =>
                Success(result, issues, EOF[A])
              case Empty() =>
                Success(result, issues, Empty[A])
              case El(input, newIssues) =>
                // If inner iteratee returned some B's have to just toss them
                Success(result, newIssues ::: issues, Empty[A])
            }
          case Cont(innerK) =>
            // Inner iteratee is not done - return a continuation
            Cont(step(innerK, nextTranslatee))
        }
      }

      // IterV[A, IterV[B,V]] main step function
      def step(k: (Input[B]) => Iteratee[B,V], currentTranslatee : Translatee[A,B]) : (Input[A]) => Iteratee[A,V] = {

        case El(input, issues) =>
          // Translate input, accumulate output B in an immutable queue
          val (resultsB, moreIssues, nextTranslatee) = currentTranslatee.translate(
            input, Queue[B](), issues,
            out = {
              (queue : Queue[B],b : B) => {
                queue.enqueue(b)
              }
            },
            onIssue = {
              (list : List[Issue], issue : Issue) => {
                issue :: list
              }
            }
          )
          if(currentTranslatee == Translatee.Error) {
            // If an error occurs return done with issues
            Failure(moreIssues, Empty[A])
          } else {
            // No error in translator
            // Push results and advance inner iteratee
            val nextInnerIteratee =
              if(resultsB.nonEmpty) {
                k(El(resultsB, moreIssues))
              } else {
                k(El(Nil, moreIssues))
              }

            resolveDoneOrContBasedOnInnerIteratee(nextInnerIteratee,nextTranslatee)
          }
        case Empty() =>
          val nextInnerIteratee = k(Empty[B])
          resolveDoneOrContBasedOnInnerIteratee(nextInnerIteratee,currentTranslatee)

        case EOF() =>
          val nextInnerIteratee = k(EOF[B])
          resolveDoneOrContBasedOnInnerIteratee(nextInnerIteratee,currentTranslatee)
      }

      resolveDoneOrContBasedOnInnerIteratee(initialInnerIteratee,self.translatee)
    }
  }

}

