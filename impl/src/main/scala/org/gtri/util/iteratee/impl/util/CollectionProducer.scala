package org.gtri.util.iteratee.impl.util

import org.gtri.util.iteratee.api._
import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.impl.Producers._
import org.gtri.util.iteratee.impl.Producers

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/25/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
// No Producer class or Enumeratee class to discourage holding onto head of stream
class CollectionProducer[A](collection : Traversable[A], chunkSize : Int = STD_CHUNK_SIZE) extends Producer[A] {

  class Cont[A](collection : Traversable[A],chunkSize : Int) extends Producers.Cont[A] {

    def step = {
      val (nextChunk, nextS) = collection.splitAt(chunkSize)
      println("chunk=" + nextChunk)
      if(nextS.isEmpty) {
        Result(Success(), nextChunk)
      } else {
        Result(new Cont(nextS, chunkSize), nextChunk)
      }
    }
  }

  def initialState() = new Cont(collection, chunkSize)
}
