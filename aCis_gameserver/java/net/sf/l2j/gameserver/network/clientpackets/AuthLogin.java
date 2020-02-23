package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameguard.GameGuard;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.network.SessionKey;

public final class AuthLogin extends L2GameClientPacket
{
	private String _loginName;
	private int _playKey1;
	private int _playKey2;
	private int _loginKey1;
	private int _loginKey2;
	private final byte[] _data = new byte[48];
	
	@Override
	protected void readImpl()
	{
		_loginName = readS().toLowerCase();
		_playKey2 = readD();
		_playKey1 = readD();
		_loginKey1 = readD();
		_loginKey2 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (getClient().getAccountName() != null)
			return;
		
		if (Config.ALLOW_GUARD_SYSTEM)
		{
			if (!GameGuard.getInstance().doAuthLogin(getClient(), _data, _loginName))
				return;
		}
		
		getClient().setAccountName(_loginName);
		getClient().setSessionId(new SessionKey(_loginKey1, _loginKey2, _playKey1, _playKey2));
		
		// Add the client.
		LoginServerThread.getInstance().addClient(_loginName, getClient());
	}
}