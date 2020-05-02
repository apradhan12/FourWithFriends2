package messages;

import java.io.Serializable;

public class SetPlayerColorServerMessage implements Serializable {
  private char player;

  public SetPlayerColorServerMessage(char player) {
    this.player = player;
  }

  public char getPlayer() {
    return player;
  }
}
