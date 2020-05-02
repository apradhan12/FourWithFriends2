import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import messages.GameOverClientMessage;
import messages.GameOverServerMessage;
import messages.GetDropColumnClientMessage;
import messages.GetDropColumnServerMessage;
import messages.RegisterPlayerDropClientMessage;
import messages.RegisterPlayerDropServerMessage;
import messages.SetPlayerColorClientMessage;
import messages.SetPlayerColorServerMessage;
import messages.SetPlayerTurnClientMessage;
import messages.SetPlayerTurnServerMessage;

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
        out.writeObject(new SetPlayerColorClientMessage());
      } else if (data instanceof GetDropColumnServerMessage) {
        GetDropColumnServerMessage message = (GetDropColumnServerMessage) data;
        int column = client.getDropColumn();
        out.writeObject(new GetDropColumnClientMessage(column));
      } else if (data instanceof SetPlayerTurnServerMessage) {
        SetPlayerTurnServerMessage message = (SetPlayerTurnServerMessage) data;
        client.setPlayerTurn(message.getPlayer());
        out.writeObject(new SetPlayerTurnClientMessage());
      } else if (data instanceof RegisterPlayerDropServerMessage) {
        RegisterPlayerDropServerMessage message = (RegisterPlayerDropServerMessage) data;
        client.registerPlayerDrop(message.getPlayer(), message.getColumn(), message.getRow());
        out.writeObject(new RegisterPlayerDropClientMessage());
      } else if (data instanceof GameOverServerMessage) {
        GameOverServerMessage message = (GameOverServerMessage) data;
        client.gameOver(message.getWinner());
        out.writeObject(new GameOverClientMessage());
        return false;
      }
      return true;
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Failed to read/send message");
    }
  }
}
