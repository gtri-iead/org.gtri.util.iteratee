package org.gtri.util.iteratee.impl

import org.gtri.util.iteratee.api
/**
 * Created with IntelliJ IDEA.
 * User: Lance
 * Date: 11/26/12
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
object Enumeratees {
  case class Result[A](next : api.Enumeratee[A], output : Traversable[A], issues : Traversable[api.Issue] = Nil) extends api.Enumeratee.Result[A]
}
