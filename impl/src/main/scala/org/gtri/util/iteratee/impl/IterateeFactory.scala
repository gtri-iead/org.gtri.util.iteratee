//package org.gtri.util.iteratee.impl
//
//import base.BaseTranslatee
//import org.gtri.util.iteratee.api
//import api.Builder.State
//import org.gtri.util.iteratee.api.{ErrorHandlingCode, Enumeratee, Iteratee, Translator, Producer, Consumer, Builder}
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/21/12
// * Time: 5:58 AM
// * To change this template use File | Settings | File Templates.
// */
//class IterateeFactory(val errorHandlingCode : ErrorHandlingCode = ErrorHandlingCode.NORMAL) extends api.IterateeFactory {
//
//  def createPlanner() = new Planner(this)
//
//  def createProducer[A, S](e: Enumeratee[A, S]) = new Producer[A] {
//    def enumeratee[S](i: Iteratee[A, S]) = e.attach(i)
//  }
//
//  def createConsumer[A,S](i: Iteratee[A, S]) = new Consumer[A,S] {
//    def iteratee = i
//  }
//
//  def createBuilder[A, V](i: Iteratee[A, State[V]]) = new Builder[A,V] {
//    def iteratee = i
//  }
//
//  def createTranslator[A, B](f: (A) => B) = new Translator[A,B] {
//    def translatee[S](i: Iteratee[B, S]) = new TranslateeF(i, f)
//  }
//
//}
