package com.lucene.webSearch.utility;

import org.quartz.CronExpression;
//org.quartz.CronExpression.isValidExpression(expression);

public class Helper {

	public static Boolean isValidCronExpression(final String cronExpression) {
		if (CronExpression.isValidExpression(cronExpression)) {
			return true;
		}
		return false;
	}
	
}
