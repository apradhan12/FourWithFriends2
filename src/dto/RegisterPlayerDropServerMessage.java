package dto;

import java.io.Serializable;

public class RegisterPlayerDropServerMessage implements Serializable {
  private PlayerColor player;
  private int column;
  private int row;

  public RegisterPlayerDropServerMessage(PlayerColor player, int column, int row) {
    this.player = player;
    this.column = column;
    this.row = row;
  }

  public PlayerColor getPlayer() {
    return player;
  }

  public int getColumn() {
    return column;
  }

  public int getRow() {
    return row;
  }
}
