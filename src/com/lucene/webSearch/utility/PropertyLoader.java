package com.lucene.webSearch.utility;

/**
 * @author soura
 *
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.lucene.webSearch.manager.DBManager;

public class PropertyLoader {

	static public Properties PROPERTIES = new Properties();

	public static void initialisePropertyLoader() throws ApplicationException {

		InputStream input = null;

		try {

			try
			{
				input = PropertyLoader.class.getClassLoader().getResourceAsStream("resources/config.properties");
			}
			catch(Exception ex)
			{
				System.err.println("Exception occured :: "+ex.getMessage());
				input = new FileInputStream(".src/resources/config.properties");

			}
			// load a properties file
			PROPERTIES.load(input);

			
		} catch (IOException ex) {
			throw new ApplicationException("IOException while loading Property File with error :: " + ex.getMessage());
			// ex.printStackTrace();
		} catch (Exception ex) {
			throw new ApplicationException("Exception while loading Property File with error :: " + ex.getMessage());
			// ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new ApplicationException(
							"IOException while closing Property File with error :: " + e.getMessage());
					// e.printStackTrace();
				} catch (Exception e) {
					throw new ApplicationException(
							"Exception while closing Property File with error :: " + e.getMessage());
					// e.printStackTrace();
				}
			}
		}

	}
		
}