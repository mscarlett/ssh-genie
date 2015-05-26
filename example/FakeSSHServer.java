import java.io.IOException;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellFactory;


public class FakeSSHServer {

	public static void main(String[] args) throws IOException {
		SshServer sshd1 = configServer();
		SshServer sshd2 = configServer();
		SshServer sshd3 = configServer();
		
		sshd1.start();
		sshd2.start();
		sshd3.start();
	}
	
	public static SshServer configServer() {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(22);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
		sshd.setShellFactory(new ProcessShellFactory(new String[] { "/bin/sh", "-i", "-l" }));
		sshd.setCommandFactory(new ScpCommandFactory());
		return sshd;
	}
}
