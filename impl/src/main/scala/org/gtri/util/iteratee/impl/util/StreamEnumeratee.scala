package org.gtri.util.iteratee.impl.util

import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.ProducerStates.Result

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/25/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
// No Producer class or Enumeratee class to discourage holding onto head of stream
object StreamEnumeratee {
  val STD_CHUNK_SIZE = 256
  def apply[A](stream : Stream[A], chunkSize : Int = STD_CHUNK_SIZE) : Producer.State[A] =
    new Cont(stream, chunkSize)

  private class Cont[A](stream : Stream[A],chunkSize : Int) extends Producer.State[A] {

    def status = StatusCode.CONTINUE

    def step = {
      val (nextChunk, nextS) = stream.splitAt(chunkSize)
      println("chunk=" + nextChunk)
      if(nextS.isEmpty) {
        Result(new Success(), nextChunk)
      } else {
        Result(new Cont(nextS, chunkSize), nextChunk)
      }
    }
  }

  private class Success[A] extends Producer.State[A] {
    def status = StatusCode.SUCCESS

    def step = Result(this, Nil)
  }

}
