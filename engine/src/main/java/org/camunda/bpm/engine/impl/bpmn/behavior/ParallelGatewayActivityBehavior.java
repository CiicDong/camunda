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
package org.camunda.bpm.engine.impl.bpmn.behavior;

import java.util.List;

import org.camunda.bpm.engine.impl.ProcessEngineLogger;
import org.camunda.bpm.engine.impl.pvm.PvmActivity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

/**
 * Implementation of the Parallel Gateway/AND gateway as definined in the BPMN
 * 2.0 specification.
 *
 * The Parallel Gateway can be used for splitting a path of execution into
 * multiple paths of executions (AND-split/fork behavior), one for every
 * outgoing sequence flow.
 *
 * The Parallel Gateway can also be used for merging or joining paths of
 * execution (AND-join). In this case, on every incoming sequence flow an
 * execution needs to arrive, before leaving the Parallel Gateway (and
 * potentially then doing the fork behavior in case of multiple outgoing
 * sequence flow).
 *
 * Note that there is a slight difference to spec (p. 436): "The parallel
 * gateway is activated if there is at least one Token on each incoming sequence
 * flow." We only check the number of incoming tokens to the number of sequenceflow.
 * So if two tokens would arrive through the same sequence flow, our implementation
 * would activate the gateway.
 *
 * Note that a Parallel Gateway having one incoming and multiple ougoing
 * sequence flow, is the same as having multiple outgoing sequence flow on a
 * given activity. However, a parallel gateway does NOT check conditions on the
 * outgoing sequence flow.
 *
 * @author Joram Barrez
 * @author Tom Baeyens
 */

/**
 * BPMN 2.0 规范中定义的并行网关和网关的实现。并行网关可用于将执行路径拆分为多个执行路径（AND-splitfork 行为），一个用于每个传出序列流。并行网关也可用于合并或加入执行路径（AND-join）。
 * 在这种情况下，在每个传入的序列流上，需要在离开并行网关之前到达执行（并且可能在多个传出序列流的情况下执行分叉行为）。
 * 请注意，与规范（第 436 页）略有不同：“如果每个传入序列流上至少有一个令牌，则激活并行网关。”我们只检查传入令牌的数量到序列流的数量。
 * 因此，如果两个令牌将通过相同的序列流到达，我们的实现将激活网关。
 * 请注意，具有一个传入和多个传出序列流的并行网关与在给定活动上具有多个传出序列流相同。但是，并行网关不会检查传出序列流的条件。
 *
 */
public class ParallelGatewayActivityBehavior extends GatewayActivityBehavior {

  protected static final BpmnBehaviorLogger LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;

  public void execute(ActivityExecution execution) throws Exception {

    //获取当前执行实例
    PvmActivity activity = execution.getActivity();
    //获取传出序列流列表
    List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
    //停用此执行。例如，这在连接中很有用：执行仍然存在，但不再处于活动状态(猜测可能是先更改执行的状态)
    execution.inactivate();
    //锁定该执行状态
    lockConcurrentRoot(execution);
    //查找所有的不活跃的并行执行实例
    List<ActivityExecution> joinedExecutions = execution.findInactiveConcurrentExecutions(activity);
    //传入序列流的列表
    int nbrOfExecutionsToJoin = execution.getActivity().getIncomingTransitions().size();
    //所有的不活跃的执行列表
    int nbrOfExecutionsJoined = joinedExecutions.size();
    //当传入序列表 == 所有非活跃的执行列表，传出网关
    if (nbrOfExecutionsJoined==nbrOfExecutionsToJoin) {

      // Fork
      LOG.activityActivation(activity.getId(), nbrOfExecutionsJoined, nbrOfExecutionsToJoin);
      execution.leaveActivityViaTransitions(outgoingTransitions, joinedExecutions);

    } else {
      LOG.noActivityActivation(activity.getId(), nbrOfExecutionsJoined, nbrOfExecutionsToJoin);
    }
  }

}
