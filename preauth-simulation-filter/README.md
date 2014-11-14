# preauth-simulation-filter

This package is intended to provide a Servlet Filter useful for developers
that want to simulate the effects of an environment that applies "pre-authentication."

An example of "pre-authentication" in this context would be:

- A Java Web Application that depends on HttpServletRequest#getRemoteUser or #getHeader(String) for user identity
- A Web Server like Apache sitting in front of the Java application using proxy_ajp 
- A SAML2 SP (like Shibboleth) providing identity attributes in REMOTE_USER or headers.

This environment can be tricky for a developer to setup on their local workstation. Further environment
concerns may prevent developers from setting up something similar to this altogether.

This library provides a Servlet Filter that a developer can configure to shim in the desired Pre-Authentication behavior.
There are no dependencies for this Filter (other than the Servlet API).

### Caveat Emptor

Since this filter is clearly going to give you the ability to simulate data for trusted request attributes in your application,
make sure it's not something you deploy in your production environment. If you are doing this in production, definitely
make sure you understand what you are doing.

If you are using Spring, look at [spring-profile-conditional-filter](../spring-profile-conditional-filter) to wrap this filter in a way that
is conditionally activated.

### Configuring

Here is a sample web.xml:

    <filter>
      <filter-name>PreAuthenticationSimulationServletFilter</filter-name>
      <filter-class>com.github.nblair.web.PreAuthenticationSimulationServletFilter</filter-class>
      <init-param>
        <param-name>preauth.remoteUser</param-name>
        <param-value>someusername</param-value>
      </init-param>
      <init-param>
        <param-name>preauth.headerNames</param-name>
        <param-value>header1;header2;header3;header4</param-value>
      </init-param>
      <init-param>
        <param-name>preauth.headerValues</param-name>
        <param-value>value1;mvalue2a,mvalue2b;value3;</param-value>
      </init-param>
    </filter>

(Add your own filter-mapping).

Given this example, requests that fall under the filter-mapping you've provided will respond accordingly:

    assertEquals("someusername", request.getRemoteUser());
    
    assertEquals("value1", request.getHeader("header1");
    
    assertEquals("mvalue2a", request.getHeader("header2");
    Enumeration<String> multiValued = request.getHeaders("header2");
    assertEquals("mvalue2a", multivalued.nextElement());
    assertEquals("mvalue2b", multivalued.nextElement());
    
    assertEquals("value3", request.getHeader("header3");
    
    assertEquals("", request.getHeader("header4");
    

