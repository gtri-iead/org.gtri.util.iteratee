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
 * An interface for a translator which translates input of one type into output 
 * of another type.
 * 
 * @author Lance
 */
public interface Translator<A, B>  {
  <S> Iteratee<A,S> translatee(Iteratee<B,S> i);
}

/* Try #4: Issues when composing Translatees, implementation becomes much easier
 * if the translatee function simply returns an iteratee
 * 
public interface Translator<A, B>  {
  <S> Translatee<A,B,S> translatee(Iteratee<B,S> i);
}
 */
/*
 * Try #3 See Enumeratee #1 for deatils why this doesn't work
public interface Translator<A, B>  {
  <S,Ib extends Iteratee<B,S>> Translatee<A,B,S,Ib> translatee(Ib i);
}

 */
/*
 * Try #2: See Translatee for why this didn't work.
public interface Translator<A, B>  {
  <S> Enumeratee<B,S> translate(Enumeratee<A,S> e);
}

 */
/*
 * Try #1: only allowing translation of IterS,IterV. Still might do this.
public interface Translator<A, B>  {
  IterS<A> translate(IterS<B> translate);
  <V> IterV<A,V> translate(IterV<B,V> translate);
}

 */
