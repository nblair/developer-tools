## spring-project-rest-api

This module provides Spring `@Controller`s that expose a REST API for information about your project, like Build information and `Environment.getActiveProfiles()`.

### Adding to your project

Add this dependency to your project:

```
<dependency>
  <groupId>com.github.nblair</groupId>
  <artifactId>spring-project-rest-api</artifactId>
  <version>0.3.0</version>
</dependency>
```

Next, if you want the Build API to return project information, you'll need the help of a few Maven plugins to generate the necessary properties. 
Create a file named `buildNumber.properties` in src/main/resources of your project, and copy the text from buildNumber-SAMPLE.properties.

Then, Add the following to your build plugins:

```
     <build>
     	<resources>
    		<resource>
        		<directory>${basedir}/src/main/resources</directory>
        		<filtering>true</filtering>
   	 		</resource>
		</resources>
        <plugins>
           ...
           <plugin>
                <groupId>pl.project13.maven</groupId>
                <artifactId>git-commit-id-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>revision</goal>
                         </goals>
                    </execution>
                </executions>
                <configuration>
                	<dotGitDirectory>${project.basedir}/../.git</dotGitDirectory>
               		<generateGitPropertiesFile>false</generateGitPropertiesFile>
    				<skip>false</skip>
                </configuration>
            </plugin>
            ...
         </plugins>
     </build>
```

Now that the dependency is installed, simply `@ComponentScan` the `com.github.nblair.project.rest` package in your web configuration.
You'll find a new API - /build - under your application's context.  