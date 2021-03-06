package com.ubaid.app.model.strategy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ubaid.app.model.SportUtil;
import com.ubaid.app.model.SportUtilFactory;
import com.ubaid.app.model.logic.Logic;
import com.ubaid.app.model.logic.SubEventsLogic;
import com.ubaid.app.model.objects.Converter;
import com.ubaid.app.model.objects.ConverterFactory;
import com.ubaid.app.model.objects.Entity;
import com.ubaid.app.model.objects.Match;
import com.ubaid.app.model.objects.SubEvents;
import com.ubaid.app.model.schedule1_1.Helper;
import com.ubaid.app.model.schedule1_1.Key;
import com.ubaid.app.model.schedule1_1.Outcome;
import com.ubaid.app.model.schedule1_1.oddsDetection.OddsDetection;

/**
 * this class is responsible to show matches of an tournament
 * 
 * we have two providers having id 3000107 and 3000866
 * how to show subevents of an tournament?
 * first we retrieve all matches of an tournaments
 * then we find its home/draw/away odds against each match
 * when we find the home/draw/away odds against this match 
 * then we put this match in the return array (representing in the web)
 * ---> here we have responsiblity 
 * ---> we have to separate different provider's odds against each match 
 * example:
 * 			provider_1 team_x team_y odds
 * 			provider_2 team_x team_y odds
 *  
 * 
 * @author ubaid
 *
 */
public class SubEventsStrategy extends AbstractRequestHandler
{

	public SubEventsStrategy()
	{
		
	}

	@Override
	public JSONArray get(Map<String, String[]> map)
	{
		//geting converter which convert subEvents into match
		Converter converter = ConverterFactory.getConverter();
		
		//getting getRequest parameters
		String attribute = map.get("id")[0];
		String sportName = map.get("sportName")[0];
		
		//getting logic
		Logic logic  = new SubEventsLogic();
		
		//parsing id of the tournament
		long id = Long.parseLong(attribute);
		
		//getting all subevents
		LinkedList<Entity> events = logic.getAll(id);
		LinkedList<SubEvents> list = new LinkedList<>();
		for(int i = 0; i < events.size(); i++)
		{
			list.add((SubEvents) events.get(i));
		}
		
		//---> list contain all the matches (partial)  of an tournament 
		//---> partial means, that, there are two subevent having same event id 
		//---> but contain only one team, 
		//---> converter will merge these subevents (partial match) into complete match
		//---> it will first go converter which assign each match its home/draw/away odds

		//converting all subevents into matches on passing subEvents and eventPart id
		//these matches will have [home draw away odds]
		SportUtil su = SportUtilFactory.getSportUtil();
		
		//converter.convert(list<partial_matches, eventPartId, bettingTypeId) 
		//getting all matches [2 providers]
		List<Match> matchs = converter.convert(list,
								su.getEventPartId(sportName, su.getSubEventBettingType(sportName)),
								su.getSubEventBettingType(sportName));
					
		JSONArray array = new JSONArray();
		JSONObject object;
		
		//TODO observ the iteration
		//creating JSON array of JSON objects
		Map<Key, Outcome> table = OddsDetection.getTrackedOutcomes();
		for(Match match : matchs)
		{
			object = new JSONObject();
			try
			{
				String checked = null;
				Key key = new Key(match.getHomeTeamOutcomeId(), match.getProviderId());
				checked = table.get(key) == null ? "" : "Checked";
				
				object.put(Helper.ISCHECKED.toString(), checked);
				object.put(Helper.HOMETEAM.toString(), match.getHomeTeam());
				object.put(Helper.AWAYTEAM.toString(), match.getAwayTeam());
				object.put(Helper.LEAGUENAME.toString(), match.getTournamentName());
				object.put(Helper.HOMETEAMODDS.toString(), match.getHomeTeamOdds());
				object.put(Helper.AWAYTEAMODDS.toString(), match.getAwayTeamOdds());
				object.put(Helper.DRAWODDS.toString(), match.getDrawOdds());
				object.put(Helper.HOMETEAMOUTCOMEID.toString(), match.getHomeTeamOutcomeId());
				object.put(Helper.AWAYTEAMOUTCOMEID.toString(), match.getAwayTeamOutcomeId());
				object.put(Helper.DRAWOUTCOMEID.toString(), match.getDrawOutcomeId());
				object.put("startTime", match.getStartTime());
				object.put("id", match.getId());
				object.put("sportName", sportName);
				object.put("providerId", match.getProviderId());
				array.put(object);
				
			}
			catch(NullPointerException exp)
			{
				
			}

		}
			

		return array;
	}

}
