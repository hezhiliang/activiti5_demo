package com.example.activitidemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestsActiviti5 {
	@Autowired
	private Gson gson;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ManagementService managementService;
	@Autowired
	private ProcessEngineConfigurationImpl processEngineConfiguration;

	public static void main(String[] args) {
		System.out.print("请输入任意字符:");
		Scanner in = new Scanner(System.in);
		String name = in.nextLine();
		System.out.println(name);
		// 读取1行，用nextLine,读取一个字符串（以空格作为分隔）用next(),读取一个整数，用nextInt(),读取一个浮点数，用nextDouble();
		// 使用Scanner输入时，需要在程序的最开始加上import java.util.*;
		// 当时用的类不是定义在基本java.lang包中时，一定要使用import将相应的包加载进来
	}



	/**
	 * 获取流程引擎。新建数据库表
	 */
	@Test
	public void newTable() {
		System.out.println("=======开始新建数据库表=============");
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println("=======新建数据库表完成=============");
	}

	/**
	 * 一、创建一次 部署(加载流程定义xml) 多次部署相对于流程版本升级
	 */
	@Test
	public void deployProcess() {
		// Deployment deployment = repositoryService.createDeployment()
		// .addClasspathResource("processes/简单的请假流程.bpmn")
		// .name("系统部署")
		// .category("请假流程")//流程类型
		// // .tenantId()//部署者id
		// .deploy();

		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("processes/员工请假--测试.bpmn").name("系统部署")
				.category("复杂的请假流程")// 流程类型
				// .tenantId()//部署者id
				.deploy();
		System.out.println("流程部署成功！" + deployment);// DeploymentEntity[id=37501,
													// name=系统部署]
	}

	/**
	 * 二、启动流程
	 */
	@Test
	public void startProcess() {
		// 1. createProcessDefinitionQuery():创建流程定义查询
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery();

		// 2. query.processDefinitionKey()：查询流程定义指定的key
		ProcessDefinition result = query.processDefinitionKey("demo1")
				.latestVersion() // 最新版本
				.singleResult();// 单个结果
		System.out.println("流程定义为============>" + result);// myProcess_1:2:2504

		// 3. 创建一个启动流程，启动指定id的流程实例
		runtimeService.startProcessInstanceById(result.getId());
		System.out.println("流程id为===========>" + result.getId() + " 的流程启动完毕！");

	}

	/**
	 * 三、查询的API使用
	 */
	@Test
	public void selectProcess() {
		/**
		 * repositoryService.createProcessDefinitionQuery()
		 */
		// 1. createProcessDefinitionQuery():流程定义
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery();

		// deploymentId()：部署id
		// singleResult()：单个结果
		/* 根据 deployment表的ID_ 获取单个流程定义 */
		ProcessDefinition result = query.deploymentId("37501").singleResult();
		System.out.println("===根据 deployment 表的ID_ 获取单个流程定义===");
		System.out.println("流程定义为===>" + result);// ProcessDefinitionEntity[demo1:2:37504]

		/* 根据流程定义id获取所有流程定义 */
		List<ProcessDefinition> demo1 = repositoryService
				.createProcessDefinitionQuery().processDefinitionKey("demo1")
				.list();
		System.out.println("====流程定义id为[demo1]的所有流程定义为====");
		for (ProcessDefinition processDefinition : demo1) {
			System.out.println("流程定义为===>" + processDefinition);
		}

		/* 获取所有流程定义 */
		List<ProcessDefinition> processDefinitionList = repositoryService
				.createProcessDefinitionQuery().list();
		System.out.println("=====获取所有流程定义======");
		for (ProcessDefinition processDefinition : processDefinitionList) {
			System.out.println("流程定义为===>" + processDefinition);
		}
		repositoryService.createProcessDefinitionQuery().suspended();

		/* 获取任务列表 */
		List<Task> taskList = taskService.createTaskQuery().list();
		System.out.println("=====获取所有任务列表======");
		for (Task task : taskList) {
			System.out.println("任务为===>" + task);
		}

		/* 获取 历史流程实例 列表 */
		List<HistoricProcessInstance> historicProcessInstanceList = historyService
				.createHistoricProcessInstanceQuery().list();
		System.out.println("=====获取 历史流程实例 列表=====");
		for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
			System.out.println("历史流程实例为===>" + historicProcessInstance);
		}

		/* 使用本机查询，即自定义sql查询 */
		System.out.println("表名为：===>"
				+ managementService.getTableName(Task.class));
		List<Task> tasks = taskService
				.createNativeTaskQuery()
				.sql("SELECT * FROM "
						+ managementService.getTableName(Task.class)
						+ " T WHERE T.NAME_ = #{taskName}")
				.parameter("taskName", "审批").list();

		System.out.println("流程名“审判”的任务数量有：" + tasks.size());
		for (Task task : tasks) {
			System.out.println("任务为===>" + task);
		}
		// managementService.getEventLogEntries()

		// long count = taskService.createNativeTaskQuery()
		// .sql("SELECT count(*) FROM " +
		// managementService.getTableName(Task.class) + " T1, "
		// + managementService.getTableName(VariableInstanceEntity.class) +
		// " V1 WHERE V1.TASK_ID_ = T1.ID_")
		// .count();

	}

	/**
	 * 四、任务签收
	 */
	@Test
	public void taskClaim() {
		// 1， 获取任务
		// List<Task> list =
		// taskService.createTaskQuery().processDefinitionKey("demo1").list();
		// 根据流程定义id 查
		// List<Task> list =
		// taskService.createTaskQuery().taskId("40012").list(); 根据 任务id查
		List<Task> list = taskService.createTaskQuery()
				.processInstanceId("57501").list();// 根据 流程定义id 查
		System.out.println("有" + list.size() + "个任务！");
		// 遍历
		for (Task task : list) { // 已绑定签收人
			System.out.println("任务为=======>" + task);
			System.out.println(task + "的绑定人为===>" + task.getAssignee());
			if (StringUtils.isNotBlank(task.getAssignee())) {

			} else { // 未绑定签收人
				// claim：签收任务
				taskService.claim(task.getId(), "令狐冲");
				Task task1 = taskService.createTaskQuery()
						.processInstanceId("57501").singleResult();
				System.out.println(task1 + "的绑定人为===>" + task1.getAssignee());
			}

			// 3.2 unclaim：传入id,解除绑定。
			// System.out.println(task.getAssignee() + " 解除了 " +
			// task.getName());
			// taskService.unclaim(task.getId());

			// 4. complete：完成任务
			taskService.complete(task.getId());
			System.out.println("任务完成");

			/* 投掷信号事件 */
			runtimeService.signal("alertSignal");// 信号说明
			runtimeService.signalEventReceived("alert1");// 将信号全局抛出到所有预订处理程序
			runtimeService.signalEventReceived("alert2", "alertSignal");// 仅将信号传递给特定执行
		}
	}

	/**
	 * 五、历史查询
	 */
	@Test
	public void history() {
		// 1. 获取流程引擎
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		// 2. 获取 historyService 历史流程实例
		HistoryService historyService = processEngine.getHistoryService();
		// createHistoricProcessInstanceQuery()：查询历史流程实例
		// finished：完结的流程
		// list：集合
		List<HistoricProcessInstance> hiss = historyService
				.createHistoricProcessInstanceQuery().finished().list();

		for (HistoricProcessInstance his : hiss) {
			// 之前时哪次部署产生的流程定义，从而启动的流程实例
			System.out.println("获取部署id ===>" + his.getDeploymentId());
			System.out.println("获取流程开始时间 ===>" + his.getStartTime());
			System.out.println("获取流程结束时间 ===>" + his.getEndTime());
			System.out.println("获取流程定义id ===>" + his.getProcessDefinitionId());
			System.out.println("获取流程id ===>" + his.getId());

			System.out.println();

		}

		// 查询历史任务
		List<HistoricTaskInstance> hisTasks = historyService
				.createHistoricTaskInstanceQuery().finished().list();

		for (HistoricTaskInstance hisTask : hisTasks) {

			System.out.print("任务id：" + hisTask.getId() + "\t");
			System.out.print("任务名：" + hisTask.getName() + "\t");
			System.out.print("任务签收者：" + hisTask.getAssignee() + "\t");
			System.out.print("任务开始时间：" + hisTask.getStartTime() + "\t");
			System.out.print("任务结束时间：" + hisTask.getEndTime() + "\t");

			System.out.println();
		}

	}
}