package org.gtri.util.box

import org.gtri.util.iteratee.api.IssueHandlingCode

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/16/12
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
object Ops {
  import scala.language.implicitConversions

  implicit class BoxAnything[A,B](self: A) {
    def box: Box[A] = Box(self)
  }

  /*
   * Apply Ops
   * Syntatic sugar for flatMap, mainly for Boxes of tuples
   */
  implicit class ApplyOps[A](self: Box[A]) {
    def apply[ZZ](f: A => Box[ZZ]) : Box[ZZ] = self flatMap f
  }

  implicit class ApplyOps2[A,B](self: Box[(A,B)]) {
    def apply[ZZ](f: (A,B) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps3[A,B,C](self: Box[(A,B,C)]) {
    def apply[ZZ](f: (A,B,C) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps4[A,B,C,D](self: Box[(A,B,C,D)]) {
    def apply[ZZ](f: (A,B,C,D) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps5[A,B,C,D,E](self: Box[(A,B,C,D,E)]) {
    def apply[ZZ](f: (A,B,C,D,E) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps6[A,B,C,D,E,F](self: Box[(A,B,C,D,E,F)]) {
    def apply[ZZ](f: (A,B,C,D,E,F) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps7[A,B,C,D,E,F,G](self: Box[(A,B,C,D,E,F,G)]) {
    def apply[ZZ](f: (A,B,C,D,E,F,G) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps8[A,B,C,D,E,F,G,H](self: Box[(A,B,C,D,E,F,G,H)]) {
    def apply[ZZ](f: (A,B,C,D,E,F,G,H) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps9[A,B,C,D,E,F,G,H,I](self: Box[(A,B,C,D,E,F,G,H,I)]) {
    def apply[ZZ](f: (A,B,C,D,E,F,G,H,I) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps10[A,B,C,D,E,F,G,H,I,J](self: Box[(A,B,C,D,E,F,G,H,I,J)]) {
    def apply[ZZ](f: (A,B,C,D,E,F,G,H,I,J) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps11[A,B,C,D,E,F,G,H,I,J,K](self: Box[(A,B,C,D,E,F,G,H,I,J,K)]) {
    def apply[ZZ](f: (A,B,C,D,E,F,G,H,I,J,K) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }

  implicit class ApplyOps12[A,B,C,D,E,F,G,H,I,J,K,L](self: Box[(A,B,C,D,E,F,G,H,I,J,K,L)]) {
    def apply[ZZ](f: (A,B,C,D,E,F,G,H,I,J,K,L) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
  }
  //  Generate the ApplyOps definition string
      def makeApplyOps(n: Int) = {
        require(n < 26, "Ran out of letters!")
        val types = 'A' until ('A'+n).toChar
        val varTypes = 'a' until ('a'+n).toChar
        val boxTypeStrings = (types map { (t) => s"Box[$t]" }).mkString(",")
        val forGenStrings = ((varTypes zip (1 to n)).map { case (v,n) => s"$v <- self._$n" }).mkString(";")
        val typeStrings = types.mkString(",")
        val varTypeStrings = varTypes.mkString(",")
        s"""  implicit class ApplyOps$n[$typeStrings](self: Box[($typeStrings)]) {
          |    def apply[ZZ](f: ($typeStrings) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
          | }
        """.stripMargin
      }
  // 2 to 12 foreach (i => println(makeApplyOps(i)))

  /*
   * Cram Ops
   * "cram" compresses the number of recover calls on a composite RecoverBox to 1.
   * val boxZ = for(a <- boxA; b <- boxB; c <- boxC) yield z
   * If boxA, boxB and boxC are all RecoverBoxes then boxZ.recover will need to be called 3 times to get to the SuccessBox
   * To avoid this, the following should be used instead:
   *  val boxZ = (boxA,boxB,boxC).cram { (a,b,c) => Box(z) }
   * This will combine boxA,boxB and boxC such that if one of them is a RecoverBox only *one* recover is necessary to
   * recover all inner boxes at once.
   * "cram", like the for-comprehension (and flatMap/map) also ensures that if any one of the boxes is empty then the
   * composite box is also empty. For the example above, if boxA, boxB or boxC is empty then boxZ will be empty.
   */
  implicit class CramOps2[A,B](self: (Box[A],Box[B])) {
    def cram: Box[(A,B)] = {
      if(self._1.isRecoverable || self._2.isRecoverable) {
        new RecoverBox[(A,B)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover) yield(a,b) }),
          self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2) yield(a,b)
      }
    }
  }

  implicit class CramOps3[A,B,C](self: (Box[A],Box[B],Box[C])) {
    def cram: Box[(A,B,C)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable) {
        new RecoverBox[(A,B,C)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover) yield(a,b,c) }),
          self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3) yield(a,b,c)
      }
    }
  }

  implicit class CramOps4[A,B,C,D](self: (Box[A],Box[B],Box[C],Box[D])) {
    def cram: Box[(A,B,C,D)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable) {
        new RecoverBox[(A,B,C,D)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover) yield(a,b,c,d) }),
          self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4) yield(a,b,c,d)
      }
    }
  }

  implicit class CramOps5[A,B,C,D,E](self: (Box[A],Box[B],Box[C],Box[D],Box[E])) {
    def cram: Box[(A,B,C,D,E)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable || self._5.isRecoverable) {
        new RecoverBox[(A,B,C,D,E)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover;e <- self._5.recover) yield(a,b,c,d,e) }),
          self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4;e <- self._5) yield(a,b,c,d,e)
      }
    }
  }

  implicit class CramOps6[A,B,C,D,E,F](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F])) {
    def cram: Box[(A,B,C,D,E,F)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable || self._5.isRecoverable || self._6.isRecoverable) {
        new RecoverBox[(A,B,C,D,E,F)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover;e <- self._5.recover;f <- self._6.recover) yield(a,b,c,d,e,f) }),
          self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4;e <- self._5;f <- self._6) yield(a,b,c,d,e,f)
      }
    }
  }

  implicit class CramOps7[A,B,C,D,E,F,G](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G])) {
    def cram: Box[(A,B,C,D,E,F,G)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable || self._5.isRecoverable || self._6.isRecoverable || self._7.isRecoverable) {
        new RecoverBox[(A,B,C,D,E,F,G)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover;e <- self._5.recover;f <- self._6.recover;g <- self._7.recover) yield(a,b,c,d,e,f,g) }),
          self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4;e <- self._5;f <- self._6;g <- self._7) yield(a,b,c,d,e,f,g)
      }
    }
  }

  implicit class CramOps8[A,B,C,D,E,F,G,H](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H])) {
    def cram: Box[(A,B,C,D,E,F,G,H)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable || self._5.isRecoverable || self._6.isRecoverable || self._7.isRecoverable || self._8.isRecoverable) {
        new RecoverBox[(A,B,C,D,E,F,G,H)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover;e <- self._5.recover;f <- self._6.recover;g <- self._7.recover;h <- self._8.recover) yield(a,b,c,d,e,f,g,h) }),
          self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4;e <- self._5;f <- self._6;g <- self._7;h <- self._8) yield(a,b,c,d,e,f,g,h)
      }
    }
  }

  implicit class CramOps9[A,B,C,D,E,F,G,H,I](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H],Box[I])) {
    def cram: Box[(A,B,C,D,E,F,G,H,I)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable || self._5.isRecoverable || self._6.isRecoverable || self._7.isRecoverable || self._8.isRecoverable || self._9.isRecoverable) {
        new RecoverBox[(A,B,C,D,E,F,G,H,I)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover;e <- self._5.recover;f <- self._6.recover;g <- self._7.recover;h <- self._8.recover;i <- self._9.recover) yield(a,b,c,d,e,f,g,h,i) }),
          self._9.log ::: self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4;e <- self._5;f <- self._6;g <- self._7;h <- self._8;i <- self._9) yield(a,b,c,d,e,f,g,h,i)
      }
    }
  }

  implicit class CramOps10[A,B,C,D,E,F,G,H,I,J](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H],Box[I],Box[J])) {
    def cram: Box[(A,B,C,D,E,F,G,H,I,J)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable || self._5.isRecoverable || self._6.isRecoverable || self._7.isRecoverable || self._8.isRecoverable || self._9.isRecoverable || self._10.isRecoverable) {
        new RecoverBox[(A,B,C,D,E,F,G,H,I,J)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover;e <- self._5.recover;f <- self._6.recover;g <- self._7.recover;h <- self._8.recover;i <- self._9.recover;j <- self._10.recover) yield(a,b,c,d,e,f,g,h,i,j) }),
          self._10.log ::: self._9.log ::: self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4;e <- self._5;f <- self._6;g <- self._7;h <- self._8;i <- self._9;j <- self._10) yield(a,b,c,d,e,f,g,h,i,j)
      }
    }
  }

  implicit class CramOps11[A,B,C,D,E,F,G,H,I,J,K](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H],Box[I],Box[J],Box[K])) {
    def cram: Box[(A,B,C,D,E,F,G,H,I,J,K)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable || self._5.isRecoverable || self._6.isRecoverable || self._7.isRecoverable || self._8.isRecoverable || self._9.isRecoverable || self._10.isRecoverable || self._11.isRecoverable) {
        new RecoverBox[(A,B,C,D,E,F,G,H,I,J,K)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover;e <- self._5.recover;f <- self._6.recover;g <- self._7.recover;h <- self._8.recover;i <- self._9.recover;j <- self._10.recover;k <- self._11.recover) yield(a,b,c,d,e,f,g,h,i,j,k) }),
          self._11.log ::: self._10.log ::: self._9.log ::: self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4;e <- self._5;f <- self._6;g <- self._7;h <- self._8;i <- self._9;j <- self._10;k <- self._11) yield(a,b,c,d,e,f,g,h,i,j,k)
      }
    }
  }

  implicit class CramOps12[A,B,C,D,E,F,G,H,I,J,K,L](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H],Box[I],Box[J],Box[K],Box[L])) {
    def cram: Box[(A,B,C,D,E,F,G,H,I,J,K,L)] = {
      if(self._1.isRecoverable || self._2.isRecoverable || self._3.isRecoverable || self._4.isRecoverable || self._5.isRecoverable || self._6.isRecoverable || self._7.isRecoverable || self._8.isRecoverable || self._9.isRecoverable || self._10.isRecoverable || self._11.isRecoverable || self._12.isRecoverable) {
        new RecoverBox[(A,B,C,D,E,F,G,H,I,J,K,L)](
          Recoverable({ for(a <- self._1.recover;b <- self._2.recover;c <- self._3.recover;d <- self._4.recover;e <- self._5.recover;f <- self._6.recover;g <- self._7.recover;h <- self._8.recover;i <- self._9.recover;j <- self._10.recover;k <- self._11.recover;l <- self._12.recover) yield(a,b,c,d,e,f,g,h,i,j,k,l) }),
          self._12.log ::: self._11.log ::: self._10.log ::: self._9.log ::: self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
        )
      } else {
        for(a <- self._1;b <- self._2;c <- self._3;d <- self._4;e <- self._5;f <- self._6;g <- self._7;h <- self._8;i <- self._9;j <- self._10;k <- self._11;l <- self._12) yield(a,b,c,d,e,f,g,h,i,j,k,l)
      }
    }
  }

  // Generate the CramOps definition string
    def makeCramOps(n: Int) = {
      require(n < 26, "Ran out of letters!")
      val types = 'A' until ('A'+n).toChar
      val varTypes = 'a' until ('a'+n).toChar
      val boxTypeStrings = (types map { (t) => s"Box[$t]" }).mkString(",")
      val forGenRecoverStrings = ((varTypes zip (1 to n)).map { case (v,n) => s"$v <- self._$n.recover" }).mkString(";")
      val forGenStrings = ((varTypes zip (1 to n)).map { case (v,n) => s"$v <- self._$n" }).mkString(";")
      val typeStrings = types.mkString(",")
      val varTypeStrings = varTypes.mkString(",")
      val logAccStrings = ((n to 1 by -1) map { i => s"self._$i.log"}).mkString(" ::: ")
      val ifRecoverableStrings = ((1 to n) map { i => s"self._$i.isRecoverable"}).mkString(" || ")
      s"""  implicit class CramOps$n[$typeStrings](self: ($boxTypeStrings)) {
        |    def cram: Box[($typeStrings)] = {
        |      if($ifRecoverableStrings) {
        |        new RecoverBox[($typeStrings)](
        |          Recoverable({ for($forGenRecoverStrings) yield($varTypeStrings) }),
        |          $logAccStrings
        |        )
        |      } else {
        |        for($forGenStrings) yield($varTypeStrings)
        |      }
        |    }
        | }
      """.stripMargin
    }
  // 2 to 12 foreach (i => println(makeCramOps(i)))
}
