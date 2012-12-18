package org.gtri.util.box

/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 12/14/12
 * Time: 3:13 AM
 * To change this template use File | Settings | File Templates.
 */
trait LogWriter[A, +MDT] {
  def copy(newLog : List[A]) : MDT
  def log : List[A]

  def :+(item : A) = append(item)
  def ++(item : A) = append(item)
  def append(item : A) = copy(item :: log)

  def :+(moreItems : List[A]) = append(moreItems)
  def ++(moreItems : List[A]) = append(moreItems)
  def append(moreItems : List[A]) = copy(moreItems ::: log)

  def +:(item : A) = prepend(item)
  def prepend(item : A) = copy(log ::: List(item))

  def +:(moreItems : List[A]) = prepend(moreItems)
  def prepend(moreItems: List[A]) = copy(log ::: moreItems)
}