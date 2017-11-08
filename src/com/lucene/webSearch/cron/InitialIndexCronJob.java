package com.lucene.webSearch.cron;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.lucene.webSearch.lucene.CreateInitialIndex;

public class InitialIndexCronJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		CreateInitialIndex initialIndexCreator = new CreateInitialIndex();
		initialIndexCreator.execute();
		
	}

}
