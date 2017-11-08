package com.lucene.webSearch.utility;

public interface ApplicationConstants {

	String CREATE_INITIAL_INDEX = "CREATE/RE-CREATE INITIAL INDEX";
	String UPDATE_INITIAL_INDEX = "UPDATE INITIAL INDEX";

	String FIRST_INDEX_ENABLED = PropertyLoader.PROPERTIES.getProperty("FirstIndexEnabled");
	String FIRST_INDEX_UPDATE = PropertyLoader.PROPERTIES.getProperty("FirstIndexUpdateOnStartUp");

	String UPDATE_INDEX_ENABLED = PropertyLoader.PROPERTIES.getProperty("UpdateIndexEnabled");
	String UPDATE_INDEX_CRON_ENABLED = PropertyLoader.PROPERTIES.getProperty("UpdateIndexCronEnabled");
	String UPDATE_INDEX_CRON = PropertyLoader.PROPERTIES.getProperty("UpdateIndexCron");
	String UPDATE_INDEX_CRON_MINUTES = PropertyLoader.PROPERTIES.getProperty("UpdateIndexCronMinutes");

	String INPUT_FILE_DIRECTORY = PropertyLoader.PROPERTIES.getProperty("InputFileDirectory");
	String INDEX_OUTPUT_FILE_DIRECTORY = PropertyLoader.PROPERTIES.getProperty("IndexOutputFileDirectory");

	String LUCENE_TABLE_ENABLED = PropertyLoader.PROPERTIES.getProperty("LuceneTableEnabled");
	String LUCENE_TABLE_CLEANUP_ENABLED = PropertyLoader.PROPERTIES.getProperty("LuceneTableCleanUpEnabled");
	String LUCENE_TABLE_CLEANUP_QUERY = PropertyLoader.PROPERTIES.getProperty("LuceneTableCleanUpQuery");
	String LUCENE_TABLE_CLEANUP_CRON = PropertyLoader.PROPERTIES.getProperty("LuceneTableCleanUpCron");
	String LUCENE_TABLE_NAME = PropertyLoader.PROPERTIES.getProperty("LuceneIndexTableName");
	String LUCENE_TABLE_METHOD_COL = PropertyLoader.PROPERTIES.getProperty("LuceneIndexTableMethodNameColumn");
	// String LUCENE_TABLE_DATE_COL =
	// PropertyLoader.PROPERTIES.getProperty("LuceneIndexTableDateColumn");
	// String LUCENE_TABLE_TIME_COL =
	// PropertyLoader.PROPERTIES.getProperty("LuceneIndexTableTimeColumn");
	String LUCENE_TABLE_DATA_COL = PropertyLoader.PROPERTIES.getProperty("LuceneIndexTableDataColumn");
	String LUCENE_TABLE_COMMENTS_COL = PropertyLoader.PROPERTIES.getProperty("LuceneIndexTableCommentsColumn");

	String LUCENE_INSERT_QUERY = "INSERT INTO " + LUCENE_TABLE_NAME + "(" + LUCENE_TABLE_METHOD_COL.trim() + ","
			+ LUCENE_TABLE_DATA_COL + "," + LUCENE_TABLE_COMMENTS_COL + ") VALUES (?,?,?)";
	String LUCENE_CREATE_QUERY = "";

	String SEARCH_TAG_ENABLED = PropertyLoader.PROPERTIES.getProperty("SearchTagEnabled");
	String SEARCH_TAG_TABLE_NAME = PropertyLoader.PROPERTIES.getProperty("SearchTagTableName");
	String SEARCH_TAG_COLUMN = PropertyLoader.PROPERTIES.getProperty("SearchTagColumnName");
	String SEARCH_TAG_COUNTER = PropertyLoader.PROPERTIES.getProperty("SearchTagIntCounterName");
	String SEARCH_TAG_RETURN_RESULTS = PropertyLoader.PROPERTIES.getProperty("SearchTagReturnResults"); 
	String SEARCH_TAG_SELECT_QUERY = "SELECT COUNT(*) FROM "+SEARCH_TAG_TABLE_NAME+" WHERE LOWER("+SEARCH_TAG_COLUMN+") LIKE LOWER(?)";
	String SEARCH_TAG_UPDATE_QUERY = "UPDATE "+SEARCH_TAG_TABLE_NAME+" SET "+SEARCH_TAG_COUNTER+" = 1 + "+SEARCH_TAG_COUNTER+" WHERE LOWER("+SEARCH_TAG_COLUMN+") LIKE LOWER(?)";
	String SEARCH_TAG_INSERT_QUERY = "INSERT INTO "+SEARCH_TAG_TABLE_NAME+"("+SEARCH_TAG_COLUMN+") VALUES (?)";
	String SEARCH_TAG_GET_ALL = "SELECT * FROM (SELECT "+SEARCH_TAG_COLUMN+" FROM "+SEARCH_TAG_TABLE_NAME+" ORDER BY "+SEARCH_TAG_COUNTER+" DESC) WHERE ROWNUM <= "+SEARCH_TAG_RETURN_RESULTS;
	

	String SEARCH_TAG_CLEANUP = PropertyLoader.PROPERTIES.getProperty("SearchTagCleanUpEnabled");
	String SEARCH_TAG_CLEANUP_CRON = PropertyLoader.PROPERTIES.getProperty("SearchTagCleanUpCron");
	String SEARCH_TAG_CLEANUP_QUERY = PropertyLoader.PROPERTIES.getProperty("SearchTagCleanUpQuery");

}
