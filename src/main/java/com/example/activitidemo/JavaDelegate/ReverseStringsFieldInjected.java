package com.example.activitidemo.JavaDelegate;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 从配置文件注入多个属性类型必须为Expression
 * 实现 JavaDelegate 委托类，重写execute 方法，去设置流程变量
 */
public class ReverseStringsFieldInjected implements JavaDelegate {

    private Expression text1;
    private Expression text2;

    public void execute(DelegateExecution execution) {
        String value1 = (String) text1.getValue(execution);
        execution.setVariable("var1", new StringBuffer(value1).toString());//reverse-反转
        
        String value2 = (String) text2.getValue(execution);
        execution.setVariable("var2", new StringBuffer(value2).toString());
    }
}