[![Build Status](https://travis-ci.org/nblair/developer-tools.svg?branch=master)](https://travis-ci.org/nblair/developer-tools)

# Developer Tools

This is a collection of (questionably?) useful libraries supporting
me and my team's software development.

### preauth-simulation filter

[preauth-simulation-filter](preauth-simulation-filter/README.md) includes a Java Servlet
Filter that lets a developer spoof Pre-Authentication environments (Shibboleth, SiteMinder) locally
without having to install them on their workstation.

Available via Maven Central:

    <dependency>
    	<groupId>com.github.nblair</groupId>
    	<artifactId>preauth-simulation-filter</artifactId>
    	<version>0.1.0</version>
    </dependency>
    
### spring-profile-conditional-filter

[spring-profile-conditional-filter](spring-profile-conditional-filter/README.md) gives you the means
to conditionally activate a Java Servlet Filter if and only if one or more Spring Profiles
are active.

Available via Maven Central:

    <dependency>
    	<groupId>com.github.nblair</groupId>
    	<artifactId>spring-profile-conditional-filter</artifactId>
    	<version>0.1.0</version>
    </dependency>
    