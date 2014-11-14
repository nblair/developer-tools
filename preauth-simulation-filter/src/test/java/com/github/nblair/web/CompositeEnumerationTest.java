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
