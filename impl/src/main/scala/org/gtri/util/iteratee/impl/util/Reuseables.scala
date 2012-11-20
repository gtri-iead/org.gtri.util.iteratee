package org.gtri.util.iteratee.impl.util

import org.gtri.util.iteratee.api._

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 11/19/12
* Time: 2:09 AM
* To change this template use File | Settings | File Templates.
*/
case class ReuseableProducer[A,S](e : Enumeratee[A,S]) extends Producer[A] {

  def enumeratee[S](i: Iteratee[A, S]) = e.attach(i)
}

case class ReuseableConsumer[A](i : Iteratee[A, Consumer.State[A]]) extends Consumer[A] {
  def iteratee() = i
}

case class ReuseableBuilder[A,V](i : Iteratee[A,Builder.State[A,V]]) extends Builder[A,V] {
  def iteratee() = i
}
