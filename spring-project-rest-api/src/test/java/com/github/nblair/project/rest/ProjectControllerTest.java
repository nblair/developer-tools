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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

/**
 * Tests for {@link ProjectController}.
 * 
 * @author Nicholas Blair
 */
public class ProjectControllerTest {

  /**
   * Verify stable behavior when all known build properties are missing.
   */
  @Test
  public void build_noPropertiesSet() {
    ProjectController controller = new ProjectController();
    Environment environment = new MockEnvironment();
    controller.setEnvironment(environment);
    
    Build build = controller.build();
    assertNull(build.getBuildNumber());
    assertNull(build.getProjectVersion());
    assertNull(build.getScmBranch());
    assertNull(build.getTimestamp());
    assertTrue(build.isDirty()); 
  }
  /**
   * Experiment for successful scenario representing a release.
   */
  @Test
  public void build_successful_release() {
    ProjectController controller = new ProjectController();
    Environment env = new MockEnvironment()
      .withProperty(ProjectController.PROJECT_VERSION, "1.2.3")
      .withProperty(ProjectController.GIT_TAGS, "foo-1.2.3")
      .withProperty(ProjectController.GIT_COMMIT_ID_ABBREV, "abcd123")
      .withProperty(ProjectController.GIT_DIRTY, "false");
    controller.setEnvironment(env);
    
    Build build = controller.build();
    assertEquals("abcd123", build.getBuildNumber());
    assertEquals("1.2.3", build.getProjectVersion());
    assertNull(build.getScmBranch());
    assertFalse(build.isDirty()); 
  }
  /**
   * Experiment for successful scenario representing a transient build.
   */
  @Test
  public void build_successful_snapshot() {
    ProjectController controller = new ProjectController();
    Environment env = new MockEnvironment()
      .withProperty(ProjectController.PROJECT_VERSION, "1.2.4-SNAPSHOT")
      .withProperty(ProjectController.GIT_BRANCH, "something/branchname")
      .withProperty(ProjectController.GIT_COMMIT_ID_ABBREV, "abcd123")
      .withProperty(ProjectController.GIT_DIRTY, "false");
    controller.setEnvironment(env);
    
    Build build = controller.build();
    assertEquals("abcd123", build.getBuildNumber());
    assertEquals("1.2.4-SNAPSHOT", build.getProjectVersion());
    assertEquals("something/branchname", build.getScmBranch());
    assertFalse(build.isDirty()); 
  }
  
 
}
