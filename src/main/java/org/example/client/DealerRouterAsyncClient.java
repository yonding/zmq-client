package org.example.client;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;

public class DealerRouterAsyncClient {

    public static class ClientTask implements Runnable {
        private final String id;

        public ClientTask(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            try (ZContext context = new ZContext()) {
                ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
                String identity = id;
                socket.setIdentity(identity.getBytes(StandardCharsets.US_ASCII));
                socket.connect("tcp://localhost:5570");
                System.out.println("Client " + identity + " started");

                ZMQ.Poller poller = context.createPoller(1);
                poller.register(socket, ZMQ.Poller.POLLIN);

                int reqs = 0;

                while (!Thread.currentThread().isInterrupted()) {
                    reqs++;
                    System.out.println("Req #" + reqs + " sent..");
                    socket.send("request #" + reqs);

                    Thread.sleep(1000);

                    if (poller.poll(1000)>0) {
                        if (poller.pollin(0)) {
                            String msg = socket.recvStr();
                            System.out.println(identity + " received: " + msg);
                        }
                    }
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void start(String[] args) {
        Thread client = new Thread(new ClientTask(args[0]));
        client.start();
    }
}
