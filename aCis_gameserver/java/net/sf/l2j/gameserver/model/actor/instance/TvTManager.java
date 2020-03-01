package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.events.TvTEvent;
import net.sf.l2j.gameserver.model.entity.events.Event.EventState;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Williams
 *
 */
public class TvTManager extends Folk
{
	public TvTManager(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.equals("tvt_event_participation"))
			TvTEvent.getInstance().registerPlayer(player);
		else if (command.equals("tvt_event_remove_participation"))
			TvTEvent.getInstance().removePlayer(player);
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		if (player == null)
			return;
		
		if (TvTEvent.getInstance().getEventState() == EventState.REGISTRATION)
		{
			String htmFile = "data/html/event/";
			
			if (!TvTEvent.getInstance().isRegistered(player))
				htmFile += "TvTEventParticipation";
			else
				htmFile += "TvTEventRemoveParticipation";
			
			htmFile += ".htm";
			
			String htmContent = HtmCache.getInstance().getHtm(htmFile);
			if (htmContent != null)
			{
				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
				
				npcHtmlMessage.setHtml(htmContent);
				npcHtmlMessage.replace("%objectId%", String.valueOf(getObjectId()));
				npcHtmlMessage.replace("%registeredcount%", String.valueOf(TvTEvent.getInstance().getRegistered().size()));
				npcHtmlMessage.replace("%minimumplayers%", String.valueOf(Config.TVT_MIN_PARTICIOANTS));
				npcHtmlMessage.replace("%maximumplayers%", String.valueOf(Config.TVT_MAX_PARTICIOANTS));
				npcHtmlMessage.replace("%minimumlevel%", String.valueOf(Config.TVT_MIN_LEVEL));
				npcHtmlMessage.replace("%maximumlevel%", String.valueOf(Config.TVT_MAX_LEVEL));
				player.sendPacket(npcHtmlMessage);
			}
		}
		else
		{
			String htmContent = HtmCache.getInstance().getHtm("data/html/event/TvTEventStatus.htm");
			
			if (htmContent != null)
			{
				
				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(getObjectId());
				
				npcHtmlMessage.setHtml(htmContent);
				npcHtmlMessage.replace("%team1playercount%", String.valueOf(TvTEvent.getInstance().getBlueTeam().size()));
				npcHtmlMessage.replace("%team1points%", String.valueOf(player.getEvent().getBlueKills()));
				npcHtmlMessage.replace("%team2playercount%", String.valueOf(TvTEvent.getInstance().getRedTeam().size()));
				npcHtmlMessage.replace("%team2points%", String.valueOf(player.getEvent().getRedKills()));
				player.sendPacket(npcHtmlMessage);
			}
		}
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}