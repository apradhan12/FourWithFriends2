package dto;

import java.io.Serializable;

public class SetPlayerColorServerMessage implements Serializable {

  private PlayerColor player;

  public SetPlayerColorServerMessage(PlayerColor player) {
    this.player = player;
  }

  public PlayerColor getPlayer() {
    return player;
  }
}
