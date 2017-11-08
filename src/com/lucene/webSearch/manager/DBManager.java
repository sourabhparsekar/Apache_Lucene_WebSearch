package com.lucene.webSearch.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lucene.webSearch.utility.ApplicationConstants;
import com.lucene.webSearch.utility.ApplicationException;
import com.lucene.webSearch.utility.DBUtil;
import com.lucene.webSearch.utility.PropertyLoader;

public class DBManager {

	// final static Logger log = Logger.getLogger(DBManager.class);

	/**
	 * Method to INSERT to Lucene Audit Table * @param OperationName
	 * 
	 * @param Data
	 * @param Comments
	 * @return
	 */
	public static boolean AUDIT_INSERT(final String OperationName, String Data, String Comments) {
		System.out.println("Start of AUDIT_INSERT method of DBManager Class.");

		Connection connection = null;
		DBUtil dbUtil = null;
		PreparedStatement statement = null;
		boolean success = false;

		try {
			if (!ApplicationManager.isDBTrackingEnabled())
				return false;

			System.out.println("INSERT ERROR Operation Name ==> " + OperationName);
			System.out.println("INSERT Data ==> " + Data);
			System.out.println("INSERT Comments ==> " + Comments);
			if (Data.length() > 1024) {
				System.out.println("Data length exceeds column, trimming to 1024 characters.");
				Data = Data.substring(0, 1020);
				Data = Data + "...";
			}

			if (Comments.length() > 1024) {
				System.out.println("Comments length exceeds column, trimming to 1024 characters.");
				Comments = Comments.substring(0, 1020);
				Comments = Comments + "...";
			}

			final String INSERT = ApplicationConstants.LUCENE_INSERT_QUERY; // 5
			// parameters;
			System.out.println("INSERT ==> " + INSERT);

			dbUtil = new DBUtil();
			connection = dbUtil.getConnection();

			// constructs SQL statement
			statement = connection.prepareStatement(INSERT);

			int parameterIndex = 1; // (EMPLOYEE_ID, FUNCTION, DATA)
			statement.setString(parameterIndex++, OperationName);
			statement.setString(parameterIndex++, Data);
			statement.setString(parameterIndex++, Comments);

			// sends the statement to the database server
			int row = statement.executeUpdate();
			if (row > 0) {
				success = true;
				System.out.println("DATA AUDIT SUCCESSFULLY DONE");
			}

		} catch (ApplicationException e) {
			System.err.println("Application Exception while adding AUDIT to DB");
			// e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception while adding AUDIT to DB");
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(connection, statement, null);
			dbUtil = null;
		}
		System.out.println("Data entered in audit table :" + success);
		System.out.println("End of AUDIT_INSERT method of DBManager Class.");

		return success;
	}

	/**
	 * Method to insert error to Lucene Audit Table
	 * 
	 * @param USER
	 * @param function
	 * @param Data
	 * @return
	 */
	public static boolean ERROR_INSERT(final String OperationName, String Data, String Comments) {
		System.out.println("Start of ERROR_INSERT method of DBManager Class.");

		Connection connection = null;
		DBUtil dbUtil = null;
		PreparedStatement statement = null;
		boolean success = false;

		try {
			if (!ApplicationManager.isDBTrackingEnabled())
				return false;

			System.out.println("INSERT ERROR Operation Name ==> " + OperationName);
			System.out.println("INSERT Data ==> " + Data);
			System.out.println("INSERT Comments ==> " + Comments);
			if (Data.length() > 1024) {
				System.out.println("Data length exceeds column, trimming to 1024 characters.");
				Data = Data.substring(0, 1020);
				Data = Data + "...";
			}

			if (Comments.length() > 1024) {
				System.out.println("Comments length exceeds column, trimming to 1024 characters.");
				Comments = Comments.substring(0, 1020);
				Comments = Comments + "...";
			}

			final String INSERT = ApplicationConstants.LUCENE_INSERT_QUERY; // 5
			// parameters
			System.out.println("INSERT ==> " + INSERT);

			dbUtil = new DBUtil();
			connection = dbUtil.getConnection();

			// constructs SQL statement
			statement = connection.prepareStatement(INSERT);

			int parameterIndex = 1; // (Operation Name, DATA, Comments)
			statement.setString(parameterIndex++, "ERROR : " + OperationName);
			statement.setString(parameterIndex++, Data);
			statement.setString(parameterIndex++, Comments);

			// sends the statement to the database server
			int row = statement.executeUpdate();
			if (row > 0) {
				success = true;
				System.out.println("DATA ERROR SUCCESSFULLY DONE");
			}

		} catch (ApplicationException e) {
			System.err.println("Application Exception while adding ERROR to DB");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception while adding ERROR to DB");
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(connection, statement, null);
			dbUtil = null;
		}
		System.out.println("Data entered in error table :" + success);
		System.out.println("End of ERROR_INSERT method of DBManager Class.");

		return success;
	}

	/**
	 * #Below method for Add/update Tag Table Name if SearchTagEnabled=true
	 * SearchTagTableName=SEARCH_TAGS SearchTagColumnName=TAG
	 * SearchTagIntCounterName=COUNT
	 * 
	 * @param TagName
	 * @return true/false
	 */
	public static boolean TAG_INSERT_UPDATE(final String TagName) {
		System.out.println("Start of TAG_INSERT_UPDATE method of DBManager Class.");

		Connection connection = null;
		DBUtil dbUtil = null;
		PreparedStatement statement = null;
		boolean success = false;
		String QUERY = null;

		try {
			if (!ApplicationManager.isSearchTagTrackingEnabled())
				return false;

			System.out.println("Search Tag Name ==> " + TagName);

			dbUtil = new DBUtil();
			connection = dbUtil.getConnection();

			if (!IsSearchTagAvailable(TagName.trim())) {
				QUERY = ApplicationConstants.SEARCH_TAG_INSERT_QUERY;
				// constructs SQL statement
				statement = connection.prepareStatement(QUERY);
				statement.setString(1, TagName.trim().toLowerCase());
			} else {
				QUERY = ApplicationConstants.SEARCH_TAG_UPDATE_QUERY;
				// constructs SQL statement
				statement = connection.prepareStatement(QUERY);
				statement.setString(1, "%"+TagName.trim().toLowerCase()+"%");
			}
			System.out.println("QUERY ==> " + QUERY);


			// sends the statement to the database server
			int row = statement.executeUpdate();
			if (row > 0) {
				success = true;
				System.out.println("Search Tag Added/Updated successfully.");
			}

		} catch (ApplicationException e) {
			System.err.println("Application Exception while adding/updating SearchTag to DB");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception while adding/updating SearchTag to DB");
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(connection, statement, null);
			dbUtil = null;
		}
		System.out.println("Data entered in SearchTag table :" + success);
		System.out.println("End of TAG_INSERT_UPDATE method of DBManager Class.");

		return success;
	}

	/**
	 *
	 * #Below method used to enable Search Tag Tracking SearchTagEnabled=true
	 * 
	 * @param TagName
	 * @return true/false
	 */
	public static Boolean IsSearchTagAvailable(final String TagName) {

		System.out.println("Start of IsSearchTagAvailable method of DBManager Class.");

		Connection connection = null;
		DBUtil dbUtil = null;
		PreparedStatement statement = null;
		boolean success = false;
		ResultSet resultset = null;

		try {
			System.out.println("Search Tag Name ==> " + TagName);

			System.out.println("SELECT ==> " + ApplicationConstants.SEARCH_TAG_SELECT_QUERY);

			dbUtil = new DBUtil();
			connection = dbUtil.getConnection();

			// constructs SQL statement
			statement = connection.prepareStatement(ApplicationConstants.SEARCH_TAG_SELECT_QUERY);
			statement.setString(1, "%"+TagName.trim().toLowerCase()+"%");

			// sends the statement to the database server
			resultset = statement.executeQuery();

			if (resultset != null  && resultset.next()) {
				if(resultset.getInt(1) > 0)	success = true;
			}

		} catch (ApplicationException e) {
			System.err.println("Application Exception while checking if SearchTag is Available.");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception while  checking if SearchTag is Available.");
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(connection, statement, resultset);
			dbUtil = null;
		}

		System.out.println("End of IsSearchTagAvailable method of DBManager Class.");

		return success;
	}

	/**
	 *
	 * #Below method used to enable Search Tag Tracking SearchTagEnabled=true
	 * 
	 * @param TagName
	 * @return true/false
	 */
	public static List<String> GetAllSearchTagAvailable() {

		System.out.println("Start of GetAllSearchTagAvailable method of DBManager Class.");

		Connection connection = null;
		DBUtil dbUtil = null;
		PreparedStatement statement = null;
		ResultSet resultset = null;
		List<String> searchTagList = null;
		
		try {
		
			System.out.println("SELECT ==> " + ApplicationConstants.SEARCH_TAG_GET_ALL);

			dbUtil = new DBUtil();
			connection = dbUtil.getConnection();

			// constructs SQL statement
			statement = connection.prepareStatement(ApplicationConstants.SEARCH_TAG_GET_ALL);

			// sends the statement to the database server
			resultset = statement.executeQuery();
			searchTagList = new ArrayList<String>();
			
			while (resultset.next()) {
				searchTagList.add(resultset.getString(1));
			}

		} catch (ApplicationException e) {
			System.err.println("Application Exception while getting SearchTag is Available.");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception while getting SearchTag is Available.");
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(connection, statement, resultset);
			dbUtil = null;
		}

		System.out.println("End of GetAllSearchTagAvailable method of DBManager Class.");

		return searchTagList;
	}

	
	/**
	 *
	 * #Below parameter used to specify Lucene db table cleanup query for data
	 * older than n days LuceneTableCleanUpQuery=DELETE FROM
	 * SYSTEM.LUCENE_AUDIT_TABLE WHERE TRANSACTIONDATE <= TRUNC(SYSDATE) - 30
	 * 
	 * @param TagName
	 * @return true/false
	 */
	public static Boolean CleanUpAuditTable() {

		System.out.println("Start of CleanUpAuditTable method of DBManager Class.");

		Connection connection = null;
		DBUtil dbUtil = null;
		Statement statement = null;
		boolean success = false;
		ResultSet resultset = null;

		try {
			System.out.println("DELETE QUERY ==> " + ApplicationConstants.LUCENE_TABLE_CLEANUP_QUERY);

			dbUtil = new DBUtil();
			connection = dbUtil.getConnection();

			// sends the statement to the database server
			statement = connection.createStatement();
			success = statement.execute(ApplicationConstants.LUCENE_TABLE_CLEANUP_QUERY);
			System.out.println("Old Data Purged successfully.");
		} catch (ApplicationException e) {
			System.err.println("Application Exception while cleaning Audit Table.");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception while cleaning Audit Table.");
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(connection, statement, resultset);
			dbUtil = null;
		}

		System.out.println("End of CleanUpAuditTable method of DBManager Class.");

		return success;
	}

	public static void main(String... strings) throws ApplicationException {
		PropertyLoader.initialisePropertyLoader();
		System.out.println(DBManager.GetAllSearchTagAvailable());
	}

}
