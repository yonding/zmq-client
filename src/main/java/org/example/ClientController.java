package org.example;

import org.example.client.*;

import java.util.Scanner;

public class ClientController {
    public static void main(String[] args) throws Exception {
        int num = -1;
        Scanner sc = new Scanner(System.in);
        while (num != 0) {
            System.out.println("========================");
            System.out.println(" 0 : Quit");
            System.out.println(" 1 : ReqRepBasicClient");
            System.out.println(" 2 : PubSubBasicClient");
            System.out.println(" 3 : PubSubAndPullPushClient");
            System.out.println(" 4 : PubSubAndPullPushClientV2");
            System.out.println(" 5 : DealerRouterAsyncClient");
            System.out.println("========================");

            num = sc.nextInt();
            switch (num) {
                case 0:
                    break;
                case 1:
                    ReqRepBasicClient.start();
                    break;
                case 2:
                    PubSubBasicClient.start(new String[]{});
                    break;
                case 3:
                    PubSubAndPullPushClient.start();
                    break;
                case 4:
                    PubSubAndPullPushClientV2.start(new String[]{"client#1"});
                    break;
                case 5:
                    DealerRouterAsyncClient.start();
                    break;
                default:
                    System.out.println("1~5 중에 선택해주세요.\n");
            }
        }
    }
}