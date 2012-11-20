//package org.gtri.util.iteratee.impl.util
//
//import org.gtri.util.iteratee.api.{Consumer, Issue, Producer, Builder}
//
///**
// * Created with IntelliJ IDEA.
// * User: Lance
// * Date: 11/19/12
// * Time: 2:11 AM
// * To change this template use File | Settings | File Templates.
// */
//case class BuilderResult[A,V](
//  producer : Producer[A],
//  builder : Builder[A,V],
//  isSuccess : Boolean = false,
//  value : Option[V] = None,
//  issues : List[Issue] = Nil,
//  overflow : List[A] = Nil
//) extends Builder.Result[A,V]
//
//case class ConsumerResult[A](
//  producer : Producer[A] = EmptyProducer[A],
//  consumer : Consumer[A] = EmptyConsumer[A],
//  isSuccess : Boolean = false,
//  issues : List[Issue] = Nil,
//  overflow : List[A] = Nil
//) extends Consumer.Result[A]
//
//case class EmptyBuilderResult[A,V]() extends BuilderResult[A,V](EmptyProducer[A],EmptyBuilder[A,V])
//case class EmptyConsumerResult[A]() extends ConsumerResult[A](EmptyProducer[A],EmptyConsumer[A])
