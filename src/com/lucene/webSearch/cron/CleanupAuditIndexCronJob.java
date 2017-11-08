package com.lucene.webSearch.cron;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.lucene.webSearch.manager.DBManager;

public class CleanupAuditIndexCronJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		Boolean status = DBManager.CleanUpAuditTable();
		System.out.println("Job Completed with Status :: "+status);
	}

}
