package com.fourwithfriends.client;

public interface IClientView {

  void updateGameStatus();

  void updateBoardState();

  void updatePlayerColor();

  void updateConnectionState();

  void render();

  void setModel(IClientModel model);

  void setController(IClientController controller);
}
