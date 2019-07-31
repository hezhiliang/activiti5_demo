package com.example.activitidemo;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailTests {
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


	@Test
	public void test() {
		Deployment deploy = repositoryService.createDeployment()
				.addClasspathResource("diagrams/发送邮件.bpmn")
				.name("测试邮件任务")
				.category("测试")
				.deploy();
		System.out.println(deploy);
		ProcessDefinition singleResult = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey("emailTask").latestVersion()
				.singleResult();

		runtimeService.startProcessInstanceById(singleResult.getId());
		System.out.println("ok");
	}
}
