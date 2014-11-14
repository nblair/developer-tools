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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Tests for {@link PreAuthenticationSimulationServletFilter}.
 * 
 * @author Nicholas Blair
 */
public class PreAuthenticationSimulationServletFilterTest {

	/**
	 * Setup an instance of {@link PreAuthenticationSimulationServletFilter} with nothing
	 * provided for username or additionalHeaders; confirm expected request behavior unmodified.
	 * @throws ServletException 
	 * @throws IOException 
	 */
	@Test
	public void doFilter_no_overrides() throws IOException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		// setup the request to return this data (not the filter)
		when(request.getRemoteUser()).thenReturn("someuser");
		when(request.getHeader("foo")).thenReturn("bar");
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = new SimpleAssertionFilterChain("someuser", ImmutableMap.of("foo", "bar"));
		
		new PreAuthenticationSimulationServletFilter().doFilter(request, response, chain);
	}
	/**
	 * Populate {@link PreAuthenticationSimulationServletFilter#setRemoteUser(String)} and verify that the
	 * {@link HttpServletRequest#getRemoteUser()} reflects the value.
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void doFilter_override_remoteUser() throws IOException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		PreAuthenticationSimulationServletFilter filter = new PreAuthenticationSimulationServletFilter();
		filter.setRemoteUser("override-remoteUser");
		
		FilterChain chain = new SimpleAssertionFilterChain("override-remoteUser", Collections.<String, String>emptyMap());
		filter.doFilter(request, response, chain);
	}
	/**
	 * Populate {@link PreAuthenticationSimulationServletFilter#setAdditionalHeaders(Map)} and verify
	 * that {@link HttpServletRequest#getHeader(String)} reflects the value.
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@Test
	public void doFilter_override_header() throws IOException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		PreAuthenticationSimulationServletFilter filter = new PreAuthenticationSimulationServletFilter();
		Map<String, List<String>> additionalHeaders = new HashMap<>();
		additionalHeaders.put("foo1", Arrays.asList("bar1"));
		additionalHeaders.put("foo2", Arrays.asList("bar2"));
		filter.setAdditionalHeaders(additionalHeaders);
		
		FilterChain chain = new SimpleAssertionFilterChain(null, ImmutableMap.of("foo1", "bar1", "foo2", "bar2"));
		filter.doFilter(request, response, chain);
	}
	/**
	 * Verify behavior of {@link PreAuthenticationSimulationServletFilter#init(FilterConfig)} when no init-params
	 * are provided.
	 * @throws ServletException
	 */
	@Test
	public void init_nulls() throws ServletException {
		FilterConfig filterConfig = mock(FilterConfig.class);
		PreAuthenticationSimulationServletFilter filter = new PreAuthenticationSimulationServletFilter();
		filter.init(filterConfig);
		assertNull(filter.getRemoteUser());
		assertTrue(filter.getAdditionalHeaders().isEmpty());
	}
	/**
	 * Confirm expected result for sample init-params.
	 * 
	 * @throws ServletException
	 */
	@Test
	public void init_example() throws ServletException {
		FilterConfig filterConfig = mock(FilterConfig.class);
		when(filterConfig.getInitParameter("preauth.remoteUser")).thenReturn("inittest");
		when(filterConfig.getInitParameter("preauth.headerNames")).thenReturn("header1;header2;header3");
		when(filterConfig.getInitParameter("preauth.headerValues")).thenReturn("value1;mvalue2a,mvalue2b;value3");
		PreAuthenticationSimulationServletFilter filter = new PreAuthenticationSimulationServletFilter();
		filter.init(filterConfig);
		assertEquals("inittest", filter.getRemoteUser());
		assertEquals("value1", filter.getAdditionalHeaders().get("header1").get(0));
		assertEquals("mvalue2a", filter.getAdditionalHeaders().get("header2").get(0));
		assertEquals("mvalue2b", filter.getAdditionalHeaders().get("header2").get(1));
		assertEquals("value3", filter.getAdditionalHeaders().get("header3").get(0));
	}
	/**
	 * Similar to {@link #init_example()}, however all of the values in 'preauth.headerValues' should be
	 * empty strings.
	 * 
	 * @throws ServletException
	 */
	@Test
	public void init_empty_values() throws ServletException {
		FilterConfig filterConfig = mock(FilterConfig.class);
		when(filterConfig.getInitParameter("preauth.headerNames")).thenReturn("header1;header2;header3");
		when(filterConfig.getInitParameter("preauth.headerValues")).thenReturn(";,;");
		PreAuthenticationSimulationServletFilter filter = new PreAuthenticationSimulationServletFilter();
		filter.init(filterConfig);
		assertEquals("", filter.getAdditionalHeaders().get("header1").get(0));
		assertEquals("", filter.getAdditionalHeaders().get("header2").get(0));
		assertEquals("", filter.getAdditionalHeaders().get("header2").get(1));
		assertEquals("", filter.getAdditionalHeaders().get("header3").get(0));
	}
	/**
	 * 
	 * @throws ServletException
	 */
	@Test(expected=ServletException.class)
	public void init_mismatched_header_length() throws ServletException {
		FilterConfig filterConfig = mock(FilterConfig.class);
		when(filterConfig.getInitParameter("preauth.headerNames")).thenReturn("header1;header2;header3");
		when(filterConfig.getInitParameter("preauth.headerValues")).thenReturn("value1;mvalue2a,mvalue2b");
		PreAuthenticationSimulationServletFilter filter = new PreAuthenticationSimulationServletFilter();
		filter.init(filterConfig);
	}
	/**
	 * Simple {@link FilterChain} to perform our {@link Assert}ions.
	 * 
	 * @author Nicholas Blair
	 */
	static class SimpleAssertionFilterChain implements FilterChain {

		private final String expectedRemoteUser;
		private final Map<String, String> expectedHeaders;
		/**
		 * @param expectedRemoteUser expected value for {@link HttpServletRequest#getRemoteUser()}
		 * @param expectedHeaders a map of expectations for {@link HttpServletRequest#getHeader(String)}.
		 */
		public SimpleAssertionFilterChain(String expectedRemoteUser,
				Map<String, String> expectedHeaders) {
			super();
			this.expectedRemoteUser = expectedRemoteUser;
			this.expectedHeaders = expectedHeaders;
		}
		/*
		 * (non-Javadoc)
		 * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
		 */
		@Override
		public void doFilter(ServletRequest request, ServletResponse response)
				throws IOException, ServletException {
			assertEquals(expectedRemoteUser, ((HttpServletRequest)request).getRemoteUser());
			for(Entry<String, String> entry: expectedHeaders.entrySet()) {
				assertEquals(entry.getValue(), ((HttpServletRequest)request).getHeader(entry.getKey()));
			}
		}
	}
}
