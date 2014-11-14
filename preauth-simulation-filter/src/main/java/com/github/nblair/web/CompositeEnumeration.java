/**
 * 
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
