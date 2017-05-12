import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.BufferedReader;
import java.util.Scanner;

/**
 * Created by admin on 12.05.2017.
 */
public class Client extends Thread {
    public static Connection connection;
    public Client() throws JMSException {

        connection = createConection();

    }

    public Connection createConection() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("" + "tcp://localhost:61616");
        return activeMQConnectionFactory.createConnection();
    }

    Scanner scanner = new Scanner(System.in);
    String message="";

    @Override
    public void run() {
        try {
        //   Connection connection = createConection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("MyQueue");
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); //ВАЖНО!
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
