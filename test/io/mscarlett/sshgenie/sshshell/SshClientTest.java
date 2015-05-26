package io.mscarlett.sshgenie.sshshell;

import java.io.IOException;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.command.ScpCommandFactory;

public class SshClientTest {

	private static final String DEFAULT_KEY = "hostkey.ser";
	
	public void test() throws IOException {
	    SshServer sshd = SshServer.setUpDefaultServer();
	    sshd.setPort(22);
	    sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(DEFAULT_KEY));
	    sshd.setShellFactory(new ProcessShellFactory(new String[] { "/bin/sh", "-i", "-l" }));
	    sshd.setCommandFactory(new ScpCommandFactory(10000));
	    sshd.start();
	}
}
