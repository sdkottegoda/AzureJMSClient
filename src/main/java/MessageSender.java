//import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class MessageSender {
    public static void main(String[] args) throws NamingException, JMSException {

        String queueName = "tempq_sasikala";
        // Set up JNDI context
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("connectionfactory.SBCF", Const.failoverString);

        hashtable.put("queue.tempq_sasikala", queueName);
        hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
        Context context = new InitialContext(hashtable);

        // Look up queue
        Destination queue = (Destination) context.lookup(queueName);
//        Destination queue = (Destination) queueName;

        ConnectionFactory cf = (ConnectionFactory) context.lookup("SBCF");
        Connection connection = cf.createConnection(Const.keyName, Const.keyValue);
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        // Create producer
        MessageProducer producer = session.createProducer(queue);
        while (true) {
            try {
                TextMessage message = session.createTextMessage("hello");
                producer.send(message);
                System.out.println("Sent!");
            } catch (JMSException e) {
                System.out.println("Not sent!");
                System.out.println("Caught" + e.getMessage());
            }
            //        message.writeBytes(String.valueOf(i).getBytes());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
