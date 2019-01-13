package com.ubaid.app.controller.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ubaid.app.model.Converter;
import com.ubaid.app.model.asianhandicap.AssianHandicapConverter;
import com.ubaid.app.model.logic.AssianHandicapLogic;
import com.ubaid.app.model.logic.Logic;
import com.ubaid.app.model.logic.OverUnderLogic;
import com.ubaid.app.model.objects.Entity;
import com.ubaid.app.model.objects.Match;
import com.ubaid.app.model.overunder.OverUnderConverter;

public class _OverUnder extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    public _OverUnder() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Logic logic = new OverUnderLogic();
			Converter converter = new OverUnderConverter();
			
			String _id = request.getParameter("id");
			String homeTeam = request.getParameter("homeTeam");
			String awayTeam = request.getParameter("awayTeam");
						
			long id = Long.parseLong(_id);
			
			LinkedList<Entity> _eEntities = logic.getAll(id);
					
			LinkedList<Match> events =  converter.convert(id, homeTeam, awayTeam, _eEntities);
			
			JSONArray array = new JSONArray();
			JSONObject object;
			
			for (Match match : events)
			{
				object = new JSONObject();
				object.put("threshold", match.getHomeTeamThreshold1());
				object.put("overTeamOdds", match.getOverOdds());
				object.put("underTeamOdds", match.getUnderOdds());
				array.put(object);
			}
			
			
			PrintWriter writer = response.getWriter();
			writer.write(array.toString());
			writer.flush();
			
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

}