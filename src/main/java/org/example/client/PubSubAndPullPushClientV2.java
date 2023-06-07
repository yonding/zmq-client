package org.example.client;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Random;

public class PubSubAndPullPushClientV2 {

    public static void start(String[] args) {
        String clientID = args[0];

        try (ZContext context = new ZContext()) {
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.subscribe("".getBytes());
            subscriber.connect("tcp://localhost:5560");

            ZMQ.Socket publisher = context.createSocket(SocketType.PUSH);
            publisher.connect("tcp://localhost:5561");

            ZMQ.Poller poller = context.createPoller(1);
            poller.register(subscriber, ZMQ.Poller.POLLIN);

            Random random = new Random(System.currentTimeMillis());
            while (true) {
                try {
                    if (poller.poll(100) > 0) {
                        byte[] message = subscriber.recv();
                        System.out.println(clientID + ": receive status => " + new String(message));
                    } else {
                        int rand = random.nextInt(100) + 1;
                        if (rand < 10) {
                            Thread.sleep(1000);
                            String msg = "(" + clientID + ":ON)";
                            publisher.send(msg.getBytes());
                            System.out.println(clientID + ": send status - activated");
                        } else if (rand > 90) {
                            Thread.sleep(1000);
                            String msg = "(" + clientID + ":OFF)";
                            publisher.send(msg.getBytes());
                            System.out.println(clientID + ": send status - deactivated");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
