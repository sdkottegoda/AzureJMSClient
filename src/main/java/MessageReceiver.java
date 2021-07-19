//import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class MessageReceiver {
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
//        Connection connection = cf.createConnection();
        Connection connection = cf.createConnection(Const.keyName, Const.keyValue);

        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(queue);

        Message message = consumer.receive();
        message.acknowledge();
        System.out.println(((TextMessage)message).getText());
        connection.close();

    }
}
