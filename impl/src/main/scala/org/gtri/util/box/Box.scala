package org.gtri.util.box

import org.gtri.util.iteratee.api.{IssueHandlingCode, Issue}
import org.gtri.util.iteratee.api.Issue.ImpactCode

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/16/12
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
object Box {

  def empty[A] : Box[A] = empty[A](Nil)
  def empty[A](issue : Issue) : Box[A] = new EmptyBox[A](List(issue))
  def empty[A](log : List[Issue]) : Box[A] = new EmptyBox[A](log)
  def apply[A](a : A) : Box[A] = apply[A](a, Nil)
  def apply[A](a : A, issue : Issue) : Box[A] = apply[A](a, List(issue))
  def apply[A](a : A, log : List[Issue]) : Box[A] = new SuccessBox[A](a, log)
  def recover[A](recoverBox: => Box[A]) : Box[A] = recover[A](recoverBox, Nil)
  def recover[A](recoverBox: => Box[A], issue : Issue) : Box[A] = recover[A](recoverBox, List(issue))
  def recover[A](recoverBox: => Box[A], log : List[Issue]) : Box[A] = new RecoverBox[A](Recoverable(recoverBox), log)
  def option[A](opt : Option[A]) : Box[A] = option[A](opt, Nil)
  def option[A](opt : Option[A], issue : Issue) : Box[A] = option[A](opt, List(issue))
  def option[A](opt : Option[A], log : List[Issue]) = {
    if(opt.isDefined) {
      new SuccessBox[A](opt.get, log)
    } else {
      new EmptyBox[A](log)
    }
  }

  def examples {
    import Ops._

    val b1 : Box[Int] = 1.box
    val b2 : Box[Int] = Box(2)
    b2 match { // open a box to see if there is anything there
      case SuccessBox(item, log) =>
        println(item)
        log foreach { println(_) }
      case RecoverBox(recover, log) =>
        log foreach { println(_) }
      case EmptyBox(log) =>
        log foreach { println(_) }
    }
    val b3 = b1 fold b2 // replace contents of b1 with contents of b2 (since they are both full) and append their logs
    val b4 = Box.empty[Int]
    val b5 : Box[Int] = b1 >> b4 // doesn't replace b1 since b4 is empty, but logs are still appended
    val b6 : Box[String] = Box("asdf")
    val b7 : Box[(Int,String)] = (b1,b6).cram // cram the contents of two boxes together (box is only full if b1 and b6 are full also appends logs)
    val b8 : Box[(Int,String,Int)] = (b1,b6,b2).cram // cram more stuff into our boxes
    val b9 : Box[String] = b8 { // apply a function to the crammed box b8
        (a : Int,b : String,c : Int) => { // function is only called if b8 is full
          Box("asdf") // Box up a result - logs will be appended
        }
      } // b9 now has "asdf" and a concat of logs from b1,b6,b2 and itself
  }
}

sealed trait Box[+A] extends LogWriter[Issue, Box[A]] { self =>

  // Override in inherited classes
  def fold[X](ifEmpty: => X, ifRecover: Recoverable[A] => X, ifSuccess: A => X) : X
  def isEmpty : Boolean = false
  def isRecoverable : Boolean = false
  def isSuccess : Boolean = false
  def get : A = throw new NoSuchElementException
  def recoverable : Recoverable[A] = throw new IllegalStateException

  def nonEmpty = isEmpty == false
  def isFailure = isSuccess == false

  /**
   * Try to recover from a failure. If box is not recoverable returns empty or success with no logs.
   * @return a new Box with the result of the attempt
   */
  def recover : Box[A] = fold(
    ifEmpty = { Box.empty[A] },
    ifRecover = { _.recover },
    ifSuccess = { Box(_) }
  )

  /**
   * Convert to an option. Success maps to Some, recoverable and empty maps to None
   * @return an option
  */
  def toOption : Option[A] = fold(
    ifEmpty = { None },
    ifRecover = { _ => None },
    ifSuccess = { a => Some(a) }
  )

  /**
   * Fold this into that, such that this overwrites that when both are success or recoverable. Logs are always appended.
   * @param that
   * @tparam AA
   * @return this folded into that
   */
  def >>[AA >: A](that : Box[AA]) = fold(that)
  /**
   * Fold this into that, such that this overwrites that when both are success or recoverable. Logs are always appended.
   * @param that
   * @tparam AA
   * @return this folded into that
   */
  def fold[AA >: A](that : Box[AA]) : Box[AA] = {
    fold(
      ifEmpty = that.fold(
        ifEmpty = { that.append(this.log) },
        ifRecover = { _ => that.append(this.log) },
        ifSuccess = { _ => that.append(this.log) }
      ),
      ifRecover = {
        _ => that.fold(
          ifEmpty = { this.prepend(that.log) },
          ifRecover = { _ => this.prepend(that.log) },
          ifSuccess = { _ => that.append(this.log) }
        )
      },
      ifSuccess = {
        _ => that.fold(
          ifEmpty = { this.prepend(that.log) },
          ifRecover = { _ => this.prepend(that.log) },
          ifSuccess = { _ => this.prepend(that.log) }
        )
      }
    )
  }

  /**
   * Flatmap that short-circuits on empty box. On recover box, returns a new RecoverBox that can be recovered to proceed
   * with the flatMap.
   * @param f
   * @tparam B
   * @return
   */
  def flatMap[B](f: A => Box[B]) : Box[B] = {
    fold(
      ifEmpty = new EmptyBox[B](log),
      ifRecover = { createFlatMapRecoverableBox(_,f,log) },
      ifSuccess = { f(_).prepend(log) }
    )
  }

  /**
   * Map that short-circuits on empty box. On recover box, returns a new RecoverBox that can be recovered to proceed
   * with the map.
   * @param f
   * @tparam B
   * @return
   */
  def map[B](f: A => B) : Box[B] = {
    fold(
      ifEmpty = new EmptyBox[B](log),
      ifRecover = { createFlatMapRecoverableBox(_,{ (a : A) => new SuccessBox(f(a)) },log) }, // error here with { new SuccessBox(f(_)) } ???
      ifSuccess = { a => new SuccessBox(f(a),log) } // error here with { new SuccessBox(f(_),log) } ???
    )
  }

  private def createFlatMapRecoverableBox[A,B](recoverable: Recoverable[A], f: A => Box[B], log : List[Issue]) : Box[B] = {
    new RecoverBox(
      Recoverable({
        // Try to recover
        val boxA = recoverable.recover
        boxA.fold(
          // If empty return an empty box
          ifEmpty = { new EmptyBox[B](boxA.log) },
          // If recover return this with updated logs
          ifRecover = { _ => createFlatMapRecoverableBox(recoverable, f, boxA.log) },
          // If success return the result of the map with updated logs
          ifSuccess = { f(_).prepend(boxA.log) }
        )
      })
      ,log
    )
  }

  /**
   * Filter success box only. Recoverable or empty boxes do not invoke p
   * @param p
   * @return
   */
  def withFilter(p: A => Boolean): Box[A] = {
    // TODO: Make ifRecover return a RecoverBox
    fold(
      ifEmpty = this,
      ifRecover = { _ => Box.empty[A](log) },
      ifSuccess = { a => if(p(a)) this else Box.empty[A](log) }
    )
  }

  /**
   * Foreach for success box only. Recoverable or empty boxes do not invoke f
   * @param f
   * @tparam U
   */
  def foreach[U](f: A => U) {
    fold(
      ifEmpty = { () },
      ifRecover = { _ => () },
      ifSuccess = { a => f(a) }
    )
  }
}

final case class EmptyBox[+A](log : List[Issue] = Nil) extends Box[A] {
  override def isEmpty : Boolean = true

  def copy(newLog: List[Issue]) = new EmptyBox(newLog)

  def fold[X](ifEmpty: => X, ifRecover: Recoverable[A] => X, ifSuccess: A => X) : X = ifEmpty
}

final case class RecoverBox[+A](override val recoverable : Recoverable[A], log : List[Issue] = Nil) extends Box[A] {
  override def isRecoverable : Boolean = true

  def copy(newLog: List[Issue]) = new RecoverBox(recoverable, newLog)

  def fold[X](ifEmpty: => X, ifRecover: Recoverable[A] => X, ifSuccess: A => X) : X = ifRecover(recoverable)
}

final case class SuccessBox[+A] (override val get : A, log : List[Issue] = Nil) extends Box[A] {
  override def isSuccess : Boolean = true

  def copy(newLog: List[Issue]) = new SuccessBox[A](get, newLog)

  def fold[X](ifEmpty: => X, ifRecover: Recoverable[A] => X, ifSuccess: A => X) : X = ifSuccess(get)
}