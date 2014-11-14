/**
 * 
 */
package com.github.nblair.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * Subclass of {@link DelegatingFilterProxy} that allows for conditional execution of the 
 * delegate {@link Filter} if and only if the required spring {@link Profile}s are active.
 * 
 * If any or all of the required profiles are not active, the delegate {@link Filter#doFilter(ServletRequest, ServletResponse, FilterChain)} 
 * is not invoked ({@link FilterChain#doFilter(ServletRequest, ServletResponse)} is allowed to proceed).
 * 
 * @see Environment#acceptsProfiles(String...)
 * @author Nicholas Blair
 */
public class ProfileConditionalDelegatingFilterProxy extends
		DelegatingFilterProxy {

	private String[] requiredProfiles = new String[0];
	/**
	 * 
	 */
	public ProfileConditionalDelegatingFilterProxy() {
		super();
	}
	/**
	 * @param delegate
	 */
	public ProfileConditionalDelegatingFilterProxy(Filter delegate) {
		super(delegate);
	}
	/**
	 * @param targetBeanName
	 * @param wac
	 */
	public ProfileConditionalDelegatingFilterProxy(String targetBeanName,
			WebApplicationContext wac) {
		super(targetBeanName, wac);
	}
	/**
	 * @param targetBeanName
	 */
	public ProfileConditionalDelegatingFilterProxy(String targetBeanName) {
		super(targetBeanName);
	}
	/**
	 * 
	 * @param requiredProfile
	 * @return
	 */
	public ProfileConditionalDelegatingFilterProxy setRequiredProfile(String requiredProfile) {
		this.requiredProfiles = new String[] { requiredProfile };
		return this;
	}
	/**
	 * 
	 * @param requiredProfiles
	 * @return
	 */
	public ProfileConditionalDelegatingFilterProxy setRequiredProfiles(String... requiredProfiles) {
		this.requiredProfiles = requiredProfiles;
		return this;
	}
	
	/**
	 * @return the requiredProfiles
	 */
	public String[] getRequiredProfiles() {
		return requiredProfiles;
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.filter.DelegatingFilterProxy#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		if(requiredProfilesAreActive()) {
			super.doFilter(request, response, filterChain);
		} else {
			filterChain.doFilter(request, response);
		}
	}
	/**
	 * Returns true if all the required profiles are active in the environment.
	 * 
	 * Note, even though {@link Environment#acceptsProfiles(String...)} is multi-valued, we have
	 * to check each profile individually because ENvironment's method will return true if any of the arguments
	 * are valid (not necessarily all).
	 * 
	 * @see Environment#acceptsProfiles(String...)
	 * @return true if all {@link #getRequiredProfiles()} are active
	 */
	protected boolean requiredProfilesAreActive() {
		if(requiredProfiles.length == 0) {
			return true;
		}
		Environment environment = findWebApplicationContext().getEnvironment();
		for(String profile: requiredProfiles) {
			if(!environment.acceptsProfiles(profile)) {
				return false;
			}
		}
		return true;
	}
}
