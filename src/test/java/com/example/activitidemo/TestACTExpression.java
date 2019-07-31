package com.example.activitidemo;

import java.util.List;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestACTExpression {
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

	/**
	 * 一、创建一次 部署(加载流程定义xml) 部署是可以多次的
	 */
	@Test
	public void deployProcess() {
		// 获取流程引擎
		// 拿到repositoryService仓库服务
		// 创建一次部署
		Deployment deploy = repositoryService.createDeployment()
				// 添加类路径下的流程图
				.addClasspathResource("diagrams/serviceTaskLC.bpmn")
				.name("ServiceTack")
				// 部署
				.deploy();
		System.out.println("部署流程实例成功!");
		System.out.println(deploy);
	}

	@Test
	// @org.activiti.engine.test.Deployment
	public void testDelegateExpression() {

		this.deployProcess();// 部署

		// 开始
		ProcessInstance procInst = runtimeService.startProcessInstanceByKey("serviceTaskLC");
		System.out.println("流程实例开启完成" + procInst);

		assertEquals(true, procInst.isEnded());// 判断流程是否结束

		List<Task> taskList = taskService.createTaskQuery().list();
		for (Task task : taskList) {
			assertEquals("My Task", task.getName());
			this.isEnd(task.getId());
		}
		
		// taskService.complete(task.getId());
		assertEquals(0, runtimeService.createProcessInstanceQuery().count());
		
		
	}

	/* 检测是否流程已经结束 */
	public void isEnd(String procInId) {
		HistoricProcessInstance historicProcessInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceId(procInId).singleResult();
		System.out.println("=======>Process instance end time: "
				+ historicProcessInstance.getEndTime());
	}
}
