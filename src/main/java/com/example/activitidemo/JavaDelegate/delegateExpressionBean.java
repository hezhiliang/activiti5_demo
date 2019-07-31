package com.example.activitidemo.JavaDelegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class delegateExpressionBean implements JavaDelegate{

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        execution.setVariable("delegateVar","流程变量");
    }
}
