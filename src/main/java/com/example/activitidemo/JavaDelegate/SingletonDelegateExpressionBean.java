package com.example.activitidemo.JavaDelegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateHelper;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.concurrent.atomic.AtomicInteger;

public class SingletonDelegateExpressionBean implements JavaDelegate {

    public static AtomicInteger INSTANCE_COUNT = new AtomicInteger(0);

    public SingletonDelegateExpressionBean() {
        INSTANCE_COUNT.incrementAndGet();
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Expression fieldAExpression = DelegateHelper.getFieldExpression(execution, "fieldA");
        Number fieldA = (Number) fieldAExpression.getValue(execution);

        Expression fieldBExpression = DelegateHelper.getFieldExpression(execution, "fieldB");
        Number fieldB = (Number) fieldBExpression.getValue(execution);

        int result = fieldA.intValue() + fieldB.intValue();

        String resultVariableName = DelegateHelper.getFieldExpression(execution, "resultVariableName").getValue(execution).toString();
        execution.setVariable(resultVariableName, result);
    }

}