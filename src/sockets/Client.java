package sockets;

import java.io.*;
import java.net.*;

/**
 *
 * @author darren
 */
public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        int portNum = 11113;

        Socket socket = new Socket("localhost", portNum);

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        
        out.writeObject(50);
                
        String response = (String) in.readObject();

        System.out.println("Server message: " + response);
        
        //socket.close();
        
    }
}
