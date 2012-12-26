//package org.gtri.util.iteratee.box

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/24/12
 * Time: 4:37 AM
 * To change this template use File | Settings | File Templates.
 */
//object Box {
//  import Try3._
//
//  type LogWriter[+A] = Writer[List[Issue], Option[A]]
//  case object LogWriter {
//    def empty[A]: LogWriter[A] = empty[A](Nil)
//    def empty[A](issue: Issue): LogWriter[A] = empty[A](List(issue))
//    def empty[A](issues: List[Issue]): LogWriter[A] = Writer(issues, None)
//    def apply[A](a: A): LogWriter[A] = apply[A](a, Nil)
//    def apply[A](a: A, issue: Issue): LogWriter[A] = apply[A](a, List(issue))
//    def apply[A](a: A, issues: List[Issue]): LogWriter[A] = Writer(issues, Some(a))
//    //    def unapply[A](writer : Writer[List[Issue], Option[A]]) : Option[(Option[A], List[Issue])] = {
//    //      Some(writer.value, writer.written)
//    //    }
//  }
//
//  //  type Box[+A] = Try2[Writer[List[Issue],Option[A]]] // Doesn't work b/c recover state has no log
//  //  type Box2[+A] = Try3[Writer[List[Issue],A]] // Doesn't work b/c empty state & recover state has no log
//  //  type Box3[+A] = Writer[List[Issue], Try3[A]] // Doesn't work b/c recover can't return a log in addition to A
//  //  type Box4[+A] = Writer[List[Issue], Try3[Writer[List[Issue], A]]] // Doesn't work b/c there are now two logs
//  type Box[+A] = Try3[Writer[List[Issue],A],List[Issue]]
//  type Box[+A] = Writer[List[Issue],Try3[A, List[Issue]]]
//
//  def empty[A]: Box[A] = empty[A](Nil)
//  def empty[A](issue: Issue): Box[A] = empty[A](List(issue))
//  def empty[A](issues: List[Issue]): Box[A] = NoGo(issues)
//  def recover[A](recoverable : => Box[A]) : Box[A] = recover[A](recoverable, Nil)
//  def recover[A](recoverable : => Box[A], issue : Issue) : Box[A] = recover[A](recoverable, List(issue))
//  def recover[A](recoverable : => Box[A], issues : List[Issue]) : Box[A] = Recover(issues,recoverable)
//  def apply[A](a: A): Box[A] = apply[A](a, Nil)
//  def apply[A](a: A, issue: Issue): Box[A] = apply[A](a, List(issue))
//  def apply[A](a: A, issues: List[Issue]): Box[A] = Go(Writer(issues, a))
//
//  case object EmptyBox {
//    def apply[A]: Box[A] = apply[A](Nil)
//    def apply[A](issue: Issue): Box[A] = apply[A](List(issue))
//    def apply[A](issues: List[Issue]): Box[A] = NoGo(issues)
//
//    def unapply[A](box : Box[A]) : Option[List[Issue]] = {
//      if(box.isNoGo) {
//        Some(box.err)
//      } else {
//        None
//      }
//    }
//  }
//
//  case object FullBox {
//    def apply[A](a: A): Box[A] = apply[A](a, Nil)
//    def apply[A](a: A, issue: Issue): Box[A] = apply[A](a, List(issue))
//    def apply[A](a: A, issues: List[Issue]): Box[A] = Go(Writer(issues, a))
//    def unapply[A](box : Box[A]) : Option[(A, List[Issue])] = {
//      if(box.isGo) {
//        Some((box.get.value, box.get.written))
//      } else {
//        None
//      }
//    }
//  }
//
//  case object RecoverBox {
//    def apply[A](recoverable : => Box[A]) : Box[A] = apply[A](recoverable, Nil)
//    def apply[A](recoverable : => Box[A], issue : Issue) : Box[A] = apply[A](recoverable, List(issue))
//    def apply[A](recoverable : => Box[A], issues : List[Issue]) : Box[A] = Recover(issues,recoverable)
//    def unapply[A](box : Box[A]) : Option[(Lazy[Box[A]],List[Issue])] = {
//      if(box.isRecover) {
//        Some((box.recoverable, box.err))
//      } else {
//        None
//      }
//    }
//  }
//
//  implicit class boxAllTheThings[A](a : A) {
//    def box : Box[A] = Box(a)
//  }
//
//  def examples {
//    //    import org.gtri.util.iteratee.impl.box.Ops._
//    import org.gtri.util.iteratee.api.Issue
//    import org.gtri.util.iteratee.api.Issues._
//
//    val issue2 = log("asdf2",java.util.logging.Level.INFO)
//    val issue4 = log("asdf4",java.util.logging.Level.INFO)
//    val issue5 = log("asdf5",java.util.logging.Level.INFO)
//    val issue6 = log("asdf6",java.util.logging.Level.INFO)
//    val issue10 = log("asdf10",java.util.logging.Level.INFO)
//    val issue11 = log("asdf11",java.util.logging.Level.INFO)
//
//    val b1 : Box[Int] = 1.box // syntatic sugar
//    val b2 : Box[Int] = Box(2, issue2)
//    val b4 = Box.empty[Int](issue4)
//    val b6 : Box[String] = Box("asdf", issue6)
//
//    val b10 : Box[String] = Box.recover({println("HERE!"); FullBox("qwerty", issue11)},issue10) // build a box that represents a failed operation that can be recovered
//
//    b2 match { // open a box to see if there is anything there
//      case FullBox(item, log) =>
//        println(item)
//        log foreach { println(_) }
//      case RecoverBox(recover, log) =>
//        log foreach { println(_) }
//      case EmptyBox(log) =>
//        log foreach { println(_) }
//    }
//    //    val b3 = b1 fold b2 // replace contents of b2 with contents of b1 (since they are both full) and append their logs
//    //    val b5 : Box[Int] = b4 >> b1 // doesn't replace b1 since b4 is empty, but logs are still appended
//    //    val b7 : Box[(Int,String)] = (b1,b6).cram // cram the contents of two boxes together (box is only full if b1 and b6 are full also appends logs)
//    //    val b8 : Box[(Int,String,Int)] = (b1,b6,b2).cram // cram more stuff into our boxes
//    //    val b9 : Box[String] = b8 { // apply a function to the crammed box b8
//    //        (a : Int,b : String,c : Int) => { // function is only called if b8 is full
//    //          Box("asdf") // Box up a result - logs will be appended
//    //        }
//    //      } // b9 now has "asdf" and a concat of logs from b1,b6,b2 and itself
//
//    //    val b11 : Box[Int] =
//    //      (b1,b2,b10).cram // b/c b10 is a RecoverBox cram will return a RecoverBox that can be recovered to recover from the failed operation
//    //      {  // This is called using ApplyOps - syntatic sugar for flatMap
//    //        (a : Int,b : Int,s : String) =>
//    //          println(a+b);Box(a+b)
//    //      } // This whole operation will not be resolved immediately, instead b11 will be a RecoverBox that can be recovered to force the whole chain to recover
//    //    val b12 : Box[Int] = b11.recover // using cram means only recover call is necessary
//
//    //    val b13 : Box[Int] = for(a <- b1;b <- b2) yield a + b // SuccessBox(3,List(issue2))
//    //    val b14 : Box[Int] = for(a <- b1;b <- b2; c <- b4) yield a + b + c // EmptyBox(List(issue2,issue4))
//    //    val b15 : Box[String] = for(a <- b1;b <- b10; c <- b10) yield a.toString + b + c // RecoverBox(recoverable, List(issue2,issue4))
//    //    val b16 : Box[String] = b15.recover.recover // Recover must be called twice b/c b15 uses b10 twice (this is why cram should be used instead)
//  }
//}
