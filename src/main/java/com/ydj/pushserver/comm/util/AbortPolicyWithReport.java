package com.ydj.pushserver.comm.util;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午04:53:37
 * @version : 1.0
 * @description :
 *
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

	private final String threadName;

	public AbortPolicyWithReport(String threadName) {
		this.threadName = threadName;
	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		String msg = String.format("Thread pool is EXHAUSTED!"
						+ " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d), Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s) !",
						threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(),
						e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(),
						e.isTerminated(), e.isTerminating());
		throw new RejectedExecutionException(msg);
	}

}