package org.example.client;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class ReqRepBasicClient {
    public static void start() {
        try (ZContext context = new ZContext()) {
            //  Socket to talk to server
            System.out.println("Connecting to hello world server...");

            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");

            // Do 10 requests, waiting each time for a response
            for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
                String request = "Hello";
                System.out.println("Sending Request " + requestNbr);
                socket.send(request.getBytes(ZMQ.CHARSET), 0);

                // Get the reply
                byte[] reply = socket.recv(0);
                System.out.println(
                        "Received reply "+ requestNbr+" [ " + new String(reply, ZMQ.CHARSET) + " ]"
                );
            }
        }
    }
}