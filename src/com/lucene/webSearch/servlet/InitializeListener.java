/**
 * 
 */
package com.lucene.webSearch.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.lucene.webSearch.application.LuceneDocumentSearch;
import com.lucene.webSearch.utility.ApplicationException;
import com.lucene.webSearch.utility.PropertyLoader;

/**
 * @author soura
 *
 */
@WebListener
public class InitializeListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("On start web app");
        LuceneDocumentSearch.execute();
        try {
			PropertyLoader.initialisePropertyLoader();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("On shutdown web app");
    }
	
	
	
}
