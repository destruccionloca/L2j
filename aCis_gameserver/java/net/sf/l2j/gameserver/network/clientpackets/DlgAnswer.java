package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.entity.events.TvTEvent;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 * @author Dezmond_snz Format: cddd
 */
public final class DlgAnswer extends L2GameClientPacket
{
	private int _messageId;
	private int _answer;
	private int _requesterId;
	
	@Override
	protected void readImpl()
	{
		_messageId = readD();
		_answer = readD();
		_requesterId = readD();
	}
	
	@Override
	public void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (_messageId == SystemMessageId.RESSURECTION_REQUEST_BY_S1.getId() || _messageId == SystemMessageId.DO_YOU_WANT_TO_BE_RESTORED.getId())
			player.reviveAnswer(_answer);
		else if (_messageId == SystemMessageId.S1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT.getId())
			player.teleportAnswer(_answer, _requesterId);
		else if (_messageId == 1983 && Config.ALLOW_WEDDING)
				player.engageAnswer(_answer);
		else if (_messageId == SystemMessageId.WOULD_YOU_LIKE_TO_OPEN_THE_GATE.getId())
			player.activateGate(_answer, 1);
		else if (_messageId == SystemMessageId.WOULD_YOU_LIKE_TO_CLOSE_THE_GATE.getId())
			player.activateGate(_answer, 0);
		else if (_answer == 1 && _messageId == SystemMessageId.S1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT_EVENT.getId())
			TvTEvent.getInstance().registerPlayer(player);
		else if (_messageId == SystemMessageId.S1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT_RECALL.getId())
		{   
			if (_answer == 1) 
			{
				for (Player allsplayer : World.getInstance().getPlayers())
					player.teleportTo(allsplayer.getX(), allsplayer.getY(), allsplayer.getZ(), 100);
			}
		}
	}
}