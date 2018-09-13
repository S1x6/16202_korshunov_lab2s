package main;

import server.ServerThread;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("provide one argument: server port");
            return;
        }

        try {
            int port = Integer.valueOf(args[0]);
            if (port < 1 || port > 65535) throw new NumberFormatException();
            ServerThread server = new ServerThread(port);
            server.start();
        } catch (NumberFormatException ex) {
            System.out.println("wrong argument: provide valid port number");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
