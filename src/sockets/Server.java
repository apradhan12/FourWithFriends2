package sockets;

// Keeping imports easy to remember.
import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Port number to bind server to.
        int portNum = 11113;
        
        // Socket for server to listen at.
        ServerSocket listener = new ServerSocket(portNum);

        System.out.println("Server is now running at port: " + portNum);

        // Simply making Server run continously.
        while (true) {
            try {
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
                break;
            } finally {
                      // Closing Server Socket now.
                      listener.close();
               
            }
            
             
                
        }
    }

}
