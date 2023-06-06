package org.example.client;

import java.util.StringTokenizer;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import static java.lang.Thread.sleep;

public class PubSubBasicClient {
    public static void start(String args[]) {
        try (ZContext context = new ZContext()) {
            //  Socket to talk to server
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);

            System.out.println("Collecting updates from weather server");
            subscriber.connect("tcp://localhost:5556");

            //  Subscribe to zipcode, default is NYC, 10001
            String filter = (args.length > 0) ? args[0] : "10001";
            subscriber.subscribe(filter.getBytes(ZMQ.CHARSET));

            //  Process 20 updates
            long total_temp = 0;
            int update_nbr;
            for (update_nbr = 0; update_nbr < 20; update_nbr++) {
                //  Use trim to remove the tailing '0' character
                String string = subscriber.recvStr(0).trim();

                StringTokenizer sscanf = new StringTokenizer(string, " ");
                int zipcode = Integer.valueOf(sscanf.nextToken());
                int temperature = Integer.valueOf(sscanf.nextToken());
                int relhumidity = Integer.valueOf(sscanf.nextToken());

                System.out.println(
                        "Receive temperature for zipcode '" + zipcode
                                + "' was " + temperature + " F"
                );
                total_temp += temperature;
                sleep(100);
            }

            System.out.println(
                    String.format(
                            "Average temperature for zipcode '%s' was %.2f F.",
                            filter,
                            (double) total_temp / (update_nbr + 1)
                    )
            );

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}