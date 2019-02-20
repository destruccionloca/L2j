package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.pledge.Clan;

/**
 * Format: (ch) d [dd]
 * @author -Wooden-
 */
public class PledgeSkillList extends L2GameServerPacket
{
	private final Clan _clan;
	
	public PledgeSkillList(Clan clan)
	{
		_clan = clan;
	}
	
	@Override
	protected void writeImpl()
	{
		L2Skill[] skills = _clan.getClanSkills();
		
		writeC(0xfe);
		writeH(0x39);
		writeD(skills.length);
		for (L2Skill sk : skills)
		{
			if (sk == null)
				continue;
			
			writeD(sk.getId());
			writeD(sk.getLevel());
		}
	}
}