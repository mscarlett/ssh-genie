package io.mscarlett.sshgenie.sshshell;

import org.apache.sshd.client.ClientFactoryManager;
import org.apache.sshd.client.session.ClientSessionImpl;
import org.apache.sshd.common.io.IoSession;
 
public class ExecuteShellCommand extends ClientSessionImpl {

	public ExecuteShellCommand(ClientFactoryManager client, IoSession session) throws Exception {
		super(client, session);
	}
 
}