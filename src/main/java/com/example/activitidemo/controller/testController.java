package com.example.activitidemo.controller;

import com.google.gson.Gson;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class testController {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private Gson gson;

    @GetMapping("/hello")
    public String test() {
        return "Hello World!";
    }

    /**
     * 部署流程
     * @param deployName  流程文件名
     * @param
     * @return
     */
    @PostMapping("/deploy")
    public String deploy(@RequestParam("deployName") String deployName){
        //1、根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/" + deployName)//processes/简单的请假流程.bpmn
                .name("系统部署")
//                .category("请假流程")//流程类型
//                .tenantId()//部署者id
                .deploy();
        return "[" + deployName + "]流程定义部署成功!";
    }
//    /**
//     * 获取 流程定义
//     */
//    @GetMapping
//    public  void getProcessDefinition(){
//        //2、获取流程定义
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//                .deploymentId(deployment.getId())
//                .singleResult();
//    }

    /**
     * 部署流程 --> 获取流程定义 --> 启动流程 --> 签收(可不签收) --> 获取任务 --> 完成任务
     */
    @GetMapping("/demoProject1")
    public void demoProject1() {
        //1、根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/简单的请假流程.bpmn")
                .name("系统部署")
                .category("请假流程")//流程类型
//                .tenantId()//部署者id
                .deploy();

        //2、获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        //3、启动流程定义，返回流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());
        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID：" + processId);

        //4、获取 任务
        Task task = taskService.createTaskQuery()//创建任务查询
                .processInstanceId(processId)//根据流程实例id查
                .singleResult();//获取单个结果

//        //5. 签收任务
//        taskService.claim(task.getId(),"张三");
//        System.out.println(task.getAssignee() + "签收了任务");

        System.out.println("第一次执行前，任务名称：" + task.getName());
        //6. 完成 任务
        taskService.complete(task.getId());

//        taskService.claim(task.getId(),"李四");
//        System.out.println(task.getAssignee() + "签收了任务");

        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("第二次执行前，任务名称：" + task.getName());
        taskService.complete(task.getId());
//        taskService.claim(task.getId(),"王五");
//        System.out.println(task.getAssignee() + "签收了任务");

        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("task为null，任务执行完毕：" + task);
    }



}
