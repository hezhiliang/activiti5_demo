package com.example.activitidemo.service.Impl;

import com.example.activitidemo.service.ActivityDemoService;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityDemoServiceImpl implements ActivityDemoService{
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;
    @Autowired
    private ManagementService managementService;

    /**
     * 启动流程
     * @param bizId 业务id
     */
    public void startProcesses(String bizId) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("demo5", bizId);//流程图id，业务表id
        System.out.println("流程启动成功，流程id:"+pi.getId());
    }


    @Override
    public void getText() {
        System.out.println("模拟调用spring bean中的方法成功！");
    }
}