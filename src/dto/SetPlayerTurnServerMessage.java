package dto;

import java.io.Serializable;

public class SetPlayerTurnServerMessage implements Serializable {

  private PlayerColor player;

  public SetPlayerTurnServerMessage(PlayerColor player) {
    this.player = player;
  }

  public PlayerColor getPlayer() {
    return player;
  }
}
