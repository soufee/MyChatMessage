import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by admin on 12.05.2017.
 */
public class Server {
    public static int listeners = 1;
    private static Connection connection;
    private static Session session;
    private static MessageConsumer messageConsumer;

    public Connection createConection() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("" + "tcp://localhost:61616");
        return activeMQConnectionFactory.createConnection();
    }

    public void recieveMessage() throws JMSException {
        try {
            connection = createConection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("MyQueue");

            messageConsumer = session.createConsumer(destination);

            Message message = messageConsumer.receive(5000);
            System.out.println(((TextMessage) message).getText());

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            session.close();
            connection.close();
            messageConsumer.close();
        }

    }

    public static void main(String[] args) throws InterruptedException {

        Server consumer = new Server();

        while (listeners > 0) {
            // System.out.println("Количество пользователей = "+listeners);
            try {
                consumer.recieveMessage();
            } catch (Exception e) {
                System.out.println("Количество участников чата " + listeners);
                Thread.sleep(1000);

            }
        }
    }
}
