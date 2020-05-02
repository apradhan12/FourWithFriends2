package messages;

import java.io.Serializable;

public class SetPlayerTurnServerMessage implements Serializable {
  private char player;

  public SetPlayerTurnServerMessage(char player) {
    this.player = player;
  }

  public char getPlayer() {
    return player;
  }
}
