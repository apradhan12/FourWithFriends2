package messages;

public class GameOverServerMessage {
  private char winner;

  public GameOverServerMessage(char winner) {
    this.winner = winner;
  }

  public char getWinner() {
    return winner;
  }
}
