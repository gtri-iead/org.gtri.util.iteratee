//package org.gtri.util.iteratee.box

/**
* Created with IntelliJ IDEA.
* User: Lance
* Date: 12/16/12
* Time: 10:31 PM
* To change this template use File | Settings | File Templates.
*/
//object Ops {
//  implicit class BoxAnything[A](self: A) {
//    def box: Box[A] = Box(self)
//  }

//  /*
//   * Apply Ops
//   * Syntatic sugar for flatMap, mainly for Boxes of tuples
//   */
//  implicit class ApplyOps[A](self: Box[A]) {
//    def apply[ZZ](f: A => Box[ZZ]) : Box[ZZ] = self flatMap f
//  }
//
//  implicit class ApplyOps2[A,B](self: Box[(A,B)]) {
//    def apply[ZZ](f: (A,B) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps3[A,B,C](self: Box[(A,B,C)]) {
//    def apply[ZZ](f: (A,B,C) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps4[A,B,C,D](self: Box[(A,B,C,D)]) {
//    def apply[ZZ](f: (A,B,C,D) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps5[A,B,C,D,E](self: Box[(A,B,C,D,E)]) {
//    def apply[ZZ](f: (A,B,C,D,E) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps6[A,B,C,D,E,F](self: Box[(A,B,C,D,E,F)]) {
//    def apply[ZZ](f: (A,B,C,D,E,F) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps7[A,B,C,D,E,F,G](self: Box[(A,B,C,D,E,F,G)]) {
//    def apply[ZZ](f: (A,B,C,D,E,F,G) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps8[A,B,C,D,E,F,G,H](self: Box[(A,B,C,D,E,F,G,H)]) {
//    def apply[ZZ](f: (A,B,C,D,E,F,G,H) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps9[A,B,C,D,E,F,G,H,I](self: Box[(A,B,C,D,E,F,G,H,I)]) {
//    def apply[ZZ](f: (A,B,C,D,E,F,G,H,I) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps10[A,B,C,D,E,F,G,H,I,J](self: Box[(A,B,C,D,E,F,G,H,I,J)]) {
//    def apply[ZZ](f: (A,B,C,D,E,F,G,H,I,J) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps11[A,B,C,D,E,F,G,H,I,J,K](self: Box[(A,B,C,D,E,F,G,H,I,J,K)]) {
//    def apply[ZZ](f: (A,B,C,D,E,F,G,H,I,J,K) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//
//  implicit class ApplyOps12[A,B,C,D,E,F,G,H,I,J,K,L](self: Box[(A,B,C,D,E,F,G,H,I,J,K,L)]) {
//    def apply[ZZ](f: (A,B,C,D,E,F,G,H,I,J,K,L) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//  }
//  //  Generate the ApplyOps definition string
//      def makeApplyOps(n: Int) = {
//        require(n < 26, "Ran out of letters!")
//        val types = 'A' until ('A'+n).toChar
//        val varTypes = 'a' until ('a'+n).toChar
//        val boxTypeStrings = (types map { (t) => s"Box[$t]" }).mkString(",")
//        val forGenStrings = ((varTypes zip (1 to n)).map { case (v,n) => s"$v <- self._$n" }).mkString(";")
//        val typeStrings = types.mkString(",")
//        val varTypeStrings = varTypes.mkString(",")
//        s"""  implicit class ApplyOps$n[$typeStrings](self: Box[($typeStrings)]) {
//          |    def apply[ZZ](f: ($typeStrings) => Box[ZZ]) : Box[ZZ] = self flatMap f.tupled
//          | }
//        """.stripMargin
//      }
//  // 2 to 12 foreach (i => println(makeApplyOps(i)))
//
//  /*
//   * Cram Ops
//   * "cram" compresses the number of recover calls on a composite RecoverBox to 1.
//   * val boxZ = for(a <- boxA; b <- boxB; c <- boxC) yield z
//   * If boxA, boxB and boxC are all RecoverBoxes then boxZ.recover will need to be called 3 times to get to the SuccessBox
//   * To avoid this, the following should be used instead:
//   *  val boxZ = (boxA,boxB,boxC).cram { (a,b,c) => Box(z) }
//   * This will combine boxA,boxB and boxC such that if one of them is a RecoverBox only *one* recover is necessary to
//   * recover all inner boxes at once.
//   * "cram", like the for-comprehension (and flatMap/map) also ensures that if any one of the boxes is empty then the
//   * composite box is also empty. For the example above, if boxA, boxB or boxC is empty then boxZ will be empty.
//   */
//
//  implicit class CramOps2[A,B](self: (Box[A],Box[B])) {
//    def cram: Box[(A,B)] = {
//      lazy val allLogs = self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get).box
//      }
//    }
//  }
//
//  implicit class CramOps3[A,B,C](self: (Box[A],Box[B],Box[C])) {
//    def cram: Box[(A,B,C)] = {
//      lazy val allLogs = self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get).box
//      }
//    }
//  }
//
//  implicit class CramOps4[A,B,C,D](self: (Box[A],Box[B],Box[C],Box[D])) {
//    def cram: Box[(A,B,C,D)] = {
//      lazy val allLogs = self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get).box
//      }
//    }
//  }
//
//  implicit class CramOps5[A,B,C,D,E](self: (Box[A],Box[B],Box[C],Box[D],Box[E])) {
//    def cram: Box[(A,B,C,D,E)] = {
//      lazy val allLogs = self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail || self._5.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover || self._5.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover,self._5.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get,self._5.get).box
//      }
//    }
//  }
//
//  implicit class CramOps6[A,B,C,D,E,F](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F])) {
//    def cram: Box[(A,B,C,D,E,F)] = {
//      lazy val allLogs = self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail || self._5.isFail || self._6.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover || self._5.isRecover || self._6.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover,self._5.recover,self._6.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get,self._5.get,self._6.get).box
//      }
//    }
//  }
//
//  implicit class CramOps7[A,B,C,D,E,F,G](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G])) {
//    def cram: Box[(A,B,C,D,E,F,G)] = {
//      lazy val allLogs = self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail || self._5.isFail || self._6.isFail || self._7.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover || self._5.isRecover || self._6.isRecover || self._7.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover,self._5.recover,self._6.recover,self._7.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get,self._5.get,self._6.get,self._7.get).box
//      }
//    }
//  }
//
//  implicit class CramOps8[A,B,C,D,E,F,G,H](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H])) {
//    def cram: Box[(A,B,C,D,E,F,G,H)] = {
//      lazy val allLogs = self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail || self._5.isFail || self._6.isFail || self._7.isFail || self._8.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover || self._5.isRecover || self._6.isRecover || self._7.isRecover || self._8.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover,self._5.recover,self._6.recover,self._7.recover,self._8.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get,self._5.get,self._6.get,self._7.get,self._8.get).box
//      }
//    }
//  }
//
//  implicit class CramOps9[A,B,C,D,E,F,G,H,I](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H],Box[I])) {
//    def cram: Box[(A,B,C,D,E,F,G,H,I)] = {
//      lazy val allLogs = self._9.log ::: self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail || self._5.isFail || self._6.isFail || self._7.isFail || self._8.isFail || self._9.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover || self._5.isRecover || self._6.isRecover || self._7.isRecover || self._8.isRecover || self._9.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover,self._5.recover,self._6.recover,self._7.recover,self._8.recover,self._9.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get,self._5.get,self._6.get,self._7.get,self._8.get,self._9.get).box
//      }
//    }
//  }
//
//  implicit class CramOps10[A,B,C,D,E,F,G,H,I,J](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H],Box[I],Box[J])) {
//    def cram: Box[(A,B,C,D,E,F,G,H,I,J)] = {
//      lazy val allLogs = self._10.log ::: self._9.log ::: self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail || self._5.isFail || self._6.isFail || self._7.isFail || self._8.isFail || self._9.isFail || self._10.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover || self._5.isRecover || self._6.isRecover || self._7.isRecover || self._8.isRecover || self._9.isRecover || self._10.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover,self._5.recover,self._6.recover,self._7.recover,self._8.recover,self._9.recover,self._10.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get,self._5.get,self._6.get,self._7.get,self._8.get,self._9.get,self._10.get).box
//      }
//    }
//  }
//
//  implicit class CramOps11[A,B,C,D,E,F,G,H,I,J,K](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H],Box[I],Box[J],Box[K])) {
//    def cram: Box[(A,B,C,D,E,F,G,H,I,J,K)] = {
//      lazy val allLogs = self._11.log ::: self._10.log ::: self._9.log ::: self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail || self._5.isFail || self._6.isFail || self._7.isFail || self._8.isFail || self._9.isFail || self._10.isFail || self._11.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover || self._5.isRecover || self._6.isRecover || self._7.isRecover || self._8.isRecover || self._9.isRecover || self._10.isRecover || self._11.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover,self._5.recover,self._6.recover,self._7.recover,self._8.recover,self._9.recover,self._10.recover,self._11.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get,self._5.get,self._6.get,self._7.get,self._8.get,self._9.get,self._10.get,self._11.get).box
//      }
//    }
//  }
//
//  implicit class CramOps12[A,B,C,D,E,F,G,H,I,J,K,L](self: (Box[A],Box[B],Box[C],Box[D],Box[E],Box[F],Box[G],Box[H],Box[I],Box[J],Box[K],Box[L])) {
//    def cram: Box[(A,B,C,D,E,F,G,H,I,J,K,L)] = {
//      lazy val allLogs = self._12.log ::: self._11.log ::: self._10.log ::: self._9.log ::: self._8.log ::: self._7.log ::: self._6.log ::: self._5.log ::: self._4.log ::: self._3.log ::: self._2.log ::: self._1.log
//      if(self._1.isFail || self._2.isFail || self._3.isFail || self._4.isFail || self._5.isFail || self._6.isFail || self._7.isFail || self._8.isFail || self._9.isFail || self._10.isFail || self._11.isFail || self._12.isFail) {
//        new FailBox(allLogs)
//      } else if(self._1.isRecover || self._2.isRecover || self._3.isRecover || self._4.isRecover || self._5.isRecover || self._6.isRecover || self._7.isRecover || self._8.isRecover || self._9.isRecover || self._10.isRecover || self._11.isRecover || self._12.isRecover) {
//        new RecoverBox(
//          Recoverable({ (self._1.recover,self._2.recover,self._3.recover,self._4.recover,self._5.recover,self._6.recover,self._7.recover,self._8.recover,self._9.recover,self._10.recover,self._11.recover,self._12.recover).cram }),
//          allLogs
//        )
//      } else {
//        (self._1.get,self._2.get,self._3.get,self._4.get,self._5.get,self._6.get,self._7.get,self._8.get,self._9.get,self._10.get,self._11.get,self._12.get).box
//      }
//    }
//  }
//
//  // Generate the CramOps definition string
//    def makeCramOps(n: Int) = {
//      require(n < 26, "Ran out of letters!")
//      val types = 'A' until ('A'+n).toChar
//      val varTypes = 'a' until ('a'+n).toChar
//      val boxTypeStrings = (types map { (t) => s"Box[$t]" }).mkString(",")
//      val recoverStrings = (1 to n).map({ n => s"self._$n.recover" }).mkString(",")
//      val getStrings = (1 to n).map({ n => s"self._$n.get" }).mkString(",")
//    //      val forGenStrings = ((varTypes zip (1 to n)).map { case (v,n) => s"$v <- self._$n" }).mkString(";")
//      val typeStrings = types.mkString(",")
////      val varTypeStrings = varTypes.mkString(",")
//      val logAccStrings = ((n to 1 by -1) map { i => s"self._$i.log"}).mkString(" ::: ")
//      val ifAnyRecoverableStrings = ((1 to n) map { i => s"self._$i.isRecover"}).mkString(" || ")
//      val ifAnyEmptyStrings = ((1 to n) map { i => s"self._$i.isFail"}).mkString(" || ")
//    s"""  implicit class CramOps$n[$typeStrings](self: ($boxTypeStrings)) {
//        |    def cram: Box[($typeStrings)] = {
//        |      lazy val allLogs = $logAccStrings
//        |      if($ifAnyEmptyStrings) {
//        |        new EmptyBox(allLogs)
//        |      } else if($ifAnyRecoverableStrings) {
//        |        new RecoverBox(
//        |          Recoverable({ ($recoverStrings).cram }),
//        |          allLogs
//        |        )
//        |      } else {
//        |        ($getStrings).box
//        |      }
//        |    }
//        | }
//      """.stripMargin
//  }
//  // 2 to 12 foreach (i => println(makeCramOps(i)))
//}
