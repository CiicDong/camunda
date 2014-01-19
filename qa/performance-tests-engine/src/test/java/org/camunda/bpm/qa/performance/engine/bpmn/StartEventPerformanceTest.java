/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.qa.performance.engine.bpmn;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.qa.performance.engine.ProcessEnginePerformanceTestBase;
import org.camunda.bpm.qa.performance.engine.framework.steps.StartProcessInstanceByMessageStep;
import org.camunda.bpm.qa.performance.engine.framework.steps.StartProcessInstanceStep;
import org.junit.Test;

/**
 * @author Daniel Meyer
 *
 */
public class StartEventPerformanceTest extends ProcessEnginePerformanceTestBase {

  @Test
  @Deployment
  public void noneStartEvent() {

    perfomanceTest()
      .step(new StartProcessInstanceStep(engine, "process"))
    .run();

  }

  @Test
  @Deployment
  public void messageStartEvent() {

    perfomanceTest()
      .step(new StartProcessInstanceByMessageStep(engine, "message"))
    .run();

  }

}
