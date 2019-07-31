package com.example.activitidemo.JavaDelegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 从配置文件中注入一个字符串用于写
 *
 */
public class ToUppercase implements JavaDelegate{

    private Expression text;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String value = (String) text.getValue(execution);
        execution.setVariable("input",value.toUpperCase());
    }
}
