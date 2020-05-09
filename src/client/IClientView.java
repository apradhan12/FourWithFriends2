package client;

public interface IClientView {

  void updateGameStatus();

  void updateBoardState();

  void updatePlayerColor();

  void updateConnectionState();

  void render();
}
