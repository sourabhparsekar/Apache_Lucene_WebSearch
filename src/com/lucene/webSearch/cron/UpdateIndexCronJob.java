package com.lucene.webSearch.cron;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.tcs.bits.lucene.UpdateInitialIndex;
import com.tcs.bits.manager.ApplicationManager;
import com.tcs.bits.utility.ApplicationConstants;
import com.tcs.bits.utility.FileHandling;

public class UpdateIndexCronJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		Path folder = Paths.get(ApplicationConstants.INPUT_FILE_DIRECTORY);
		if (ApplicationManager.isUpdateIndexCronEnabled()) {
			// Folder we are going to watch
			FileHandling.processAllModifiedFiles(folder.toString(), ApplicationManager.getUpdateIndexCronTime());
		} else {
			FileHandling.watchDirectoryPath(folder);
		}
	}
}
