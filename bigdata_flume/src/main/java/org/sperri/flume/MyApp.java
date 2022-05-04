package org.sperri.flume;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.nio.charset.Charset;

/**
 * @author jie zhao
 * @date 2020/8/10 13:24
 */
public class MyApp {

    private static final String HOST = "10.0.10.252";

    private static final int PORT = 44444;

    public static void main(String[] args) {
        MyRpcClientFacade client = new MyRpcClientFacade();
        client.init(HOST, PORT);

        String data = "Hello Flume!";
        for (int i = 0; i < 10; i++) {
            client.sendDataToFlume(data);
        }
        client.cleanUp();
    }
}

class MyRpcClientFacade {
    private RpcClient client;
    private String hostname;
    private int port;

    public void init(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.client = RpcClientFactory.getDefaultInstance(hostname, port);
    }

    public void sendDataToFlume(String data) {
        // Create a Flume Event object that encapsulates the data.
        Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));

        try {
            // send the event.
            client.append(event);
        } catch (EventDeliveryException e) {
            client.close();
            client = null;
            client = RpcClientFactory.getDefaultInstance(this.hostname, this.port);
        }
    }

    public void cleanUp() {
        // close the RPC connection.
        client.close();
    }
}
