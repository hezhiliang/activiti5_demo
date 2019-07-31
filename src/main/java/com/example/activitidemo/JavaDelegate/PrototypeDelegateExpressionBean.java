package com.example.activitidemo.JavaDelegate;

import java.util.concurrent.atomic.AtomicInteger;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

public class PrototypeDelegateExpressionBean implements JavaDelegate {

	  public static AtomicInteger INSTANCE_COUNT = new AtomicInteger(0);

	  private Expression fieldA;
	  private Expression fieldB;
	  private Expression resultVariableName;

	  public PrototypeDelegateExpressionBean() {
	    INSTANCE_COUNT.incrementAndGet();//加1
	  }

	  @Override
	  public void execute(DelegateExecution execution) throws Exception {

	    Number fieldAValue = (Number) fieldA.getValue(execution);
	    Number fieldValueB = (Number) fieldB.getValue(execution);

	    int result = fieldAValue.intValue() + fieldValueB.intValue();
	    execution.setVariable(resultVariableName.getValue(execution).toString(), result);
	  }

	}