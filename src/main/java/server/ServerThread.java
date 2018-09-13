package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

    private ServerSocket serverSocket;

    public ServerThread(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientConnectionThread clientConnectionThread = new ClientConnectionThread(socket);
                clientConnectionThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
