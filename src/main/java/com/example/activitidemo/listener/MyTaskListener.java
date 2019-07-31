package com.example.activitidemo.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
/**
 * 通过任务监听器实现 任务用户分配
 * create by hzl 2019/1/23
 * @author x1c
 *
 */
public class MyTaskListener implements TaskListener{

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		delegateTask.addCandidateUser("张三3");
		delegateTask.addCandidateUser("李四3");
		delegateTask.addCandidateUser("王五3");
		
	}

}
