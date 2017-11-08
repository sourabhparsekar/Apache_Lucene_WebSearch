package com.lucene.webSearch.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.lucene.webSearch.lucene.Searcher;

/**
 * Servlet implementation class DataController
 */
@WebServlet("/DataController")
public class DataController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//final static Logger log = Logger.getLogger(DataController.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DataController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.err.println("Start of DataController doGet() Served at: " + request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.err.println("Start of DataController doPost()");
		Map<String, String> headerMap = new LinkedHashMap<String, String>();
		List<Map> dataMap = new ArrayList<Map>();

		String _print_page = null;
		String _search = null;
		List<String> fileList = null;
		Map<String, String> row = null;

		RequestDispatcher rd = null;
		try {
			_print_page = request.getParameter("_print_page");
			_search = request.getParameter("_search");

			System.err.println("Values : " + _search);
			// Lucene call for search
			fileList = Searcher.search(_search);
			// DBManager.SELECT_FROM_VIEW(_search, headerMap, dataMap);
			headerMap.put("1", "File Names");
			request.setAttribute("headerMap", headerMap);

			int rowIndex = 1;
			for (String file : fileList) {
				row = new LinkedHashMap<String, String>();
				// COLUMN_NAME/DISPLAY_NAME
				row.put("row" + rowIndex + "-1", file);
				dataMap.add(row);
				rowIndex++;
			}
			request.setAttribute("dataMap", dataMap);

			request.setAttribute("_search", _search);

			System.err.println("End of DataController doPost()");

			rd = request.getRequestDispatcher(_print_page);
			if (rd != null)
				rd.forward(request, response);
		} catch (Exception exception)// |NullPointerException|ApplicationException
										// exception)
		{
			System.err.println("Exception occured : " + exception.getMessage());
		}

	}

}
