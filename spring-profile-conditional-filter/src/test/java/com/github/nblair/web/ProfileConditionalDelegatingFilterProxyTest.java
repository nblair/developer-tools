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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.web.context.WebApplicationContext;

/**
 * Tests for {@link ProfileConditionalDelegatingFilterProxy}.
 * 
 * @author Nicholas Blair
 */
public class ProfileConditionalDelegatingFilterProxyTest {

	/**
	 * Verify behavior of {@link ProfileConditionalDelegatingFilterProxy#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
	 * when {@link ProfileConditionalDelegatingFilterProxy#getRequiredProfiles()} is empty.
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@Test
	public void doFilter_no_required_profiles() throws ServletException, IOException {
		Filter delegate = mock(Filter.class);
		ProfileConditionalDelegatingFilterProxy filterProxy = new ProfileConditionalDelegatingFilterProxy(delegate);
		
		ServletRequest request = mock(ServletRequest.class);
		ServletResponse response = mock(ServletResponse.class);
		FilterChain filterChain = mock(FilterChain.class);
		filterProxy.doFilter(request, response, filterChain);
		
		verify(delegate).doFilter(request, response, filterChain);
	}
	/**
	 * Verify behavior when the single required profile is not active (delegate should NOT be invoked).
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void doFilter_required_profile_not_set() throws ServletException, IOException {
		Filter delegate = mock(Filter.class);
		ProfileConditionalDelegatingFilterProxy filterProxy = new ProfileConditionalDelegatingFilterProxy(delegate);
		filterProxy.setRequiredProfile("foo");
		ServletContext servletContext = mockServletContextWithEnvironment(new MockEnvironment());
		
		filterProxy.setServletContext(servletContext);
		ServletRequest request = mock(ServletRequest.class);
		ServletResponse response = mock(ServletResponse.class);
		FilterChain filterChain = mock(FilterChain.class);
		filterProxy.doFilter(request, response, filterChain);
		
		verify(delegate, never()).doFilter(request, response, filterChain);
		verify(filterChain).doFilter(request, response);
	}
	/**
	 * Verify behavior when the single required profile is active (delegate SHOULD be invoked).
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void doFilter_required_profile_set() throws ServletException, IOException {
		Filter delegate = mock(Filter.class);
		ProfileConditionalDelegatingFilterProxy filterProxy = new ProfileConditionalDelegatingFilterProxy(delegate);
		filterProxy.setRequiredProfile("foo");
		MockEnvironment environment = new MockEnvironment();
		environment.setActiveProfiles("foo");
		ServletContext servletContext = mockServletContextWithEnvironment(environment);
		
		filterProxy.setServletContext(servletContext);
		ServletRequest request = mock(ServletRequest.class);
		ServletResponse response = mock(ServletResponse.class);
		FilterChain filterChain = mock(FilterChain.class);
		filterProxy.doFilter(request, response, filterChain);
		
		verify(delegate).doFilter(request, response, filterChain);
	}
	
	/**
	 *  Verify behavior when multiple required profiles are active in the environment (delegate SHOULD be invoked).
	 *  
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void doFilter_required_multiple_profiles_all_set() throws ServletException, IOException {
		Filter delegate = mock(Filter.class);
		ProfileConditionalDelegatingFilterProxy filterProxy = new ProfileConditionalDelegatingFilterProxy(delegate);
		filterProxy.setRequiredProfiles("foo", "bar");
		MockEnvironment environment = new MockEnvironment();
		environment.setActiveProfiles("foo", "bar");
		ServletContext servletContext = mockServletContextWithEnvironment(environment);
		
		filterProxy.setServletContext(servletContext);
		ServletRequest request = mock(ServletRequest.class);
		ServletResponse response = mock(ServletResponse.class);
		FilterChain filterChain = mock(FilterChain.class);
		filterProxy.doFilter(request, response, filterChain);
		
		verify(delegate).doFilter(request, response, filterChain);
	}
	
	/**
	 * Verify behavior when multiple required profiles are set and all but 1 are active in the environment (delegate should NOT be invoked).
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void doFilter_required_multiple_profiles_one_missing() throws ServletException, IOException {
		Filter delegate = mock(Filter.class);
		ProfileConditionalDelegatingFilterProxy filterProxy = new ProfileConditionalDelegatingFilterProxy(delegate);
		filterProxy.setRequiredProfiles("foo", "bar", "baz");
		MockEnvironment environment = new MockEnvironment();
		environment.setActiveProfiles("foo", "bar");
		ServletContext servletContext = mockServletContextWithEnvironment(environment);
		
		filterProxy.setServletContext(servletContext);
		ServletRequest request = mock(ServletRequest.class);
		ServletResponse response = mock(ServletResponse.class);
		FilterChain filterChain = mock(FilterChain.class);
		filterProxy.doFilter(request, response, filterChain);
		
		verify(delegate, never()).doFilter(request, response, filterChain);
		verify(filterChain).doFilter(request, response);
	}
	
	/**
	 * Set up a {@link ServletContext} to contain a {@link WebApplicationContext} with the provided {@link Environment}.
	 * 
	 * @param environment
	 * @return a mock {@link ServletContext} ready for use in the delegate filter proxy
	 */
	protected ServletContext mockServletContextWithEnvironment(Environment environment) {
		ServletContext servletContext = mock(ServletContext.class);
		WebApplicationContext wac = mock(WebApplicationContext.class);
	
		when(wac.getEnvironment()).thenReturn(environment);
		when(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).thenReturn(wac);
		return servletContext;
	}
}
