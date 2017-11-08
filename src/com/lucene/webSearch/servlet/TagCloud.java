package com.lucene.webSearch.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lucene.webSearch.manager.DBManager;
import com.lucene.webSearch.utility.ApplicationException;
import com.lucene.webSearch.utility.PropertyLoader;

/**
 * Servlet implementation class TagCloud
 */
@WebServlet("/TagCloud")
public class TagCloud extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TagCloud() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {
			PropertyLoader.initialisePropertyLoader();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> tagList = DBManager.GetAllSearchTagAvailable();
		System.out.println("Tag List has :: "+tagList.size());
		String buffer = "";
		Random randomGenerator = new Random();
		
		String color = "";
		for(String str : tagList)
		{
			switch(randomGenerator.nextInt(100)%4)
			{
			case 0: color = "#337ab7"; break;
			case 1: color = "#185387"; break;
			case 2: color = "#8dadc9"; break;
			case 3: color = "#4b6377"; break;
			}
			int size = randomGenerator.nextInt(250);
			if(size < 50) size += 50;
			buffer += "<a href=\"Javascript:selectTag('"+str+"');\" style=\"font-size:"+size+"%;color:"+color+";\">"+str+"</a> "; 
		}
		if(tagList.size() < 1)
			buffer = "No Tags Available. Please Search to Add Tags.";
		response.getWriter().append(buffer);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
