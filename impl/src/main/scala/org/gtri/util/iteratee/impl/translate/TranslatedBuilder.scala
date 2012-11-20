//package org.gtri.util.iteratee.impl.translate
//
//import org.gtri.util.iteratee.api.{Iteratee, Enumeratee, Translator, Builder}
//
///**
//* Created with IntelliJ IDEA.
//* User: Lance
//* Date: 11/19/12
//* Time: 2:24 AM
//* To change this template use File | Settings | File Templates.
//*/
//case class TranslatedBuilder[A,B,V](builder : Builder[B,V], translator : Translator[A,B]) extends Builder[A,V] {
//
//  def iteratee : Iteratee[A, Builder.State[A,V]] = {
//    val temp : Iteratee[A, Builder.State[B,V]] = translator.translate(builder.iteratee)
//    temp
//  }
//}
