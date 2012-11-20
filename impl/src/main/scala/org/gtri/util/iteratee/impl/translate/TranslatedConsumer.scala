//package org.gtri.util.iteratee.impl.translate
//
//import org.gtri.util.iteratee.api.{Iteratee, Enumeratee, Translator, Consumer}
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 2:24 AM
// * To change this template use File | Settings | File Templates.
// */
//case class TranslatedConsumer[A,B](consumer : Consumer[B], translator : Translator[A,B]) extends Consumer[A] {
//  def iteratee = {
//    val temp : Iteratee[B, Consumer.State[B]] = consumer.iteratee
//    val temp2 : Iteratee[A, Consumer.State[A]] = translator.translatee(temp)
//  }
//}
