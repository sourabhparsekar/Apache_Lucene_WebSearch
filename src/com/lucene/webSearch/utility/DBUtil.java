package com.lucene.webSearch.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import org.apache.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;

public class DBUtil {

	//	private Connection conn;
	private static Helper helper = new Helper(); 

	//final static Logger log = Logger.getLogger(DBUtil.class);     
	/**
	 * <b>Get Application Oracle DB Connection for Insert/Update</b>
	 * @return
	 * @throws ApplicationException
	 */
	public Connection getConnection() throws ApplicationException {
		//		if( conn != null )
		//			return conn;

		System.out.println("Start of getConnection method.");
		Connection conn;
		//InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream( "/db.properties" );
		//Properties properties = new Properties();
		OracleDataSource ds = null;

		try {
			//properties.load( inputStream );
			String dbURL = PropertyLoader.PROPERTIES.getProperty("dbURL");
			String dbUser = PropertyLoader.PROPERTIES.getProperty("dbUser");
			String dbPass = PropertyLoader.PROPERTIES.getProperty("dbPass");
			ds = new OracleDataSource();
			ds.setURL(dbURL);
			System.out.println("DataSource URL Set::: "+dbURL);
			conn=ds.getConnection(Cryptography.decrypt(dbUser),Cryptography.decrypt(dbPass));
			System.out.println("DataSource connection::: "+conn);
		} /*catch (IOException e) {
			System.out.println("IO Exception while making db Connection");
			e.printStackTrace();
			throw new ApplicationException("Exception occured while making database connection. "+e.getMessage());
		}*/ catch (SQLException e) {
			System.err.println("SQL Exception while making db Connection");
			//			e.printStackTrace();
			throw new ApplicationException("Exception occured while making database connection. "+e.getMessage());
		} catch (Exception e) {
			System.err.println("Exception while making db Connection");
			//			e.printStackTrace();
			throw new ApplicationException("Exception occured while making database connection. "+e.getMessage());
		}
		System.out.println("End of getConnection method.");
		return conn;
	}

	/**
	 * <b>to be called to release resources.</b>
	 * @param connection
	 * @param statement
	 * @param resultset
	 */
	public void closeConnection( Connection connection, Statement statement, ResultSet resultset ) {
		System.out.println("Start of closeConnection method.");

		try {
			System.out.println("Closing Connection "+connection);
			if( connection != null )
				connection.close();
		} catch (SQLException e) {
			//			e.printStackTrace();
			System.err.println("SQL Exception while closing connection");
		} catch (Exception e) {
			//			e.printStackTrace();
			System.err.println("Exception while closing connection");
		} finally {
			connection = null;
		}

		try {
			System.out.println("Closing statement "+statement);
			if( statement != null )
				statement.close();
		} catch (SQLException e) {
			//			e.printStackTrace();
			System.err.println("SQL Exception while closing Statement");
		} catch (Exception e) {
			//			e.printStackTrace();
			System.err.println("Exception while closing Statement");
		} finally {
			statement = null;
		}

		try {
			System.out.println("Closing resultset "+resultset);
			if( resultset != null )
				resultset.close();
		} catch (SQLException e) {
			//			e.printStackTrace();
			System.err.println("SQL Exception while closing ResultSet");
		} catch (Exception e) {
			//			e.printStackTrace();
			System.err.println("Exception while closing ResultSet");
		} finally {
			resultset = null;
		}
		System.out.println("End of closeConnection method.");
	}

}

