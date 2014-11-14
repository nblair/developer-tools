# spring-profile-conditional-filter

Spring's DelegatingFilterProxy is a very useful class, allowing one to construct a Servlet
Filter just like any other Bean.

This class connects DelegatingFilterProxy with [Spring's Profile support](http://spring.io/blog/2011/02/14/spring-3-1-m1-introducing-profile/).
The delegate filter wrapped by this class is only triggered when all of the configured required profiles are active.

### Example Use Case

Say you have an application that in production depends on some type of Pre-Authentication (like Siteminder
or Shibboleth). It is not easy or realistic in all scenarios for a developer to have these complex
environment features available on their development workstations.

A developer might want to add the PreAuthenticationSimulationServletFilter (see [preauth-simulation-filter
sibling module](../preauth-simulation-filter)) to the application so when running the application locally, the headers and environment
variables provided by these Pre-Authentication tools are available.

This might manifest in a Spring application like so:

    /* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		super.onStartup(servletContext);
		
		servletContext.addFilter("preAuthenticationSimulationFilter", 
			new DelegatingFilterProxy("preAuthenticationSimulationFilter"))
        	.addMappingForUrlPatterns(null, false, "/preauth-login");
	}
	
Now we have a problem though: this code will be activated in other environments, including the
test/qa/production environments that have the Pre-Authentication tools actually installed. What we
need is an ability connect this filter's functionality to Spring's Profile support.

This package provides a DelegatingFilterProxy that invokes the delegate Filter if and only if any and all required profiles
are active in the environment. The above example now changes to:

    /* (non-Javadoc)
	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		super.onStartup(servletContext);
		
		servletContext.addFilter("preAuthenticationSimulationFilter", 
			new ProfileConditionalDelegatingFilterProxy("preAuthenticationSimulationFilter").setRequiredProfile("preauth-dev"))
        	.addMappingForUrlPatterns(null, false, "/my-login");
	}

Now, the developer specific functions provided by PreAuthenticationSimulationServletFilter are only performed if
the "preauth-dev" Spring Profile is active.