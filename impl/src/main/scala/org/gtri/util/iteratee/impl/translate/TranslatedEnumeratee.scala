package org.gtri.util.iteratee.impl.translate

import org.gtri.util.iteratee.api._

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/25/12
 * Time: 1:11 AM
 * To change this template use File | Settings | File Templates.
 */
class TranslatedEnumeratee[A,B,S](
  val innerEnumeratee : Enumeratee[A,List[A]],
  val translatee: Translatee[A,B],
  translateeIssues : List[Issue],
  val iteratee : Iteratee[B,S]
) extends Enumeratee[B,S] {

  def status = innerEnumeratee.status

  def issues = translateeIssues ::: innerEnumeratee.issues

  def attach[T](i: Iteratee[B, T]) = new TranslatedEnumeratee[A,B,T](innerEnumeratee,translatee,translateeIssues, i)

  override def step() : TranslatedEnumeratee[A,B,S] = {
    val nextE = innerEnumeratee.step
    val items = nextE.iteratee.state
    val (nextT, output, nextTranslateeIssues) = translatee.apply(items, Nil, translateeIssues)
    val nextI = iteratee(output)
    new TranslatedEnumeratee(nextE, nextT, nextTranslateeIssues, nextI)
  }
}
