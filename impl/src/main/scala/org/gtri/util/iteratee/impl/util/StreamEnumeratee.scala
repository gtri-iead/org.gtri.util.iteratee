package org.gtri.util.iteratee.impl.util

import org.gtri.util.iteratee.api.{Producer, Enumeratee, Issue, Iteratee}
import org.gtri.util.iteratee.impl.base.BaseEnumeratee.base
import org.gtri.util.iteratee.impl.base.BaseEnumeratee

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/25/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
// No Producer class or Enumeratee class to discourage holding onto head of stream
object StreamEnumeratee {
  def apply[A,S](
    stream : Stream[A],
    iteratee : Iteratee[A,S],
    chunkSize : Int = BaseEnumeratee.STD_CHUNK_SIZE
  ) : Enumeratee[A,S] = new Cont(stream, iteratee, Nil, chunkSize)

  private class Cont[S,A](
                 stream : Stream[A],
                 val iteratee : Iteratee[A,S],
                 _issues : List[Issue],
                 chunkSize : Int
                 ) extends base.Cont[A,S](_issues) {

    def attach[T](i: Iteratee[A, T]) = new Cont(stream, i, issues, chunkSize)

    def step = {
      val (chunk, nextS) = stream.splitAt(chunkSize)
      val nextI = iteratee(chunk.toList)
      if(nextS.isEmpty) {
        new Success(nextI, issues)
      } else {
        new Cont(nextS, nextI, issues, chunkSize)
      }
    }
  }
  private class Success[S,A](_iteratee : Iteratee[A,S], _issues : List[Issue]) extends base.Success(_iteratee, _issues)

}
