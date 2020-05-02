import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import messages.GameOverServerMessage;
import messages.GetDropColumnClientMessage;
import messages.GetDropColumnServerMessage;
import messages.RegisterPlayerDropServerMessage;
import messages.SetPlayerColorServerMessage;
import messages.SetPlayerTurnServerMessage;

public class ProxyClient implements IClient {

  private final ObjectInputStream in;
  private final ObjectOutputStream out;

  public ProxyClient(ObjectInputStream in, ObjectOutputStream out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void setPlayerColor(char player) {
    try {
      out.writeObject(new SetPlayerColorServerMessage(player));
      in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Could not send/receive message");
    }
  }

  @Override
  public int getDropColumn() {
    try {
      out.writeObject(new GetDropColumnServerMessage());
      GetDropColumnClientMessage response = (GetDropColumnClientMessage) in.readObject();
      return response.getColumn();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Could not send/receive message");
    }
  }

  @Override
  public void setPlayerTurn(char player) {
    try {
      out.writeObject(new SetPlayerTurnServerMessage(player));
      in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Could not send/receive message");
    }
  }

  @Override
  public void registerPlayerDrop(char player, int column, int row) {
    try {
      out.writeObject(new RegisterPlayerDropServerMessage(player, column, row));
      in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Could not send/receive message");
    }
  }

  @Override
  public void gameOver(char winner) {
    try {
      out.writeObject(new GameOverServerMessage(winner));
      in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Could not send/receive message");
    }
  }
}
