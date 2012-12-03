package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api._
import scala.collection.immutable.Seq
import org.gtri.util.iteratee.impl.Enumerators._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/25/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
class SeqEnumerator[A](traversable : Seq[A], chunkSize : Int = STD_CHUNK_SIZE) extends Enumerator[A] {
  import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._
  
  class Cont[A](current : Seq[A]) extends Enumerators.Cont[A] {
    val progress = new Progress()

    def step = {
      val (nextChunk, remaining) = current.splitAt(chunkSize)
      println("chunk=" + nextChunk)
      if(remaining.isEmpty) {
        Result(Success(progress), nextChunk)
      } else {
        Result(new Cont(remaining), nextChunk)
      }
    }
  }

  class ContWithDefSize[A](current : Seq[A]) extends Enumerators.Cont[A] {
    val progress = new Progress(0,traversable.size - current.size, traversable.size)

    def step = {
      val (nextChunk, remaining) = current.splitAt(chunkSize)
      println("chunk=" + nextChunk)
      if(remaining.isEmpty) {
        Result(Success(progress), nextChunk)
      } else {
        Result(new ContWithDefSize(remaining), nextChunk)
      }
    }
  }

  def initialState() = {
    if (traversable.hasDefiniteSize) {
      new ContWithDefSize(traversable)
    } else {
      new Cont(traversable)
    }
  }
}
