package com.ubaid.app.model.logic;

import java.util.LinkedList;

import javax.servlet.ServletException;

import com.ubaid.app.model.SportType;
import com.ubaid.app.model.dao.DAO;
import com.ubaid.app.model.objects.Entity;

public abstract class AbstractLogic implements Logic
{

	DAO dao;
	
	public AbstractLogic()
	{
		
	}

	@Override
	public LinkedList<Entity> getAll(SportType type)
	{
		return dao.getAll(type);
	}

	@Override
	public LinkedList<Entity> getAll()
	{
		return dao.getAll();
	}
	
	@Override
	public LinkedList<Entity> getAll(long id)
	{
		return dao.getAll(id);
	}

	@Override
	public boolean add(Entity entity) throws ServletException
	{
		throw new IllegalAccessError("Method not supported");
	}

	@Override
	public boolean delete(long id)
	{
		throw new IllegalAccessError("Method not supported");
	}

	@Override
	public Entity get(Entity entity)
	{
		throw new IllegalAccessError("Method not supported");
	}
	
}
