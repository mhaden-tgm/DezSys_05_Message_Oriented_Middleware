import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Chat Queue Class
 * 
 * @author mhaden
 * @version 1.0
 *
 */
public class JMSQueue {
	private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private static Session session = null;
	private static Connection connection = null;
	private static MessageConsumer mailbox = null;

	/**
	 * 
	 * @param url ip for connection
	 * @param user username
	 * @param topic chattopic
	 */
	public JMSQueue(String url, String user, String topic) {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination = session.createQueue(user);
			// Create the consumer
			mailbox = session.createConsumer(destination);

		} catch (Exception e) {
			System.out.println("Queue connection failed: " + e.getMessage());
		}
	}

	/**
	 * sending a message
	 * 
	 * @param receiver message receiver
	 * @param message the messagetext
	 */
	public void mail(String receiver, String message) {
		TextMessage msg;
		try {
			Destination destination = session.createQueue(receiver);
			// Create the producer.
			MessageProducer mail = session.createProducer(destination);
			System.out.println("# MAIL SENT #");
			msg = session.createTextMessage(message);
			mail.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * receive messages
	 */
	public void mailbox() {
		try {
			System.out.println("# MAILBOX #");
			TextMessage message;
			while ((message = (TextMessage) mailbox.receive(1000)) != null) {
				System.out.println(message.getText());
			}
			System.out.println("#   #   #");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * close connection
	 */
	public void close() {
		try {
			mailbox.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
