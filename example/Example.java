import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.common.future.SshFutureListener;
import org.apache.sshd.common.util.NoCloseInputStream;
import org.apache.sshd.common.util.NoCloseOutputStream;


public class Example {

	public static void main(String[] args) throws IOException, InterruptedException, Exception
	{
	    SshClient client = SshClient.setUpDefaultClient();
	    client.start();

	    final ClientSession session = client.connect("bob", "bob.mynetwork.com", 22).await().getSession();

	    int authState = ClientSession.WAIT_AUTH;
	    while ((authState & ClientSession.WAIT_AUTH) != 0) {

	        session.addPasswordIdentity("bobspassword");

	        System.out.println("authenticating...");
	        final AuthFuture authFuture = session.auth();
	        authFuture.addListener(new SshFutureListener<AuthFuture>()
	        {
	            @Override
	            public void operationComplete(AuthFuture arg0)
	            {
	                System.out.println("Authentication completed with " + ( arg0.isSuccess() ? "success" : "failure"));
	            }
	        });

	        authState = session.waitFor(ClientSession.WAIT_AUTH | ClientSession.CLOSED | ClientSession.AUTHED, 0);
	    }

	    if ((authState & ClientSession.CLOSED) != 0) {
	        System.err.println("error");
	        System.exit(-1);
	    }

	    final ClientChannel channel = session.createShellChannel();
	    channel.setOut(new NoCloseOutputStream(System.out));
	    channel.setErr(new NoCloseOutputStream(System.err));
	    channel.open();

	    executeCommand(channel, "pwd\n");
	    executeCommand(channel, "ll\n");
	    channel.waitFor(ClientChannel.CLOSED, 0);

	    session.close(false);
	    client.stop();
	}

	private static void executeCommand(final ClientChannel channel, final String command) throws IOException
	{
	    final InputStream commandInput = new ByteArrayInputStream(command.getBytes());
	    channel.setIn(new NoCloseInputStream(commandInput));
	}
}
