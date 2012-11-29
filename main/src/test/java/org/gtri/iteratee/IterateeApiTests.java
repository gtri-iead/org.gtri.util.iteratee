/*
    Copyright 2012 Georgia Tech Research Institute

    Author: lance.gatlin@gtri.gatech.edu

    This file is part of org.gtri.util.iteratee library.

    org.gtri.util.iteratee library is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    org.gtri.util.iteratee library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with org.gtri.util.iteratee library. If not, see <http://www.gnu.org/licenses/>.

*/

package org.gtri.iteratee;

import java.util.ArrayList;
import java.util.List;
import org.gtri.util.iteratee.api.*;
import org.gtri.util.iteratee.api.Consumer;
import org.gtri.util.iteratee.impl.test.TestIntToStringTranslator;
import org.gtri.util.iteratee.impl.test.TestProducer;
import org.gtri.util.iteratee.impl.test.TestIntIteratee;
import org.gtri.util.iteratee.impl.test.TestStringBuilder;
import org.gtri.util.iteratee.impl.test.TestStringConsumer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.gtri.util.iteratee.IterateeFactory;

/**
 *
 * @author lance.gatlin@gmail.com
 */
public class IterateeApiTests {
  List<Integer> integers = new ArrayList<Integer>();
  List<String> strings = new ArrayList<String>();
  List<String> strings2 = new ArrayList<String>();
  IterateeFactory factory = new IterateeFactory();
  Planner planner = factory.createPlanner();
  
  public IterateeApiTests() {
    integers.add(1);
    integers.add(2);
    integers.add(3);
    strings.add("a");
    strings.add("b");
    strings.add("c");
    strings2.add("d");
    strings2.add("e");
    strings2.add("f");
  }
  
  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  @Test
  public void testIntToString() {
    System.out.println("===testIntToString===");
    System.out.println("Creating TestIntProducer...");
    TestProducer<Integer> integerProducer = new TestProducer(integers,1);
    
    System.out.println("Creating TestIntToStringTranslator...");
    TestIntToStringTranslator intToString = new TestIntToStringTranslator();
    
    System.out.println("Creating TestStringConsumer...");
    List<String> output = new ArrayList<String>();
    TestStringConsumer stringConsumer = new TestStringConsumer(output);
    
    System.out.println("Connecting TestIntegerProducer to TestStringConsumer...");
    Consumer.Plan<Integer,String> plan = planner.connect(integerProducer, intToString, stringConsumer);
    System.out.println("Running plan...");
    Consumer.Result<Integer,String> result = plan.run();
        
    System.out.print("Successful? ");
    assertTrue(result.status().isSuccess());
    System.out.println("Ok.");
    
    System.out.println("Testing output... ");
    StringBuilder outBuilder = new StringBuilder();
    for(String s : output) {
      outBuilder.append(s);
    }
    String actual = outBuilder.toString();
    System.out.println("results=" + actual);
    assertEquals("123", actual);
    System.out.println("Ok.");
  }
  
  @Test
  public void testStringToStringBuilder() {
    System.out.println("===testStringToStringBuilder===");
    System.out.println("Creating TestStringProducer...");
    TestProducer<String> stringProducer = new TestProducer(strings,256);
    
    System.out.println("Creating TestStringConsumer...");
    List<String> output = new ArrayList<String>();
    TestStringConsumer stringConsumer = new TestStringConsumer(output);
    
    System.out.println("Connecting TestStringProducer to TestStringConsumer...");
    Consumer.Plan<String, String> plan = planner.connect(stringProducer, stringConsumer);
    System.out.println("Running plan...");
    Consumer.Result<String, String> result = plan.run();
    
    System.out.print("Successful? ");
    assertTrue(result.status().isSuccess());
    System.out.println("Ok.");
    
    System.out.println("Testing output... ");
    StringBuilder outBuilder = new StringBuilder();
    for(String s : output) {
      outBuilder.append(s);
    }
    String actual = outBuilder.toString();
    System.out.println("results=" + actual);
    assertEquals("abc", actual);
    System.out.println("Ok.");
  }
  
  @Test
  public void testStringConcat() {
    System.out.println("===testStringConcat===");
    System.out.println("Creating TestStringProducer...");
    TestProducer<String> stringProducer1 = new TestProducer(strings,2);
    System.out.println("Creating TestStringProducer...");
    TestProducer<String> stringProducer2 = new TestProducer(strings2,2);
//    Producer<String> stringProducer3 = planner.concat(stringProducer1, stringProducer2);
    System.out.println("Creating TestStringBuilder...");
    TestStringBuilder stringBuilder = new TestStringBuilder();

    System.out.println("Connecting TestStringProducer to TestStringBuilder...");
    Builder.Plan<String,String,String> plan = planner.connect(stringProducer2, stringBuilder);
    System.out.println("Running plan...");
    Builder.Result<String,String,String> result = plan.run();
    
    System.out.print("Success? ");
    assertTrue(result.status().isSuccess());
    System.out.println("Ok.");
    
    System.out.println("Testing results...");
    String actual = result.value().get();
    System.out.println("results=" + actual);
    assertEquals("def", actual);
    System.out.println("Ok.");
  }

  @Test
  public void testIteratee() {
    System.out.println("===testIteratee===");
    System.out.println("Creating TestIntProducer...");
    TestProducer<Integer> integerProducer = new TestProducer(integers,1);
    
    System.out.println("Creating TestIntIteratee...");
    TestIntIteratee testIntIteratee = new TestIntIteratee();
    
    System.out.println("Connecting TestIntegerProducer to TestIntIteratee...");
    Iteratee.Plan<Integer,Integer,Integer> plan = planner.connect(integerProducer, testIntIteratee);
    System.out.println("Running plan...");
    Iteratee.Result<Integer,Integer,Integer> result = plan.run();
        
    System.out.print("Successful? ");
    assertTrue(result.status().isSuccess());
    System.out.println("Ok.");
    
    System.out.println("Testing output... ");
    Integer actual = result.loopState();
    System.out.println("results=" + actual);
    assertEquals(6, actual.intValue());
    System.out.println("Ok.");
  }
}
