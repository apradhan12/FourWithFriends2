import dto.PlayerColor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import dto.GameOverServerMessage;
import dto.GetDropColumnClientMessage;
import dto.GetDropColumnServerMessage;
import dto.RegisterPlayerDropServerMessage;
import dto.SetPlayerColorServerMessage;
import dto.SetPlayerTurnServerMessage;

public class ProxyClient implements IClient {

  private final ObjectInputStream in;
  private final ObjectOutputStream out;

  public ProxyClient(ObjectInputStream in, ObjectOutputStream out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void setPlayerColor(PlayerColor player) {
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
  public void setPlayerTurn(PlayerColor player) {
    try {
      out.writeObject(new SetPlayerTurnServerMessage(player));
      in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Could not send/receive message");
    }
  }

  @Override
  public void registerPlayerDrop(PlayerColor player, int column, int row) {
    try {
      out.writeObject(new RegisterPlayerDropServerMessage(player, column, row));
      in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Could not send/receive message");
    }
  }

  @Override
  public void gameOver(PlayerColor winner) {
    try {
      out.writeObject(new GameOverServerMessage(winner));
      in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw new IllegalStateException("Could not send/receive message");
    }
  }
}
