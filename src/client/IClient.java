package client;

import dto.PlayerColor;

/**
 * The client does not perform any logic. It simply gives moves to the server
 * and performs actions that the server tells it to do in response. In this way,
 * the client is effectively just the view for the program.
 */
public interface IClient {

  /**
   * Informs the client the color as which they will be playing.
   */
  void setPlayerColor(PlayerColor player);

  /**
   * Gets the column in which the client wishes to drop a token.
   * If the move is invalid, the method will simply be called again.
   */
  int getDropColumn();

  /**
   * Informs the client that it is the given player's turn.
   */
  void setPlayerTurn(PlayerColor player);

  /**
   * Tells the client to set the given position to the given player token.
   */
  void registerPlayerDrop(PlayerColor player, int column, int row);

  /**
   * Informs the client that the game is over and who the winner is.
   * If there is no winner, 'N' will be returned.
   */
  void gameOver(PlayerColor winner);
}
