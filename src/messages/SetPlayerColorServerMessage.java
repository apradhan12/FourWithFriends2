package messages;

public class SetPlayerColorServerMessage {
  private char player;

  public SetPlayerColorServerMessage(char player) {
    this.player = player;
  }

  public char getPlayer() {
    return player;
  }
}
