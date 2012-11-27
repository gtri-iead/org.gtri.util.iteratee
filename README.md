org.gtri.util.iteratee
======================

A Java interface-front-ended library implemented in Scala for streaming objects between producers and consumers.

<h3>Usage Example (Java)</h3>
The following example enumerates an array of integers to an iteratee that simply prints them.
```Java
IterateeFactory factory = new IterateeFactory();
Planner planner = factory.createPlanner();
Array<Integer> ints = { 1,2,3 }
Producer<Integer> intProducer = new TestProducer(ints);
Translator<Integer,String> intToString = new TestIntToStringTranslator();
Consumer<String> strConsumer = new TestPrintConsumer<String>(output);
planner.connect(integerProducer, intToString, stringConsumer).run();
assertTrue(result.status().isSuccess());
```
<h3>Iteratee Implementation Example (Scala)</h3>
```Scala
class TestPrintConsumer[A] extends Consumer[A, Unit] {
  case class Cont[A]() extends IterS.Cont[A] {
    def apply(items: Traversable[A]) = {
      for (item <- items) {
        println(item)
      }
      Result(this)
    }

    def endOfInput() = Result(IterS.Success())
  }

  def iteratee = Cont()
}
```
<h3>Enumeratee Implementation Example (Scala)</h3>
```Scala
class TestProducer[A](iterable : java.lang.Iterable[A], chunkSize : java.lang.Integer) extends Producer[A] {

  def enumeratee() = StreamEnumeratee(iterable.iterator.toStream,chunkSize)
}
```
<h3>Translatee Implementation Example (Scala)</h3>
```Scala
class TestIntToStringTranslator extends Translator[java.lang.Integer, String] {
  class Cont extends Translatee[java.lang.Integer,String]  {

    def status = StatusCode.CONTINUE

    def apply(items: Traversable[java.lang.Integer]) = {
      println("translating=" + items)
      val nextOutput = items.foldLeft(List[String]()) {
        (list,item) => {
          item.toString :: list
        }
      }
      Result(this, nextOutput)
    }
  }
  def translatee = new Cont()
}
```
