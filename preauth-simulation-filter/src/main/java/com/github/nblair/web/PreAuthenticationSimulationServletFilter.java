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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet {@link Filter} that allows a developer to simulate various "pre-authentication"
 * behaviors that are not usually available on a developer's workstation.
 * 
 * Sample web.xml configuration: 
 <pre> 
  
  &lt;filter&gt;
    &lt;filter-name&gt;PreAuthenticationSimulationServletFilter&lt;/filter-name&gt;
    &lt;filter-class&gt;com.github.nblair.web.PreAuthenticationSimulationServletFilter&lt;/filter-class&gt;
    &lt;init-param&gt;
      &lt;param-name&gt;preauth.remoteUser&lt;/param-name&gt;
      &lt;param-value&gt;someusername&lt;/param-value&gt;
    &lt;/init-param&gt;
    &lt;init-param&gt;
      &lt;param-name&gt;preauth.headerNames&lt;/param-name&gt;
      &lt;param-value&gt;header1;header2;header3&lt;/param-value&gt;
    &lt;/init-param&gt;
    &lt;init-param&gt;
      &lt;param-name&gt;preauth.headerValues&lt;/param-name&gt;
      &lt;param-value&gt;value1;mvalue2a,mvalue2b;value3&lt;/param-value&gt;
    &lt;/init-param&gt;
  &lt;/filter&gt;

  &lt;filter-mapping&gt;
    &lt;filter-name&gt;PreAuthenticationSimulationServletFilter&lt;/filter-name&gt;
    &lt;url-pattern&gt;/*&lt;/url-pattern&gt;        
  &lt;/filter-mapping&gt;
  
 </pre>
 * 
 * @author Nicholas Blair
 */
public class PreAuthenticationSimulationServletFilter implements Filter {

	/**
	 * Name of the filter init-param for specifying additional header names to override. 
	 * Must be used in conjunction with {@link #INIT_PARAM_PREAUTH_HEADER_VALUES}.
	 */
	public static final String INIT_PARAM_PREAUTH_HEADER_NAMES = "preauth.headerNames";
	/**
	 * Name of the filter init-param for specifying additional header values to override.
	 * Must be used in tandem with {@link #INIT_PARAM_PREAUTH_HEADER_NAMES}.
	 */
	public static final String INIT_PARAM_PREAUTH_HEADER_VALUES = "preauth.headerValues";
	/**
	 * Name of the filter init-param for specifying the value of REMOTE_USER.
	 */
	public static final String INIT_PARAM_PREAUTH_REMOTE_USER = "preauth.remoteUser";
	
	private String remoteUser;
	private Map<String, List<String>> additionalHeaders = Collections.emptyMap();
	/**
	 * @return the remoteUser
	 */
	public String getRemoteUser() {
		return remoteUser;
	}
	/**
	 * @param remoteUser the remoteUser to set
	 */
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}
	/**
	 * @return the additionalHeaders
	 */
	public Map<String, List<String>> getAdditionalHeaders() {
		return additionalHeaders;
	}
	/**
	 * @param additionalHeaders the additionalHeaders to set
	 */
	public void setAdditionalHeaders(Map<String, List<String>> additionalHeaders) {
		this.additionalHeaders = additionalHeaders;
	}
	public void setAdditionalHeaders(String headerNames, String headerValues) throws ServletException {
		setAdditionalHeaders(toMap(headerNames, headerValues));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		setRemoteUser(filterConfig.getInitParameter(INIT_PARAM_PREAUTH_REMOTE_USER));
		
		String names = filterConfig.getInitParameter(INIT_PARAM_PREAUTH_HEADER_NAMES);
		String values = filterConfig.getInitParameter(INIT_PARAM_PREAUTH_HEADER_VALUES);
		
		setAdditionalHeaders(names, values);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new PreAuthenticationSimulationHttpServletRequestWrapper((HttpServletRequest) request, remoteUser, additionalHeaders),  response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * Convert the headerNames and headerValues parameters to a {@link Map} of {@link String} to {@link List}.
	 * 
	 * Note this class uses a quirky feature of {@link String#split(String, int)}. From the javadoc:
	 * <pre>
	 	"If n is non-positive then the pattern will be applied as many times as possible and the array can have any length."
	 * </pre>
	 * 
	 * We use a negative value for the int parameter because if the parameter value ends with the delimiter, we want to capture an empty string afterwards.
	 * 
	 * @see String#split(String, int)
	 * @param headerNamesParameter
	 * @param headerValuesParameter
	 * @return a {@link Map} representing the additional headers to append to the requests modified by this filter
	 * @throws ServletException
	 */
	protected Map<String, List<String>> toMap(String headerNamesParameter, String headerValuesParameter) throws ServletException {
		if(headerNamesParameter == null || headerValuesParameter == null) {
			return Collections.emptyMap();
		}
		
		String [] headerNames = headerNamesParameter.split(";");
		String [] headerValues = headerValuesParameter.split(";", -1);
		
		if(headerNames.length != headerValues.length) {
			throw new ServletException("Length of 'preauth.headerNames' (" + headerNames.length + ") and 'preauth.headerValues (" + headerValues.length + ") does not match");
		}
		
		Map<String, List<String>> result = new HashMap<>();
		for(int i = 0; i < headerNames.length; i++) {			
			result.put(headerNames[i], Arrays.asList(headerValues[i].split(",", -1)));
		}
		return result;
	}
	
}
 