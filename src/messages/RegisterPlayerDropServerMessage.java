package messages;

public class RegisterPlayerDropServerMessage {
  private char player;
  private int column;
  private int row;

  public RegisterPlayerDropServerMessage(char player, int column, int row) {
    this.player = player;
    this.column = column;
    this.row = row;
  }

  public char getPlayer() {
    return player;
  }

  public int getColumn() {
    return column;
  }

  public int getRow() {
    return row;
  }
}
