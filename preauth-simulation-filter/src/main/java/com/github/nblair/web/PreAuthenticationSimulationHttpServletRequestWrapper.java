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

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * {@link HttpServletRequestWrapper} that allows us to alter the return 
 * @author Nicholas Blair
 */
public class PreAuthenticationSimulationHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final String remoteUser;
	private final Map<String, List<String>> additionalHeaders;
	/**
	 * Constructs an instance with a value for {@link #getRemoteUser()} but no additional values for {@link #getHeader(String)} variants.
	 * 
	 * @param request
	 * @param remoteUser
	 */
	public PreAuthenticationSimulationHttpServletRequestWrapper(HttpServletRequest request, String remoteUser) {
		this(request, remoteUser, Collections.<String, List<String>>emptyMap());
	}
	/**
	 * Constructs an instance with additional values for {@link #getHeader(String)} variants and null for {@link #getRemoteUser()}.
	 * 
	 * @param request
	 * @param additionalHeaders
	 */
	public PreAuthenticationSimulationHttpServletRequestWrapper(HttpServletRequest request, Map<String, List<String>> additionalHeaders) {
		this(request, null, additionalHeaders);
	}
	/**
	 * Primary constructor. 
	 * 
	 * @param request the wrapped {@link HttpServletRequest}.
	 * @param remoteUser the value to return for {@link #getRemoteUser()}
	 * @param additionalHeaders the values to return for the {@link #getHeader(String)} variants
	 */
	public PreAuthenticationSimulationHttpServletRequestWrapper(
			HttpServletRequest request, String remoteUser,
			Map<String, List<String>> additionalHeaders) {
		super(request);
		this.remoteUser = remoteUser;
		this.additionalHeaders = additionalHeaders;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		String override = getOverrideHeaderValue(name);
		return override != null ? override : super.getHeader(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaders(java.lang.String)
	 */
	@Override
	public Enumeration<String> getHeaders(String name) {
		if(additionalHeaders.containsKey(name)) {
			return new CompositeEnumeration<String>(super.getHeaders(name), additionalHeaders.get(name).iterator());
		} else {
			return super.getHeaders(name);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaderNames()
	 */
	@Override
	public Enumeration<String> getHeaderNames() {
		return new CompositeEnumeration<String>(super.getHeaderNames(), additionalHeaders.keySet().iterator());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getIntHeader(java.lang.String)
	 */
	@Override
	public int getIntHeader(String name) {
		String override = getOverrideHeaderValue(name);
		return override != null ? Integer.parseInt(override) : super.getIntHeader(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequestWrapper#getRemoteUser()
	 */
	@Override
	public String getRemoteUser() {
		return this.remoteUser != null ? remoteUser : super.getRemoteUser();
	}
	/**
	 * 
	 * @param name
	 * @return the value of the overridden header, or null if not set.
	 */
	protected String getOverrideHeaderValue(String name) {
		List<String> fromAdditional = additionalHeaders.get(name);
		if(fromAdditional != null && !fromAdditional.isEmpty()) {
			return fromAdditional.get(0);
		}
		return null;
	}
}
