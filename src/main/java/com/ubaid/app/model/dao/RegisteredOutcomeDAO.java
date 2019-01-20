package com.ubaid.app.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;

import com.ubaid.app.model.abstractFactory.AbstractFactory;
import com.ubaid.app.model.abstractFactory.RegisteredOutcomeFactory;
import com.ubaid.app.model.objects.Entity;
import com.ubaid.app.model.schedule1_1.Outcome;
import com.ubaid.app.model.singleton.DataSource;

public class RegisteredOutcomeDAO extends AbstractDAO
{


	private static final String query = "INSERT INTO registeredOutcome VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String query1 = "SELECT * FROM registeredOutcome";
	private static final String delQuery = "DELETE FROM registeredOutcome WHERE id = ?";
	private static final String delAllQuery = "DELETE FROM registeredOutcome";
	
	public RegisteredOutcomeDAO()
	{
	}

	@Override
	String getQuery(QT type)
	{
		return query1;
	}

	@Override
	public boolean add(Entity entity) throws ServletException
	{
		try
		{
			Outcome outcome = (Outcome) entity;
			
			Connection con = DataSource.getConnection();
			PreparedStatement st = con.prepareStatement(query);
			st.setLong(1, outcome.getId());
			st.setLong(2, -1);
			st.setFloat(3, outcome.getOdds());
			st.setFloat(4, outcome.getThreshold());
			st.setString(5, outcome.getLeagueName());
			st.setString(6, outcome.getMatchName());
			st.setString(7, outcome.getParticipant());
			st.setString(8, outcome.getHomeTeam());
			st.setString(9, outcome.getAwayTeam());
			st.setTimestamp(10, outcome.getRegisterTime());
			st.setTimestamp(11, outcome.getChangedTime());
			st.setString(12, outcome.getBettingType().toString());
			st.executeUpdate();
			
			return true;
		}
		catch(SQLException exp)
		{
			exp.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean deleteById(long id)
	{
		try
		{
			Connection con = DataSource.getConnection();
			PreparedStatement st = con.prepareStatement(delQuery);
			st.setLong(1, id);
			st.executeUpdate();
			return true;
		}
		catch(SQLException exp)
		{
			exp.printStackTrace();
		}
		
		return false;
	}

	@Override
	public LinkedList<Entity> getAll()
	{
		return super.getAll();
	}

	
	
	@Override
	AbstractFactory getFactory()
	{
		return new RegisteredOutcomeFactory();
	}

	@Override
	public boolean deleteAll()
	{
		try
		{
			Connection con = DataSource.getConnection();
			PreparedStatement st = con.prepareStatement(delAllQuery);
			st.executeUpdate();
		}
		catch(SQLException exp)
		{
			return false;
		}
		
		return true;
	}
	
	
	

}
