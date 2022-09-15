/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.pvm.delegate;

import org.camunda.bpm.engine.impl.core.delegate.CoreActivityBehavior;

//Activiti提供了接口org.activiti.engine.impl.pvm.delegate.ActivityBehavior。该接口内部仅有一个execute方法。
// 该接口的实现即为不同PvmActivity节点提供了具体动作。ActivityBehavior有丰富的不同实现，对应了流程中丰富的不同功能的节点。每一个PvmActivity对象都会持有一个ActivityBehavior对象。
/**
 * @author Tom Baeyens
 */
public interface ActivityBehavior extends CoreActivityBehavior<ActivityExecution> {

  void execute(ActivityExecution execution) throws Exception;
}
