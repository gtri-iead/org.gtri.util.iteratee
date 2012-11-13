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

import org.gtri.util.iteratee.api
import Iteratee._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/4/12
 * Time: 6:45 PM
 * To change this template use File | Settings | File Templates.
 */

trait Producer[A] extends api.Producer[A] {


  def enumerator : Enumerator[A]

  //  def iterator() : Iterator[A] = {
  //    val self = this
  //    new Iterator[A] {
  //      var chunk : Option[Chunk[A]] = None
  //      var i : Option[Iterator[A]] = None
  //      def hasNext = {
  //        if(i == None || i.get.hasNext == false) {
  //          // An iteratee to extract one chunk
  //          chunk = self.enumerate(IterV.head).run
  //          if(chunk == None) {
  //            false
  //          } else {
  //            i = Some(chunk.get.iterator)
  //            i.get.hasNext
  //          }
  //        } else {
  //          true
  //        }
  //      }
  //      def next = {
  //        i.getOrElse({throw new NoSuchElementException()}).next()
  //      }
  //    }
  //  }
}

