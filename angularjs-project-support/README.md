## AngularJS Project Support

This module contains re-usable AngularJS components. The deliverable for this module is a jar that provides these resources to Servlet 3.0 environments
(google "Servlet 3.0 static resources" for more).

### Build

The "build" directive is intended for use with the `ProjectController#build` API provided by the spring-project-rest-api module.

Add the build_directive.js script to your page, and render it with:

```
<build api-url="build"></build>
```
