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

package org.gtri.util.iteratee.api;

/**
 * Try #4
 * @author Lance
 */
public interface Translatee<A,B,S> extends Iteratee<A,S> {
  
  Iteratee<B,S> downstream();
  <T> Translatee<A,B,T> attach(Iteratee<B,T> downstream);  
}

/*
 * Try #3: Reverted this. See Enumeratee try #1 for reasoning.
 *
public interface Translatee<A,B,S,Ib extends Iteratee<B,S>> extends Iteratee<A,S> {
  
  List<Issue> issues();
  
  StatusCode status();
  
  Ib downstream();
  <T, Jb extends Iteratee<B,T>> Translatee<A,B,T,Jb> attach(Jb downstream);
  
  @Override
  Translatee<A,B,S,Ib> apply(A a);
  @Override
  Translatee<A,B,S,Ib> apply(Signals.EndOfInput unused);  
}
 */
/* Try #2: I was confused here
 * 
public interface Translatee<A,B,Sa extends State<A>, Sb extends State<B>> extends Iteratee<A,Sa> {
  
  Iteratee<B,Sb> downstream();
  <Ta extends State<A>, Tb extends State<B>> Translatee<A,B,Ta,Tb> attach(Iteratee<B,Tb> downstream);

*/
/* Try #1: in the impl for this, an iteratee to attach to the inner enumeratee 
 * is still required
 * 
public interface Translatee<A,B,S> extends Enumeratee<B,S> {
  
  Enumeratee<A,S> upstream();
  <T> Translatee<A,B,T> attach(Enumeratee<A,T> upstream);
  
}
 */
