package com.ubaid.app.controller.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ubaid.app.model.schedule1_1.Notification;
import com.ubaid.app.model.schedule1_1.OutcomeNotificationi;

public class _notification extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
    public _notification()
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		try
		{
			System.out.println("In _notification servlets");
			final Notification notification = new OutcomeNotificationi();
					
	    	response.setContentType("text/event-stream");
	    	response.setCharacterEncoding("utf-8");
	    	
	    	PrintWriter writer = null;
	    	
	    	while(true)
	    	{
				JSONObject object = notification.getNotification();

	    		try
	    		{
	    			writer = response.getWriter();
	    			writer.print("data: "  + object.toString());
	    			writer.print("\n\n");	    			
	    			response.flushBuffer();
	    		}
	    		catch(IOException exp)
	    		{
	    			writer.close();
	    			break;
	    		}
	    	}
			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
