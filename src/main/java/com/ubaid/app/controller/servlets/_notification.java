package com.ubaid.app.controller.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ubaid.app.model.notification.INotification;
import com.ubaid.app.model.notification.Notification;

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
			final INotification notification = new Notification();
					
	    	response.setContentType("text/event-stream");
	    	response.setCharacterEncoding("utf-8");
	    	
	    	PrintWriter writer = null;
	    	
	    	while(true)
	    	{
				JSONObject object = notification.getNotification();

	    		try
	    		{
//	    			double randomNumber = Math.random() * 1000;
	    			writer = response.getWriter();
//	    			writer.print("data: " + object.toString());
//	    			writer.print("data: " + "Time: " + Calendar.getInstance().getTime() + "\n\n");

	    			
	    			writer.print("data: "  + object.toString());
	    			writer.print("\n\n");

	    			
	    			
	    			response.flushBuffer();

//	    			writer = response.getWriter();
//	    			
//					System.out.println(object.toString() + "_notification servlets");
//					writer.print(object.toString());
//					response.flushBuffer();

					Thread.sleep(200);
	    			    			
	    		}
	    		catch(IOException exp)
	    		{
	    			writer.close();
	    			exp.printStackTrace();
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
