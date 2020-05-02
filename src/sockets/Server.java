package sockets;

// Keeping imports easy to remember.
import java.io.*;
import java.net.*;

public class Server {

    private static final int PORT_NUM = 11113;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        try (ServerSocket listener = new ServerSocket(PORT_NUM)) {
            System.out.println("Server is now running at port: " + PORT_NUM);
            // Accept a client connection once Server recieves one.
            Socket clientSocket = listener.accept();
            Socket clientSocket2 = listener.accept();

            // Creating inout and output streams. Must creat out put stream first.
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out2 = new ObjectOutputStream(clientSocket2.getOutputStream());
            ObjectInputStream in2 = new ObjectInputStream(clientSocket2.getInputStream());

            // Reading in Integer Object from input stream.
            int i = (Integer) in.readObject();
            int i2 = (Integer) in2.readObject();

            //Sending response back to client
            String response = "Integer Object Received.";
            out.writeObject(response);
            out2.writeObject(response);

            // Outputting recieved Integer Object.
            System.out.println("Received integer: " + i);
            System.out.println("Received integer: " + i2);
            out.close();
            in.close();
            out2.close();
            in2.close();
            clientSocket.close();
            clientSocket2.close();
        }
    }

}
