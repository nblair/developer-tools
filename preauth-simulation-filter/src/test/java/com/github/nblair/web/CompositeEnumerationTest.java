/**
 * 
 */
package com.github.nblair.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * Tests on {@link CompositeEnumeration}.
 * 
 * @author Nicholas Blair
 */
public class CompositeEnumerationTest {

	/**
	 * Verify behavior of {@link CompositeEnumeration} for both empty iterator and empty enumeration.
	 */
	@Test(expected=NoSuchElementException.class)
	public void both_empty() {
		CompositeEnumeration<String> c = new CompositeEnumeration<>(Collections.<String>emptyEnumeration(), Collections.<String>emptyIterator());
		assertFalse(c.hasMoreElements());
		// will result in expected exception
		c.nextElement();
	}
	
	/**
	 * Verify behavior of {@link CompositeEnumeration} for enumeration and iterator of strings.
	 */
	@Test
	public void composite_example() {
		Enumeration<String> enumeration = Collections.enumeration(Arrays.<String>asList("A", "B", "C"));
		CompositeEnumeration<String> composite = new CompositeEnumeration<String>(enumeration, Arrays.<String>asList("D", "E", "F").iterator());
		
		StringBuffer results = new StringBuffer();
		while(composite.hasMoreElements()) {
			results.append(composite.nextElement());
		}
		
		assertEquals("DEFABC", results.toString());
	}
}
