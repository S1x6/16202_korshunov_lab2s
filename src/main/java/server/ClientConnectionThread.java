package server;

import javax.naming.spi.DirectoryManager;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientConnectionThread extends Thread {

    private Socket socket;

    private ObjectInputStream inObj;
    private DataInputStream inData;
    private ObjectOutputStream out;

    public ClientConnectionThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        FileOutputStream fos = null;
        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
            inObj = new ObjectInputStream(inputStream);
            inData = new DataInputStream(new BufferedInputStream(inputStream));
            out = new ObjectOutputStream(socket.getOutputStream());


            String[] metaInfo;


            metaInfo = (String[]) inObj.readObject();


            if (metaInfo != null) {
                System.out.println(metaInfo[0] + " " + metaInfo[1]);
                String fileName = metaInfo[0];
                int fileSize = Integer.valueOf(metaInfo[1]);
                Path uploads = Paths.get("uploads");
                if (!Files.exists(uploads)) {
                    Files.createDirectory(uploads);
                }
                Files.deleteIfExists(Paths.get("uploads/" + fileName));
                Files.createFile(Paths.get("uploads/" + fileName));
                out.writeObject("ok");


                fos = new FileOutputStream("uploads/" + fileName);
                int bufSize = 8000;
                byte[] buffer = new byte[bufSize];
                int count;
                int sum = 0;
                while ((count = inData.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    sum += count;
                }
                System.out.println(fileName + ":  " + fileSize + "/" + sum + " bytes received");
                if (fileSize == sum) {
                    out.writeObject("success");
                } else {
                    out.writeObject("loss");
                }
            }
        } catch (SocketException ex) {
            System.out.println("Connection lost");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

