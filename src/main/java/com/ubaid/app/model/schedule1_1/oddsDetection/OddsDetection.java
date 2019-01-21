package com.ubaid.app.model.schedule1_1.oddsDetection;

import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ubaid.app.model.logic.OutcomeLogic;
import com.ubaid.app.model.logic.OutcomeLogici;
import com.ubaid.app.model.logic.UpdateRegisteredOCLogic;
import com.ubaid.app.model.logic.UpdateRegisteredOCLogici;
import com.ubaid.app.model.objects.Entity;
import com.ubaid.app.model.schedule1_1.Outcome;
import com.ubaid.app.model.schedule1_1.Schedule;

public class OddsDetection implements Schedule
{

	int index = 0;
	
	public OddsDetection()
	{
		
	}

	@Override
	public void schedule()
	{
		OutcomeLogic logic = new OutcomeLogici();
		UpdateRegisteredOCLogic updateLogic = new UpdateRegisteredOCLogici();
		
		while(true)
		{
			System.out.println("Running again: " + index++);
			
			//let retrieve the ids of outcomes
			Set<Long> ids = Schedule.trackedOutcomes.keySet();
			long[] _ids = new long[ids.size()];
			int index = 0;
			for(long id : ids)
			{
				_ids[index++] = id;
			}

			
			List<Outcome> list = new LinkedList<>();
			
			//get the response from the database as list of Outcome
			if(_ids.length < 1)
			{
				try
				{
					Thread.sleep(200);
				}
				catch(InterruptedException exp)
				{
					exp.printStackTrace();
				}
				
				continue;
			}
			
			
			LinkedList<Entity> _outcomes =  logic.getAll(_ids);
			LinkedList<Outcome> outcomes = new LinkedList<>();
			for(Entity outcome : _outcomes)
			{
				outcomes.add((Outcome) outcome);
			}
			
			//comparing
			for(Outcome outcome: outcomes)
			{
				try
				{
					Outcome oldOutcome = Schedule.trackedOutcomes.get(outcome.getId());
					
					if(Math.abs(outcome.getOdds() - oldOutcome.getOdds()) >  0.000001)
					{
						
						//TODO Cloneable
						System.out.println("outcome changed detected");
						outcome.setAwayTeam(oldOutcome.getAwayTeam());
						outcome.setHomeTeam(oldOutcome.getHomeTeam());
						outcome.setLeagueName(oldOutcome.getLeagueName());
						outcome.setParticipant(oldOutcome.getParticipant());
						outcome.setRegisterTime(oldOutcome.getRegisterTime());
						outcome.setChangedTime(new Timestamp(System.currentTimeMillis()));
						outcome.setOldOdds(oldOutcome.getOdds());
						outcome.setOldThreshold(oldOutcome.getThreshold());
						outcome.setMatchName(oldOutcome.getMatchName());
						OddsDetection.trackedOutcomes.put(outcome.getId(), outcome);
						list.add(outcome);
						Schedule.notificationQueue.add(outcome);
						
					}
					
				}
				catch(NullPointerException exp)
				{
					System.out.println("NUll point exception at line 81 of Scheduler");
				}
			}
			
			try
			{
				//update the changed odds
				ExecutorService updateService = Executors.newFixedThreadPool(1);
				updateService.execute(new Runnable()
				{
					
					@Override
					public void run()
					{
						if(list.size() > 0)
							updateLogic.updateOC(list);
					}
				});
				updateService.shutdown();
				System.out.println("There are total tracked events: " + OddsDetection.trackedOutcomes.size());
				Thread.sleep(30000);
			}
			catch(InterruptedException exp)
			{
				System.out.println("Thread Interrupted at Schedular");
			}
		}
	}

	@Override
	public void run()
	{
		schedule();
	}

	public static void putInTrackeEvents(long key, Outcome outcome)
	{
		Schedule.trackedOutcomes.put(key, outcome);
	}
	
	public static void removeFromTrackedEvents(long key)
	{
		OddsDetection.trackedOutcomes.remove(key);
	}
	
	public static Hashtable<Long, Outcome> getTrackedOutcomes()
	{
		return Schedule.trackedOutcomes;
	}
}