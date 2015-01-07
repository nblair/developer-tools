# preauth-simulation-filter-servlet_2.5

This package is almost an exact copy of preauth-simulation-filter.  The major difference is that this version specifically targets a jdk 6 /servlet 2.5 environment AND an extra paramater, preauth.Enabled, must be provided in order to enable preauth.  

Servlet 2.5 does not provide the ability to dynamically register filters, therefore this filter is always registered but by default performs no-op.  Preauth will only function when preauth.Enabled is set to true and preauth.remoteUser contains a valid username.

### Caveat Emptor

Since this filter is clearly going to give you the ability to simulate data for trusted request attributes in your application,
make sure it's not something you deploy in your production environment. If you are doing this in production, definitely
make sure you understand what you are doing.


### Configuring

Here is a sample web.xml:

    <filter>
      <filter-name>PreAuthenticationSimulationServletFilter</filter-name>
      <filter-class>com.github.nblair.web.PreAuthenticationSimulationServletFilter</filter-class>
      <init-param>
        <param-name>preauth.enabled</param-name>
        <param-value>true</param-value>
      </init-param>
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
    

