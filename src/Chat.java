import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Chat Main class
 * 
 * @author mhaden
 * @version 1.0
 *
 */
public class Chat {

	/**
	 * main
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Not all arguments were given:\njmschat <ip_message_broker> <benutzername> <chatroom>");
		} else {
			System.out.println("# JMS CHAT #\nUsage:\n/mailbox\n/mail <destination_username> <message>");
			
			JMSTopic topic = new JMSTopic("tcp://" + args[0] + ":61616", args[1], args[2]);
			JMSQueue queue = new JMSQueue("tcp://" + args[0] + ":61616", args[1], args[2]);

			try {
				// Read from command line
				BufferedReader commandLine = new java.io.BufferedReader(new InputStreamReader(System.in));

				// Loop until the word "exit" is typed
				while (true) {
					String temp;

					temp = commandLine.readLine();
					String[] s = temp.split("\\s+");
					String[] ms = null;

					if (s[0].equalsIgnoreCase("/exit")) {
						topic.close();
						queue.close();
						System.exit(0);// exit program
					} else if (s[0].equalsIgnoreCase("/mail")) {
						if (temp.matches("/\\S+\\s+\\S+\\s+\\S+.+")) {
							ms = temp.split("/\\S+\\s+\\S+\\s+");
							queue.mail(s[1], ms[1]);
						} else {
							System.err.println("Usage: /mail <destination_username> <message>");
						}

					} else if (s[0].equalsIgnoreCase("/mailbox")) {
						queue.mailbox();

					} else {
						topic.send(temp.toString());
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
