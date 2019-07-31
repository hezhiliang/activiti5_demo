package com.example.activitidemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestsACTUserAndGroup {
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

	/**
	 * 分配任务组方法一：
	 * 在流程中写死“张三,李四,王五”
	 */
	@Test
	public void findTask1() {
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("diagrams/测试分配任务组.bpmn").name("测试分配任务组")
				.category("测试")// 流程类型
				// .tenantId()//部署者id
				.deploy();

		System.out.println("流程部署成功！" + deployment);// DeploymentEntity[id=117501,
													// name=测试分配任务组]

		/* 获取流程定义id */
		String procId = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("groupTask1").latestVersion()
				.singleResult().getId();
		System.out.println("立案流程定义id为：" + procId);// groupTask1:1:117504
		/* 启动流程 */
		runtimeService.startProcessInstanceById(procId);
		System.out.println("流程id为===========>" + procId + " 的流程启动完毕！");

		List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
				// .taskAssignee("李四") // 指定委派人
				.taskCandidateUser("张三") // 候选人查询
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());// 任务ID:117508
			System.out.println("任务名称:" + task.getName());// 任务名称:审批
			System.out.println("任务创建时间:" + task.getCreateTime());// 任务创建时间:Tue
																	// Jan 22
																	// 20:44:00
																	// CST 2019
			System.out.println("任务委派人:" + task.getAssignee());// 任务委派人:null
			System.out.println("流程实例ID:" + task.getProcessInstanceId());// 流程实例ID:117505
		}
	}
	
	

	/**
	 * 分配任务组方法二：
	 * 在流程中用流程变量 ${userIds}
	 */
	@Test
	public void findTask2() {
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("diagrams/测试分配任务组2.bpmn").name("测试分配任务组")
				.category("测试")// 流程类型
				// .tenantId()//部署者id
				.deploy();

		System.out.println("流程部署成功！" + deployment);//DeploymentEntity[id=120001, name=测试分配任务组]

		/* 获取流程定义id */
		String procId = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("groupTask2").latestVersion()
				.singleResult().getId();
		System.out.println("立案流程定义id为：" + procId);//groupTask2:1:120004
		
		/* 启动流程 */
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userIds", "张三,李四,王五");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("groupTask2", variables); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());//流程实例ID:120005
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId());//流程定义ID:groupTask2:1:120004

		List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
				// .taskAssignee("李四") // 指定委派人
				.taskCandidateUser("张三") // 候选人查询
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());// 任务ID:120010
			System.out.println("任务名称:" + task.getName());// 任务名称:审批
			System.out.println("任务创建时间:" + task.getCreateTime());//任务创建时间:Tue Jan 22 20:58:16 CST 2019
			System.out.println("任务委派人:" + task.getAssignee());// 任务委派人:null
			System.out.println("流程实例ID:" + task.getProcessInstanceId());// 流程实例ID:120005
		}
	}
	
	/**
	 * 分配任务组方法三：
	 * 任务监听器实现 任务分配
	 */
	@Test
	public void findTask3() {
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("diagrams/测试分配任务组3.bpmn").name("测试分配任务组")
				.category("测试")// 流程类型
				// .tenantId()//部署者id
				.deploy();
		
		System.out.println("流程部署成功！" + deployment);//DeploymentEntity[id=122501, name=测试分配任务组]
		
		/* 获取流程定义id */
		String procId = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("groupTask3").latestVersion()
				.singleResult().getId();
		System.out.println("立案流程定义id为：" + procId);//groupTask3:1:122504
		
		/* 启动流程 */
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("groupTask3"); // 流程定义表的KEY字段值
		System.out.println("流程实例ID:" + pi.getId());//流程实例ID:122505
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId());//流程定义ID:groupTask3:1:122504
		
		
		List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
				// .taskAssignee("李四") // 指定委派人
				.taskCandidateUser("张三") // 候选人查询
				.list();
		for (Task task : taskList) {
			System.out.println("任务ID:" + task.getId());// 任务ID:120010
			System.out.println("任务名称:" + task.getName());// 任务名称:审批
			System.out.println("任务创建时间:" + task.getCreateTime());//任务创建时间:Tue Jan 22 20:58:16 CST 2019
			System.out.println("任务委派人:" + task.getAssignee());// 任务委派人:null
			System.out.println("流程实例ID:" + task.getProcessInstanceId());// 流程实例ID:120005
		}
	}
	
	
	
	
}