import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.BufferedReader;
import java.util.Scanner;

/**
 * Created by admin on 12.05.2017.
 */
public class Client extends Thread {
    public static Connection connection;
    Scanner scanner = new Scanner(System.in);
    public static String message="";
    public static Session session;
    public static Destination destination;
    public static MessageProducer producer;

    public Client() throws JMSException {

        connection = createConection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue("MyQueue");
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); //ВАЖНО!
        TextMessage textMessage = session.createTextMessage("привет");
        producer.send(textMessage);
    }

    public Connection createConection() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("" + "tcp://localhost:61616");
        return activeMQConnectionFactory.createConnection();
    }


    @Override
    public void run() {
        try {
            //   Connection connection = createConection();

            while (!(message.equals("exit"))) {
                message = scanner.nextLine();
                TextMessage textMessage = session.createTextMessage(message);
                producer.send(textMessage);
            }
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JMSException {
        Client client1 = new Client();
        client1.run();

    }

}
