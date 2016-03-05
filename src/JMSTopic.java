import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Chat Topic class
 * 
 * @author mhaden
 * @version 1.0
 *
 */
public class JMSTopic implements MessageListener {
	private String user = null;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private Session session = null;
	private Connection connection = null;
	private MessageProducer producer = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;

	/**
	 * 
	 * @param url ip for connection
	 * @param user username
	 * @param topic chattopic
	 */
	public JMSTopic(String url, String user, String topic) {
		this.user = user;

		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic(topic);

			// Create the producer.
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);

		} catch (Exception e) {
			System.out.println("Topic connection failed: " + e.getMessage());
		}
	}

	/**
	 * message sending
	 * 
	 * @param s message
	 */
	public void send(String s) {
		// Create the message
		TextMessage message;
		try {
			message = session.createTextMessage(user + ": " + s);
			producer.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * message listener
	 */
	@Override
	public void onMessage(Message message) {
		try {
			System.out.println(((TextMessage) message).getText());
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
			producer.close();
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
