package org.example.client;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.util.Random;

public class PubSubAndPullPushClient {
    public static void start() {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.subscribe("".getBytes());
            subscriber.connect("tcp://localhost:5558");

            ZMQ.Socket publisher = context.createSocket(SocketType.PUSH);
            publisher.connect("tcp://localhost:5559");

            ZMQ.Poller poller = context.createPoller(1);
            poller.register(subscriber, ZMQ.Poller.POLLIN);

            Random srandom = new Random(System.currentTimeMillis());
            while (true) {
                try {
                    poller.poll(100);

                    if (poller.pollin(0)) {
                        byte[] message = subscriber.recv();
                        System.out.println("I: received message " + new String(message));
                    } else {
                        int rand = srandom.nextInt(100) + 1;
                        if (rand < 10) {
                            publisher.send(String.valueOf(rand));
                            System.out.println("I: sending message " + rand);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

