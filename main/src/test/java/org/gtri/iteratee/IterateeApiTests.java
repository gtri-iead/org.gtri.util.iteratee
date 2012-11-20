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
import org.gtri.util.iteratee.impl.test.TestIntProducer;
import org.gtri.util.iteratee.impl.test.TestIntToStringTranslator;
import org.gtri.util.iteratee.impl.test.TestStringBuilder;
import org.gtri.util.iteratee.impl.test.TestStringConsumer;
import org.gtri.util.iteratee.impl.test.TestStringProducer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Lance
 */
public class IterateeApiTests {
  List<Integer> integers = new ArrayList<Integer>();
  List<String> strings = new ArrayList<String>();
  List<String> strings2 = new ArrayList<String>();
  Planner planner = new org.gtri.util.iteratee.impl.Planner();
  
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
    Producer<Integer> integerProducer = new TestIntProducer(integers);
    
    System.out.println("Creating TestIntToStringTranslator...");
    Translator<Integer,String> intToString = new TestIntToStringTranslator();
    
    System.out.println("Creating TestStringConsumer...");
    List<String> output = new ArrayList<String>();
    Consumer<String> stringConsumer = new TestStringConsumer(output);
    
    System.out.println("Translating TestIntegerProducer with TestIntToStringTranslator...");
    Producer<String> stringProducer = planner.translate(intToString, integerProducer);
    System.out.println("Connecting TestIntegerProducer to TestStringConsumer...");
    Consumer.Plan<String> plan = planner.connect(stringProducer, stringConsumer);
    System.out.println("Running plan...");
    Consumer.Result<String> result = plan.run();
        
    System.out.print("Successful? ");
    assertTrue(result.status().isSuccess());
    System.out.println("Ok.");
    
    System.out.print("Testing output... ");
    StringBuilder outBuilder = new StringBuilder();
    for(String s : output) {
      outBuilder.append(s);
    }
    String actual = outBuilder.toString();
    System.out.print("results=" + actual);
    assertEquals("123", actual);
    System.out.println("Ok.");
  }
  
  @Test
  public void testStringToStringBuilder() {
    System.out.println("===testStringToStringBuilder===");
    System.out.println("Creating TestStringProducer...");
    Producer<String> stringProducer = new TestStringProducer(strings);
    
    System.out.println("Creating TestStringConsumer...");
    List<String> output = new ArrayList<String>();
    Consumer<String> stringConsumer = new TestStringConsumer(output);
    
    System.out.println("Connecting TestStringProducer to TestStringConsumer...");
    Consumer.Plan<String> plan = planner.connect(stringProducer, stringConsumer);
    System.out.println("Running plan...");
    Consumer.Result<String> result = plan.run();
    
    System.out.print("Successful? ");
    assertTrue(result.status().isSuccess());
    System.out.println("Ok.");
    
    System.out.print("Testing output... ");
    StringBuilder outBuilder = new StringBuilder();
    for(String s : output) {
      outBuilder.append(s);
    }
    String actual = outBuilder.toString();
    System.out.print("results=" + actual);
    assertEquals("abc", actual);
    System.out.println("Ok.");
  }
  
  @Test
  public void testStringConcat() {
    System.out.println("===testStringConcat===");
    System.out.println("Creating TestStringProducer...");
    Producer<String> stringProducer1 = new TestStringProducer(strings);
    System.out.println("Creating TestStringProducer...");
    Producer<String> stringProducer2 = new TestStringProducer(strings2);
//    Producer<String> stringProducer3 = planner.concat(stringProducer1, stringProducer2);
    System.out.println("Creating TestStringBuilder...");
    Builder<String,String> stringBuilder = new TestStringBuilder();

    System.out.println("Connecting TestStringProducer to TestStringBuilder...");
    Builder.Plan<String,String> plan = planner.connect(stringProducer2, stringBuilder);
    System.out.println("Running plan...");
    Builder.Result<String,String> result = plan.run();
    
    System.out.print("Success? ");
    assertTrue(result.status().isSuccess());
    System.out.println("Ok.");
    
    System.out.print("Testing results...");
    String actual = result.value().get();
    System.out.print("results=" + actual);
    assertEquals("def", actual);
    System.out.println("Ok.");
  }
}
