org.gtri.util.iteratee
======================

A Java interface-front-ended library implemented in Scala for streaming objects between producers and consumers.

<h3>Usage Example (Java)</h3>
The following example enumerates an array of integers to an iteratee that simply prints them.
```Java
IterateeFactory factory = new IterateeFactory();
Array<Integer> ints = { 1,2,3 }
Enumerator<Integer> intProducer = new TestEnumerator<Integer>(ints);
Iteratee<Integer,String> intToString = new TestIntToStringTranslator();
Iteratee<String, Unit> printer = new TestPrintConsumer<String>();
factory.createPlan(integerProducer, intToString, stringConsumer).run();
assertTrue(result.statusCode().isSuccess());
```
<h3>Iteratee Implementation Example (Scala)</h3>
An iteratee implementation that prints all input chunks it receives.
```Scala
import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.Iteratees._
import org.gtri.util.iteratee.impl.Iteratees
import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

class TestPrintConsumer[A] extends Iteratee[A, Unit] {
  case class Cont[A]() extends Iteratees.Cont[A, Unit] {
    def apply(items: ImmutableBuffer[A]) = {
      println("received=" + items)
      for (item <- items) {
        println(item)
      }
      Result(this)
    }

    def endOfInput() = Result(Success())
  }

  def initialState = Cont()
}
```
<h3>Enumeratee Implementation Example (Scala)</h3>
An enumerator implementation for any java.util.Iterator that groups output into chunks
```Scala
import scala.collection.immutable.Traversable
import org.gtri.util.iteratee.api
import api._
import scala.collection.JavaConversions._
import org.gtri.util.iteratee.impl.SeqEnumerator

class TestEnumerator[A](iterable : java.lang.Iterable[A], chunkSize : java.lang.Integer) extends Enumerator[A] {

  def initialState = new SeqEnumerator(iterable.iterator.toStream,chunkSize).initialState
}
```
<h3>Translating Iteratee Implementation Example (Scala)</h3>
An iteratee implementation that translates an integer to a string.
```Scala
import org.gtri.util.iteratee.api._
import org.gtri.util.iteratee.impl.Iteratees._
import org.gtri.util.iteratee.impl.Iteratees
import org.gtri.util.iteratee.impl.ImmutableBuffers.Conversions._

class TestIntToStringTranslator extends Iteratee[java.lang.Integer, String] {
  class Cont extends Iteratees.Cont[java.lang.Integer,String]  {

    def apply(items: ImmutableBuffer[java.lang.Integer]) = {
      println("translating=" + items)
      val nextOutput = items.foldLeft(List[String]()) {
        (list,item) => {
          item.toString :: list
        }
      }
      Result(this, nextOutput)
    }

    def endOfInput() = Result(Success())
  }

  def initialState = new Cont()
}
```
