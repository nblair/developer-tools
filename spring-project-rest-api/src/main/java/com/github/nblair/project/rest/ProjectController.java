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
/**
 * 
 */
package com.github.nblair.project.rest;


import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;

/**
 * {@link Controller} providing a REST API for project information.
 * 
 * Expects the {@link Environment} to be injected and provide a number of properties.
 * Here is an example of the properties you'll need:
 * <pre>
# provided via Maven resource filtering
projectVersion=${project.version}
# provided by the maven-git-commit-id-plugin: https://github.com/ktoso/maven-git-commit-id-plugin
git.tags=${git.tags}
git.branch=${git.branch}
git.commit.id.abbrev=${git.commit.id.abbrev}
git.dirty=${git.dirty}
git.build.time=${git.build.time}
 </pre>
 * 
 * @author Nicholas Blair
 */
@Controller
public class ProjectController {

  public static final String GIT_BUILD_TIME = "git.build.time";
  public static final String GIT_BRANCH = "git.branch";
  public static final String GIT_COMMIT_ID_ABBREV = "git.commit.id.abbrev";
  public static final String GIT_DIRTY = "git.dirty";
  public static final String GIT_TAGS = "git.tags";
  public static final String PROJECT_VERSION = "projectVersion";
  
  @Inject
  private Environment environment;
  /**
   * Visible for testing.
   * 
   * @param environment the environment to set
   */
  void setEnvironment(Environment environment) {
    this.environment = environment;
  }
  /**
   * 
   * @return the current {@link Build} information
   */
  @ApiOperation(value="Build information", notes="Retrieve project build information.")
  @RequestMapping(value="/build", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public @ResponseBody Build build() {
    Build build = new Build(environment.getProperty(GIT_COMMIT_ID_ABBREV));
    if(StringUtils.isEmpty(environment.getProperty(GIT_TAGS))) {
      build.setScmBranch(environment.getProperty(GIT_BRANCH));
    }
    build.setProjectVersion(environment.getProperty(PROJECT_VERSION));
    build.setTimestamp(environment.getProperty(GIT_BUILD_TIME));
    // default dirty field to 'true' if property isn't available
    build.setDirty(Boolean.parseBoolean(environment.getProperty(GIT_DIRTY, "true")));
    return build;
  }
  /**
   * 
   * @return the current {@link Environment#getActiveProfiles()}
   */
  @ApiOperation(value="Active Profiles", notes="Retrieve active profiles for the environment.")
  @RequestMapping(value="/activeProfiles", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public @ResponseBody String[] activeProfiles() {
   return environment.getActiveProfiles();
  }
}
