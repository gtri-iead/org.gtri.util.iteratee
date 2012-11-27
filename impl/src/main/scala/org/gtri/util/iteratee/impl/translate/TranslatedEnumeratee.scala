//package org.gtri.util.iteratee.impl.translate
//
//import org.gtri.util.iteratee.api._
//import org.gtri.util.iteratee.impl.Enumeratees.Result
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/25/12
// * Time: 1:11 AM
// * To change this template use File | Settings | File Templates.
// */
//class TranslatedEnumeratee[A,B](
//  val innerEnumeratee : Enumeratee[A],
//  val translatee: Translatee[A,B]
//) extends Enumeratee[B] {
//
//  def status = innerEnumeratee.status
//
//  override def step() : Result[B] = {
//    val eResult = innerEnumeratee.step
//    val tResult = translatee.apply(eResult.output)
//    Result(new TranslatedEnumeratee(eResult.next, tResult.next), tResult.output, eResult.issues ++ tResult.issues)
//  }
//}
