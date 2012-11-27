org.gtri.util.iteratee
======================

A Java interface-front-ended library implemented in Scala for streaming objects between producers and consumers.

<h2> Simple Java Example </h2>
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