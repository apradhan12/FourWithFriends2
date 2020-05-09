package client;

import dto.ConnectionState;
import dto.GameStatus;
import dto.PlayerColor;

public interface IClientModel {

  /**
   * The returned board should not be mutated directly, but rather by calling setBoardCell.
   */
  PlayerColor[][] getBoard();

  /**
   * Sets the cell to the given color.
   */
  void setBoardCell(int column, int row, PlayerColor color);

  PlayerColor getPlayerColor();

  void setPlayerColor(PlayerColor color);

  ConnectionState getConnectionState();

  void setConnectionState(ConnectionState connectionState);

  GameStatus getGameStatus();

  void setGameStatus(GameStatus status);

  int getChosenColumn();

  void setChosenColumn(int column);

  boolean isColumnChosen();

  void setColumnIsChosen(boolean columnIsChosen);
}
