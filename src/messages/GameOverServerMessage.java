package messages;

import java.io.Serializable;

public class GameOverServerMessage implements Serializable {
  private char winner;

  public GameOverServerMessage(char winner) {
    this.winner = winner;
  }

  public char getWinner() {
    return winner;
  }
}
