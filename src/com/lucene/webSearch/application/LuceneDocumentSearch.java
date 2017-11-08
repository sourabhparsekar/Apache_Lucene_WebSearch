package com.lucene.webSearch.application;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.lucene.webSearch.cron.CleanupAuditIndexCronJob;
import com.lucene.webSearch.cron.InitialIndexCronJob;
import com.lucene.webSearch.cron.UpdateIndexCronJob;
import com.lucene.webSearch.manager.ApplicationManager;
import com.lucene.webSearch.utility.ApplicationException;
import com.lucene.webSearch.utility.PropertyLoader;


public class LuceneDocumentSearch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LuceneDocumentSearch.execute();
	}
	
	public static void execute() {
		
		try {
			PropertyLoader.initialisePropertyLoader();

			if (ApplicationManager.isFirstIndexEnabled() || ApplicationManager.isFirstIndexUpdateOnStartUpEnabled()) {
				// Job 1 :: create initial index or update index on startup.
				Trigger initialIndexTrigger = TriggerBuilder.newTrigger().withIdentity("Initial_or_Startup_Index")
						.startNow().build();
				// start Trigger immediately after application is started or
				// restarted.

				JobDetail initialIndexJob = JobBuilder.newJob(InitialIndexCronJob.class).build();
				// create the Job with Job Class

				Scheduler initialIndexSchedular = StdSchedulerFactory.getDefaultScheduler();
				// create a scheduler that runs the job with the configured
				// trigger
				initialIndexSchedular.start();
				initialIndexSchedular.scheduleJob(initialIndexJob, initialIndexTrigger);

				System.out.println("Job 1 to create Initial Index or Update Initial Index completed successfully.");
			}

			if (ApplicationManager.isUpdateIndexEnabled()) {
				// Job 2 :: create initial index or update index on startup.
				Trigger updateIndexTrigger = null;
				if (ApplicationManager.isUpdateIndexCronEnabled())
					updateIndexTrigger = TriggerBuilder.newTrigger().withIdentity("Update_Index")
							.withSchedule(
									CronScheduleBuilder.cronSchedule(ApplicationManager.getUpdateIndexCronExpression()))
							.build();
				// start the index with cro expression. This is pending
				else
					updateIndexTrigger = TriggerBuilder.newTrigger().withIdentity("Update_Index").startNow().build();
				// start Trigger immediately after application is started or
				// restarted.

				// start Trigger immediately after application is started or
				// restarted.

				JobDetail updateIndexJob = JobBuilder.newJob(UpdateIndexCronJob.class).build();
				// create the Job with Job Class

				Scheduler updateIndexSchedular = new StdSchedulerFactory().getScheduler();
				// create a scheduler that runs the job with the configured
				// trigger
				updateIndexSchedular.start();
				updateIndexSchedular.scheduleJob(updateIndexJob, updateIndexTrigger);

				System.out.println("Job 2 to update Initial Index or Update Initial Index completed successfully.");

			}


			if (ApplicationManager.isDBCleanUpEnabled()) {
				// Job 3 :: DB CleanUp for Lucenen Audit Tracking
				Trigger dbIndexCleanupTrigger = TriggerBuilder.newTrigger().withIdentity("Cleanup_Audit_Index")
							.withSchedule(
									CronScheduleBuilder.cronSchedule(ApplicationManager.getCleanUpTableCronExpression()))
							.build();
				// start the index with cron expression. This is pending

				JobDetail dbIndexCleanupJob = JobBuilder.newJob(CleanupAuditIndexCronJob.class).build();
				// create the Job with Job Class

				Scheduler dbIndexCleanupSchedular = new StdSchedulerFactory().getScheduler();
				// create a scheduler that runs the job with the configured
				// trigger
				dbIndexCleanupSchedular.start();
				dbIndexCleanupSchedular.scheduleJob(dbIndexCleanupJob, dbIndexCleanupTrigger);

				System.out.println("Job 3 to do Audit Table Cleanup completed successfully.");

			}


			
			
			// System.out.print(PropertyLoader.PROPERTIES.getProperty("UpdateIndexCron"));

		} catch (ApplicationException e) {
			System.err.println("Application Exception occured with error :: " + e.getMessage());
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (SchedulerException e) {
			System.err.println("Scheduler Exception occured with error :: " + e.getMessage());
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception occured with error :: " + e.getMessage());
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

}
