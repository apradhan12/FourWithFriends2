package com.fourwithfriends.dto;

import java.io.Serializable;

public class GameOverServerMessage implements Serializable {

  private PlayerColor winner;

  public GameOverServerMessage(PlayerColor winner) {
    this.winner = winner;
  }

  public PlayerColor getWinner() {
    return winner;
  }
}
