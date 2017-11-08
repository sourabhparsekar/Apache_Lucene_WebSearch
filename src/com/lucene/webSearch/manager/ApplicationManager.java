package com.lucene.webSearch.manager;

import org.quartz.CronExpression;

import com.lucene.webSearch.utility.ApplicationConstants;

public class ApplicationManager {

	/**
	 * #Below parameter is used to enable Initial Index first time when
	 * application is run FirstIndexEnabled=true
	 * 
	 * @return true/false
	 */
	public static Boolean isFirstIndexEnabled() {
		if (ApplicationConstants.FIRST_INDEX_ENABLED != null
				&& (ApplicationConstants.FIRST_INDEX_ENABLED.trim()).equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}

	/**
	 * #Below method is used to check if Initial Index is to be refreshed when
	 * application is re-run FirstIndexUpdateOnStartUp=false/true
	 * 
	 * @return true/false
	 */
	public static Boolean isFirstIndexUpdateOnStartUpEnabled() {
		if (ApplicationConstants.FIRST_INDEX_UPDATE != null
				&& (ApplicationConstants.FIRST_INDEX_UPDATE.trim()).equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}

	/**
	 * #Below method is used to enable CRON expression to Update existing Index
	 * File when new File is added to input files UpdateIndexEnabled=true
	 * 
	 * @return true/false
	 */
	public static Boolean isUpdateIndexEnabled() {
		if (ApplicationConstants.UPDATE_INDEX_ENABLED != null
				&& (ApplicationConstants.UPDATE_INDEX_ENABLED.trim()).equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}

	/**
	 * #Below method is used to enable CRON expression to Update existing Index
	 * File UpdateIndexCronEnabled=false
	 * 
	 * @return true/false
	 */
	public static Boolean isUpdateIndexCronEnabled() {
		if (ApplicationConstants.UPDATE_INDEX_CRON_ENABLED != null
				&& (ApplicationConstants.UPDATE_INDEX_CRON_ENABLED.trim()).equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}

	/**
	 * #Below parameter is used to get User specified CRON expression to Update
	 * existing Index File else default is returned which is set to 00:01 AM
	 * everyday. Default UpdateIndexCron=0 1 0 1/1 * ? *
	 * 
	 * @return CRON_Expression
	 */
	public static String getUpdateIndexCronExpression() {
		if (isUpdateIndexCronEnabled() && CronExpression.isValidExpression(ApplicationConstants.UPDATE_INDEX_CRON.trim())) {
			return ApplicationConstants.UPDATE_INDEX_CRON.trim();
		}
		return "0 1 0 1/1 * ? *"; // default expression
	}

	/**
	 * #Below parameter is used to get User specified File Modified Time to Update
	 * existing Index File else default is returned which is set to 60 minutes
	 * everyday. UpdateIndexCronMinutes=1
	 * 
	 * @return Time in Minutes
	 */
	public static int getUpdateIndexCronTime() {
		if (isUpdateIndexCronEnabled()) {
			return Integer.parseInt(ApplicationConstants.UPDATE_INDEX_CRON_MINUTES.trim());
		}
		return 60; // default expression
	}

	
	/**
	 * #Below method is used to enable database tracking of index
	 * create/update/cleanup/etc LuceneTableEnabled=true
	 * 
	 * @return true/false
	 */
	public static Boolean isDBTrackingEnabled() {
		if (ApplicationConstants.LUCENE_TABLE_ENABLED != null
				&& (ApplicationConstants.LUCENE_TABLE_ENABLED.trim()).equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}


	/**
	 * #Below method used to check if Lucene db table cleanup is enabled 
	 * LuceneTableCleanUpEnabled=false
	 * 
	 * @return true/false
	 */
	public static Boolean isDBCleanUpEnabled() {
		if (ApplicationConstants.LUCENE_TABLE_CLEANUP_ENABLED != null
				&& (ApplicationConstants.LUCENE_TABLE_CLEANUP_ENABLED.trim()).equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}

	/**
	 * #Below parameter is used used to enable CRON expression to Cleanup query.
	 * Default is 1st of every month. Default UpdateIndexCron=0 0 12 1 1/1 ? *
	 * 
	 * @return CRON_Expression
	 */
	public static String getCleanUpTableCronExpression() {
		if (isDBCleanUpEnabled() && CronExpression.isValidExpression(ApplicationConstants.LUCENE_TABLE_CLEANUP_ENABLED.trim())) {
			return ApplicationConstants.LUCENE_TABLE_CLEANUP_CRON.trim();
		}
		return "0 0 12 1 1/1 ? *"; // default expression
	}

	/**
	 * #Below parameter used to enable Search Tag Tracking SearchTagEnabled=true
	 * 
	 * @return true/false
	 */
	public static Boolean isSearchTagTrackingEnabled() {
		if (ApplicationConstants.SEARCH_TAG_ENABLED != null
				&& (ApplicationConstants.SEARCH_TAG_ENABLED.trim()).equalsIgnoreCase("TRUE")) {
			return true;
		}
		return false;
	}

}
