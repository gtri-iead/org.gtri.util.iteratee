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

import org.gtri.util.iteratee.api._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/4/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
object Box {
  object conversions {
    implicit def boxAnything[A](a : A) : Box[A] = {
      Box(a)
    }
  }
  def empty[A] = new EmptyBox[A]
  def empty[A](issue : Issue) = new EmptyBox[A](List(issue))
  def empty[A](issues : List[Issue]) = new EmptyBox[A](issues)
  def apply[A](a : A) = new FullBox[A](a)
  def apply[A](a : A, issues : List[Issue]) = new FullBox[A](a, issues)

  def examples {
    val b1 : Box[Int] = Box(1)
    val b2 : Box[Int] = Box(2)
    b2 match { // open a box to see if there is anything there
      case FullBox(item, log) =>
        println(item)
        log foreach { println(_) }
      case EmptyBox(log) =>
        log foreach { println(_) }
    }
    val b3 = b1 fold b2 // replace contents of b1 with contents of b2 (since they are both full) and append their logs
    val b4 = Box.empty[Int]
    val b5 : Box[Int] = b1 fold b4 // doesn't replace b1 since b4 is empty, but logs are still appended
    val b6 : Box[String] = Box("asdf")
    val b7 : Box2[Int,String] = b1 cram b6 // cram the contents of two boxes together (box is only full if b1 and b6 are full also appends logs)
    val b8 : Box3[Int,String,Int] = b7 cram b2 // cram more stuff into our boxes
    val b9 : Box[String] = b8 { // apply a function to the crammed box b8
      (a : Int,b : String,c : Int) => { // function is only called if b8 is full
        Box("asdf") // Box up a result - logs will be appended
      }
    } // b9 now has "asdf" and a concat of logs from b1,b6,b2 and itself
  }
}

sealed trait Box[+A] extends LogWriter[Issue, Box[A]] {

  def isEmpty : Boolean
  def nonEmpty = isEmpty == false
  def get : A
  def toOption : Option[A]

  def empty : Box[A]

  def <<[AA >: A](rhs : Box[AA]) = fold(rhs)
  def fold[AA >: A](rhs : Box[AA]) : Box[AA] = {
    if(isEmpty) {
      if(rhs.isEmpty) {
        this.append(rhs.log)
      } else {
        rhs.prepend(this.log)
      }
    } else {
      if(rhs.isEmpty) {
        this.append(rhs.log)
      } else {
        // If both boxes have a value, replace this with rhs
        rhs.prepend(this.log)
      }
    }
  }

  def &&&[B](rhs : Box[B]) : Box2[A,B] = cram(rhs)
  def cram[B](rhs : Box[B]) : Box2[A,B] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox2(rhs.log ::: log)
    } else {
      new FullBox2(get, rhs.get, rhs.log ::: log)
    }
  }

//  def |||[B](rhs : Box[B]) : FullBox2[Option[A],Option[B]] = stack(rhs)
//  def stack[B](rhs : Box[B]) : FullBox2[Option[A],Option[B]] = {
//    new FullBox2(toOption, rhs.toOption, rhs.log ::: log)
//  }

  def apply[B](f: A => Box[B]) : Box[B] = flatMap(f)

  def flatMap[B](f: A => Box[B]) : Box[B] = {
    if(isEmpty) {
      new EmptyBox[B](log)
    } else {
      f(get).prepend(log)
    }
  }
  def map[B](f: A => B) : Box[B] = {
    if(isEmpty) {
      new EmptyBox[B](log)
    } else {
      new FullBox(f(get), log)
    }
  }
}

final case class EmptyBox[+A](log : List[Issue] = Nil) extends Box[A] {

  def isEmpty = true

  def get = throw new NoSuchElementException

  def toOption = None

  def empty = this

  def copy(newLog: List[Issue]) = new EmptyBox[A](newLog)
}

final case class FullBox[+A] (item : A, log : List[Issue] = Nil) extends Box[A] {
  def isEmpty = false

  def get = item
  def toOption = Some(item)

  def empty = new EmptyBox(log)

  def copy(newLog: List[Issue]) = new FullBox[A](item, newLog)
}

sealed trait Box2[+A,+B] extends LogWriter[Issue, Box2[A,B]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B

  def &&&[C](rhs : Box[C]) : Box3[A,B,C] = cram(rhs)
  def cram[C](rhs : Box[C]) : Box3[A,B,C] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox3(rhs.log ::: log)
    } else {
      new FullBox3(_1, _2, rhs.get, rhs.log ::: log)
    }
  }

  def apply[C](f: (A,B) => Box[C]) : Box[C] = {
    if(isEmpty) {
      new EmptyBox[C](log)
    } else {
      f(_1,_2).prepend(this.log)
    }
  }
}

final case class EmptyBox2[+A,+B](log : List[Issue] = Nil) extends Box2[A,B]{

  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox2[A,B](newLog)
}

final case class FullBox2[+A,+B] (_1 : A, _2 : B, log : List[Issue] = Nil) extends Box2[A,B] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox2[A,B](_1, _2, newLog)
}

sealed trait Box3[+A,+B,+C] extends LogWriter[Issue, Box3[A,B,C]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B
  def _3 : C
  
  def &&&[D](rhs : Box[D]) : Box4[A,B,C,D] = cram(rhs)
  def cram[D](rhs : Box[D]) : Box4[A,B,C,D] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox4(rhs.log ::: log)
    } else {
      new FullBox4(_1,_2,_3, rhs.get, rhs.log::: log)
    }
  }

  def apply[D](f: (A,B,C) => Box[D]) : Box[D] = {
    if(isEmpty) {
      new EmptyBox[D](log)
    } else {
      f(_1,_2,_3).prepend(this.log)
    }
  }

}

final case class EmptyBox3[+A,+B,+C](log : List[Issue] = Nil) extends Box3[A,B,C] {
  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def _3 = throw new NoSuchElementException

  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox3(newLog)
}

final case class FullBox3[+A,+B,+C] (_1 : A, _2 : B, _3 : C, log : List[Issue] = Nil) extends Box3[A,B,C] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox3(_1,_2,_3, newLog)
}

sealed trait Box4[+A,+B,+C,+D] extends LogWriter[Issue, Box4[A,B,C,D]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B
  def _3 : C
  def _4 : D
  
  def &&&[E](rhs : Box[E]) : Box5[A,B,C,D,E] = cram(rhs)
  def cram[E](rhs : Box[E]) : Box5[A,B,C,D,E] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox5(rhs.log ::: log)
    } else {
      new FullBox5(_1,_2,_3,_4,rhs.get, rhs.log::: log)
    }
  }

  def apply[E](f: (A,B,C,D) => Box[E]) : Box[E] = {
    if(isEmpty) {
      new EmptyBox[E](log)
    } else {
      f(_1,_2,_3,_4).prepend(this.log)
    }
  }

}

final case class EmptyBox4[+A,+B,+C,+D](log : List[Issue] = Nil) extends Box4[A,B,C,D] {
  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def _3 = throw new NoSuchElementException

  def _4 = throw new NoSuchElementException
  
  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox4(newLog)
}

final case class FullBox4[+A,+B,+C,+D] (_1 : A, _2 : B, _3 : C, _4 : D, log : List[Issue] = Nil) extends Box4[A,B,C,D] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox4(_1,_2,_3,_4,newLog)
}

sealed trait Box5[+A,+B,+C,+D,+E] extends LogWriter[Issue, Box5[A,B,C,D,E]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B
  def _3 : C
  def _4 : D
  def _5 : E

  def &&&[F](rhs : Box[F]) : Box6[A,B,C,D,E,F] = cram(rhs)
  def cram[F](rhs : Box[F]) : Box6[A,B,C,D,E,F] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox6(rhs.log ::: log)
    } else {
      new FullBox6(_1,_2,_3, _4,_5,rhs.get, rhs.log::: log)
    }
  }

  def apply[F](f: (A,B,C,D,E) => Box[F]) : Box[F] = {
    if(isEmpty) {
      new EmptyBox[F](log)
    } else {
      f(_1,_2,_3,_4,_5).prepend(this.log)
    }
  }

}

final case class EmptyBox5[+A,+B,+C,+D,+E](log : List[Issue] = Nil) extends Box5[A,B,C,D,E] {
  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def _3 = throw new NoSuchElementException

  def _4 = throw new NoSuchElementException
  
  def _5 = throw new NoSuchElementException

  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox5(newLog)
}

final case class FullBox5[+A,+B,+C,+D,+E] (_1 : A, _2 : B, _3 : C, _4 : D, _5 : E, log : List[Issue] = Nil) extends Box5[A,B,C,D,E] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox5(_1,_2,_3,_4,_5,newLog)
}

sealed trait Box6[+A,+B,+C,+D,+E,+F] extends LogWriter[Issue, Box6[A,B,C,D,E,F]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B
  def _3 : C
  def _4 : D
  def _5 : E
  def _6 : F

  def &&&[G](rhs : Box[G]) : Box7[A,B,C,D,E,F,G] = cram(rhs)
  def cram[G](rhs : Box[G]) : Box7[A,B,C,D,E,F,G] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox7(rhs.log ::: log)
    } else {
      new FullBox7(_1,_2,_3, _4,_5,_6,rhs.get, rhs.log::: log)
    }
  }

  def apply[G](f: (A,B,C,D,E,F) => Box[G]) : Box[G] = {
    if(isEmpty) {
      new EmptyBox[G](log)
    } else {
      f(_1,_2,_3,_4,_5,_6).prepend(this.log)
    }
  }

}

final case class EmptyBox6[+A,+B,+C,+D,+E,+F](log : List[Issue] = Nil) extends Box6[A,B,C,D,E,F] {
  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def _3 = throw new NoSuchElementException

  def _4 = throw new NoSuchElementException

  def _5 = throw new NoSuchElementException
  
  def _6 = throw new NoSuchElementException
  
  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox6(newLog)
}

final case class FullBox6[+A,+B,+C,+D,+E,+F] (_1 : A, _2 : B, _3 : C, _4 : D, _5 : E, _6 : F, log : List[Issue] = Nil) extends Box6[A,B,C,D,E,F] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox6(_1,_2,_3,_4,_5,_6,newLog)
}

sealed trait Box7[+A,+B,+C,+D,+E,+F,+G] extends LogWriter[Issue, Box7[A,B,C,D,E,F,G]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B
  def _3 : C
  def _4 : D
  def _5 : E
  def _6 : F
  def _7 : G

  def &&&[H](rhs : Box[H]) : Box8[A,B,C,D,E,F,G,H] = cram(rhs)
  def cram[H](rhs : Box[H]) : Box8[A,B,C,D,E,F,G,H] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox8(rhs.log ::: log)
    } else {
      new FullBox8(_1,_2,_3, _4,_5,_6,_7,rhs.get, rhs.log::: log)
    }
  }

  def apply[H](f: (A,B,C,D,E,F,G) => Box[H]) : Box[H] = {
    if(isEmpty) {
      new EmptyBox[H](log)
    } else {
      f(_1,_2,_3,_4,_5,_6,_7).prepend(this.log)
    }
  }

}

final case class EmptyBox7[+A,+B,+C,+D,+E,+F,+G](log : List[Issue] = Nil) extends Box7[A,B,C,D,E,F,G] {
  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def _3 = throw new NoSuchElementException

  def _4 = throw new NoSuchElementException

  def _5 = throw new NoSuchElementException
  
  def _6 = throw new NoSuchElementException
  
  def _7 = throw new NoSuchElementException
  
  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox7(newLog)
}

final case class FullBox7[+A,+B,+C,+D,+E,+F,+G] (_1 : A, _2 : B, _3 : C, _4 : D, _5 : E, _6 : F, _7 : G, log : List[Issue] = Nil) extends Box7[A,B,C,D,E,F,G] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox7(_1,_2,_3,_4,_5,_6,_7,newLog)
}

sealed trait Box8[+A,+B,+C,+D,+E,+F,+G,+H] extends LogWriter[Issue, Box8[A,B,C,D,E,F,G,H]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B
  def _3 : C
  def _4 : D
  def _5 : E
  def _6 : F
  def _7 : G
  def _8 : H

  def &&&[I](rhs : Box[I]) : Box9[A,B,C,D,E,F,G,H,I] = cram(rhs)
  def cram[I](rhs : Box[I]) : Box9[A,B,C,D,E,F,G,H,I] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox9(rhs.log ::: log)
    } else {
      new FullBox9(_1,_2,_3, _4,_5,_6,_7,_8,rhs.get, rhs.log::: log)
    }
  }

  def apply[I](f: (A,B,C,D,E,F,G,H) => Box[I]) : Box[I] = {
    if(isEmpty) {
      new EmptyBox[I](log)
    } else {
      f(_1,_2,_3,_4,_5,_6,_7,_8).prepend(this.log)
    }
  }

}

final case class EmptyBox8[+A,+B,+C,+D,+E,+F,+G,+H](log : List[Issue] = Nil) extends Box8[A,B,C,D,E,F,G,H] {
  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def _3 = throw new NoSuchElementException

  def _4 = throw new NoSuchElementException

  def _5 = throw new NoSuchElementException

  def _6 = throw new NoSuchElementException

  def _7 = throw new NoSuchElementException

  def _8 = throw new NoSuchElementException
  
  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox8(newLog)
}

final case class FullBox8[+A,+B,+C,+D,+E,+F,+G,+H] (_1 : A, _2 : B, _3 : C, _4 : D, _5 : E, _6 : F, _7 : G, _8 : H, log : List[Issue] = Nil) extends Box8[A,B,C,D,E,F,G,H] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox8(_1,_2,_3,_4,_5,_6,_7,_8,newLog)
}

sealed trait Box9[+A,+B,+C,+D,+E,+F,+G,+H,+I] extends LogWriter[Issue, Box9[A,B,C,D,E,F,G,H,I]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B
  def _3 : C
  def _4 : D
  def _5 : E
  def _6 : F
  def _7 : G
  def _8 : H
  def _9 : I

  def &&&[J](rhs : Box[J]) : Box10[A,B,C,D,E,F,G,H,I,J] = cram(rhs)
  def cram[J](rhs : Box[J]) : Box10[A,B,C,D,E,F,G,H,I,J] = {
    if(isEmpty || rhs.isEmpty) {
      new EmptyBox10(rhs.log ::: log)
    } else {
      new FullBox10(_1,_2,_3, _4,_5,_6,_7,_8,_9,rhs.get, rhs.log::: log)
    }
  }

  def apply[J](f: (A,B,C,D,E,F,G,H,I) => Box[J]) : Box[J] = {
    if(isEmpty) {
      new EmptyBox[J](log)
    } else {
      f(_1,_2,_3,_4,_5,_6,_7,_8,_9).prepend(this.log)
    }
  }

}

final case class EmptyBox9[+A,+B,+C,+D,+E,+F,+G,+H,+I](log : List[Issue] = Nil) extends Box9[A,B,C,D,E,F,G,H,I] {
  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def _3 = throw new NoSuchElementException

  def _4 = throw new NoSuchElementException

  def _5 = throw new NoSuchElementException

  def _6 = throw new NoSuchElementException

  def _7 = throw new NoSuchElementException

  def _8 = throw new NoSuchElementException
  
  def _9 = throw new NoSuchElementException

  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox9(newLog)
}

final case class FullBox9[+A,+B,+C,+D,+E,+F,+G,+H,+I] (_1 : A, _2 : B, _3 : C, _4 : D, _5 : E, _6 : F, _7 : G, _8 : H, _9 : I, log : List[Issue] = Nil) extends Box9[A,B,C,D,E,F,G,H,I] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox9(_1,_2,_3,_4,_5,_6,_7,_8,_9,newLog)
}

sealed trait Box10[+A,+B,+C,+D,+E,+F,+G,+H,+I,+J] extends LogWriter[Issue, Box10[A,B,C,D,E,F,G,H,I,J]] {
  def isEmpty : Boolean

  def _1 : A
  def _2 : B
  def _3 : C
  def _4 : D
  def _5 : E
  def _6 : F
  def _7 : G
  def _8 : H
  def _9 : I
  def _10 : J

//  def &&&[K](rhs : Box[K]) : Box11[A,B,C,D,E,F,G,H,I,J,K] = cram(rhs)
//  def cram[K](rhs : Box[K]) : Box11[A,B,C,D,E,F,G,H,I,J,K] = {
//    if(isEmpty || rhs.isEmpty) {
//      new EmptyBox11(rhs.log ::: log)
//    } else {
//      new FullBox11(_1,_2,_3, _4,_5,_6,_7,_8,_9,_10,rhs.get, rhs.log::: log)
//    }
//  }

  def apply[K](f: (A,B,C,D,E,F,G,H,I,J) => Box[K]) : Box[K] = {
    if(isEmpty) {
      new EmptyBox[K](log)
    } else {
      f(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10).prepend(this.log)
    }
  }

}

final case class EmptyBox10[+A,+B,+C,+D,+E,+F,+G,+H,+I,+J](log : List[Issue] = Nil) extends Box10[A,B,C,D,E,F,G,H,I,J] {
  def _1 = throw new NoSuchElementException

  def _2 = throw new NoSuchElementException

  def _3 = throw new NoSuchElementException

  def _4 = throw new NoSuchElementException

  def _5 = throw new NoSuchElementException

  def _6 = throw new NoSuchElementException

  def _7 = throw new NoSuchElementException

  def _8 = throw new NoSuchElementException

  def _9 = throw new NoSuchElementException

  def _10 = throw new NoSuchElementException

  def isEmpty = true

  def copy(newLog: List[Issue]) = new EmptyBox10(newLog)
}

final case class FullBox10[+A,+B,+C,+D,+E,+F,+G,+H,+I,+J] (_1 : A, _2 : B, _3 : C, _4 : D, _5 : E, _6 : F, _7 : G, _8 : H, _9 : I, _10 : J, log : List[Issue] = Nil) extends Box10[A,B,C,D,E,F,G,H,I,J] {
  def isEmpty = false

  def copy(newLog: List[Issue]) = new FullBox10(_1,_2,_3,_4,_5,_6,_7,_8,_9,_10,newLog)
}