package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/2/12
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
object ImmutableBuffers {
  def empty[A] = new api.ImmutableBuffer[A] {
    def length = 0

    def apply(i: Int) = throw new NoSuchElementException

    def iterator = new java.util.Iterator[A] {
      def hasNext = false

      def next() = throw new NoSuchElementException

      def remove = throw new UnsupportedOperationException
    }
  }
  object Conversions {
    implicit def immutableBufferToIndexedSeq[A](buffer : api.ImmutableBuffer[A]) : IndexedSeq[A] = {
      new IndexedSeq[A] {
        def length = buffer.length

        def apply(idx: Int) = buffer.apply(idx)
      }
    }
    implicit def seqToImmutableBuffer[A](seq : Seq[A]) : api.ImmutableBuffer[A] = {
      new api.ImmutableBuffer[A] {
        def length = seq.length

        def apply(idx: Int) = seq(idx)

        def iterator = new java.util.Iterator[A] {
          var idx : Int = 0

          def hasNext = idx < seq.length

          def next() = {
            val retv = seq(idx)
            idx += 1
            retv
          }

          def remove = throw new UnsupportedOperationException
        }
      }
    }
  }
}