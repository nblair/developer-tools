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
package com.github.nblair.project.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean representing project build information.
 * 
 * @author Nicholas Blair
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Build {
  
  @XmlElement
  protected String buildNumber;
  @XmlElement
  protected String scmBranch;
  @XmlElement
  protected String projectVersion;
  @XmlElement
  protected String timestamp;
  protected boolean dirty;
  /**
   * 
   */
  public Build() {
  }
  /**
   * 
   * @param buildNumber the value to set for {@link #getBuildNumber()}.
   */
  public Build(String buildNumber) {
    this.buildNumber = buildNumber;
  }
  /**
   * @return the buildNumber
   */
  public String getBuildNumber() {
    return buildNumber;
  }
  /**
   * @param buildNumber the buildNumber to set
   */
  public void setBuildNumber(String buildNumber) {
    this.buildNumber = buildNumber;
  }
  /**
   * @return the scmBranch
   */
  public String getScmBranch() {
    return scmBranch;
  }
  /**
   * @param scmBranch the scmBranch to set
   */
  public void setScmBranch(String scmBranch) {
    this.scmBranch = scmBranch;
  }
  /**
   * @return the projectVersion
   */
  public String getProjectVersion() {
    return projectVersion;
  }
  /**
   * @param projectVersion the projectVersion to set
   */
  public void setProjectVersion(String projectVersion) {
    this.projectVersion = projectVersion;
  }
  /**
   * @return the timestamp
   */
  public String getTimestamp() {
    return timestamp;
  }
  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  /**
   * @return true if the build was produced from a locally modified working copy
   */
  public boolean isDirty() {
    return dirty;
  }
  /**
   * @param dirty the dirty to set
   */
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Build [buildNumber=");
    builder.append(buildNumber);
    builder.append(", scmBranch=");
    builder.append(scmBranch);
    builder.append(", projectVersion=");
    builder.append(projectVersion);
    builder.append(", timestamp=");
    builder.append(timestamp);
    builder.append("]");
    return builder.toString();
  }
}