/**
 * Board of Regents of the University of Wisconsin System
 * licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.nblair.web;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Composite {@link Enumeration} wrapping another {@link Enumeration} AND an {@link Iterator}.
 * 
 * {@link #nextElement()} will exhaust the {@link Iterator} first.
 * 
 * Note: if the {@link Iterator} and the {@link Enumeration} both include an equivalent object
 * or objects, no {@link #equals(Object)} checking is done to return once.
 * 
 * @author Nicholas Blair
 */
public class CompositeEnumeration<E> implements Enumeration<E> {

	private final Enumeration<E> wrapped;
	private final Iterator<E> iterator;
	/**
	 * @param wrapped
	 * @param iterator
	 */
	public CompositeEnumeration(Enumeration<E> wrapped, Iterator<E> iterator) {
		this.wrapped = wrapped;
		this.iterator = iterator;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * Delegates to {@link Iterator#hasNext()} first, then the wrapped {@link Enumeration#hasMoreElements()}.
	 */
	@Override
	public boolean hasMoreElements() {
		if(iterator.hasNext()) {
			return true;
		}
		return wrapped.hasMoreElements();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns from the iterator first until exhausted, then from the wrapped enumeration.
	 */
	@Override
	public E nextElement() {
		if(iterator.hasNext()) {
			return iterator.next();
		}
		
		return wrapped.nextElement();
	}

}
