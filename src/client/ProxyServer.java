package client;

import dto.EmptyClientMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import dto.GameOverServerMessage;
import dto.GetDropColumnClientMessage;
import dto.GetDropColumnServerMessage;
import dto.RegisterPlayerDropServerMessage;
import dto.SetPlayerColorServerMessage;
import dto.SetPlayerTurnServerMessage;

public class ProxyServer {

  private final ObjectInputStream in;
  private final ObjectOutputStream out;
  private final IClient client;

  public ProxyServer(String host, int port, IClient client) {
    if (host == null) {
      host = "localhost";
    }
    this.client = client;
    try {
      Socket socket = new Socket(host, port);
      this.out = new ObjectOutputStream(socket.getOutputStream());
      this.in = new ObjectInputStream(socket.getInputStream());
      runServer();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Failed to connect to server");
    }
  }

  private void runServer() {
    boolean executeLoop = true;
    while (executeLoop) {
      executeLoop = readServerStream();
    }
  }

  /**
   * Returns whether to continue executing the loop.
   */
  private boolean readServerStream() {
    try {
      Object data = in.readObject();
      if (data instanceof SetPlayerColorServerMessage) {
        SetPlayerColorServerMessage message = (SetPlayerColorServerMessage) data;
        client.setPlayerColor(message.getPlayer());
        out.writeObject(new EmptyClientMessage());
      } else if (data instanceof GetDropColumnServerMessage) {
        int column = client.getDropColumn();
        out.writeObject(new GetDropColumnClientMessage(column));
      } else if (data instanceof SetPlayerTurnServerMessage) {
        SetPlayerTurnServerMessage message = (SetPlayerTurnServerMessage) data;
        client.setPlayerTurn(message.getPlayer());
        out.writeObject(new EmptyClientMessage());
      } else if (data instanceof RegisterPlayerDropServerMessage) {
        RegisterPlayerDropServerMessage message = (RegisterPlayerDropServerMessage) data;
        client.registerPlayerDrop(message.getPlayer(), message.getColumn(), message.getRow());
        out.writeObject(new EmptyClientMessage());
      } else if (data instanceof GameOverServerMessage) {
        GameOverServerMessage message = (GameOverServerMessage) data;
        client.gameOver(message.getWinner());
        out.writeObject(new EmptyClientMessage());
        return false;
      }
      return true;
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Failed to read/send message");
    }
  }
}
