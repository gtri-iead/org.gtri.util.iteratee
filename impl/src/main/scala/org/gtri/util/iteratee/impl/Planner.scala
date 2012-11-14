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
import IterVUtil._
import org.gtri.util.iteratee.api
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/11/12
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
class Planner extends api.Planner {
  def concat[A](lhs : api.Consumer[A], rhs : api.Consumer[A]) : api.Consumer[A] = {
    concat(lhs.asInstanceOf[Consumer[A]], rhs.asInstanceOf[Consumer[A]])
  }

  def concat[A](lhs : Consumer[A], rhs : Consumer[A]) : Consumer[A] = {
    new Consumer[A] {
      def iteratee : Iteratee[A,Unit] = {
        flatMap[In[A], Result[Unit], Result[Unit]](lhs.iteratee, _ => map[In[A], Result[Unit], Result[Unit]](rhs.iteratee, x => x))
      }
    }
  }

  def concat[A,V](lhs : api.Consumer[A], rhs : api.Builder[A,V]) : api.Builder[A,V] = {
    concat(lhs.asInstanceOf[Consumer[A]], rhs.asInstanceOf[Builder[A,V]])
  }

  def concat[A,V](lhs : Consumer[A], rhs : Builder[A,V]) : Builder[A,V] = {
    new Builder[A,V] {
      def iteratee : Iteratee[A,V] = {
        flatMap[In[A], Result[Unit], Result[V]](lhs.iteratee, _ => map[In[A],Result[V],Result[V]](rhs.iteratee, x => x))
      }
    }
  }

  def concat[A](lhs : api.Producer[A], rhs : api.Producer[A]) : api.Producer[A] = {
    concat(lhs.asInstanceOf[Producer[A]], rhs.asInstanceOf[Producer[A]])
  }

  def concat[A](lhs : Producer[A], rhs : Producer[A]) : Producer[A] = {
    new Producer[A] {
      def enumerator = new Enumerator[A] {
        val rhsEnumerator = rhs.enumerator
        val lhsEnumerator = lhs.enumerator
        def enumerate[V](i : Iteratee[A,V]) : Iteratee[A,V] = {
          rhsEnumerator.enumerate(lhsEnumerator.enumerate(i))
        }
        def close() {
          lhsEnumerator.close()
          rhsEnumerator.close()
        }
      }
    }
  }

  def connect[A](producer : api.Producer[A], consumer : api.Consumer[A]) : api.Consumer.Plan[A] = {
    connect(producer.asInstanceOf[Producer[A]], consumer.asInstanceOf[Consumer[A]])
  }

  def connect[A](producer : Producer[A], consumer : Consumer[A]) : api.Consumer.Plan[A] = {
    new api.Consumer.Plan[A] {
      def run = {
        val e : Enumerator[A] = producer.enumerator
        val i = e.enumerate(consumer.iteratee)
        val (optionValue, issues) = i.run
        e.close()
        new api.Consumer.Result[A] {
          def getIssues : java.util.List[api.Issue] = ListBuffer(issues : _*)
          def isSuccess = optionValue.isDefined
          def getProducer : api.Producer[A] =
            new Producer[A] {
              def enumerator : Enumerator[A] = e
            }
          def getConsumer : api.Consumer[A] =
            new Consumer[A] {
              def iteratee = i
            }
        }
      }
    }
  }

  def connect[A,V](producer : api.Producer[A], builder : api.Builder[A,V]) : api.Builder.Plan[A,V] = {
    connect[A,V](producer.asInstanceOf[Producer[A]], builder.asInstanceOf[Builder[A,V]])
  }

  def connect[A,V](producer : Producer[A], builder : Builder[A,V]) : api.Builder.Plan[A,V] = {
    new api.Builder.Plan[A,V] {
      def run = {
        val e = producer.enumerator
        val i = e.enumerate(builder.iteratee)
        val (optionValue, issues) = i.run
        e.close()
        new api.Builder.Result[A,V] {
          def getIssues : java.util.List[api.Issue] = ListBuffer(issues : _*)
          def isSuccess : Boolean = optionValue.isDefined
          def get : V= optionValue.get
          def getProducer : api.Producer[A] =
            new Producer[A] {
              def enumerator : Enumerator[A] = e
            }
          def getBuilder : api.Builder[A,V] =
            new Builder[A,V] {
              def iteratee = i
            }
        }

      }
    }
  }

  def translate[A,B](translator : api.Translator[A,B], Consumer : api.Consumer[B]) : api.Consumer[A] = {
    translate(translator.asInstanceOf[Translator[A,B]], Consumer.asInstanceOf[Consumer[B]])
  }

  def translate[A,B](translator : Translator[A,B], consumer : Consumer[B]) : Consumer[A] = {
    new Consumer[A] {
      def iteratee : Iteratee[A,Unit] = {
        translator.enumeratee.transform(consumer.iteratee)
      }
    }
  }

  def translate[A,B,V](translator : api.Translator[A,B], builder : api.Builder[B,V]) : api.Builder[A, V] = {
    translate(translator.asInstanceOf[Translator[A,B]], builder.asInstanceOf[Builder[B,V]])
  }

  def translate[A,B,V](translator : Translator[A,B], builder : Builder[B,V]) : Builder[A, V] = {
    new Builder[A, V] {
      def iteratee : Iteratee[A,V] = {
        translator.enumeratee.transform(builder.iteratee)
      }
    }
  }
}
