package messages;

public class SetPlayerTurnServerMessage {
  private char player;

  public SetPlayerTurnServerMessage(char player) {
    this.player = player;
  }

  public char getPlayer() {
    return player;
  }
}
