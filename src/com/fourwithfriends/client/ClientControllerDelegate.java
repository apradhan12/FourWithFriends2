package com.fourwithfriends.client;

import com.fourwithfriends.dto.ConnectionState;
import com.fourwithfriends.dto.GameStatus;
import com.fourwithfriends.dto.PlayerColor;

public class ClientControllerDelegate implements IClient, IClientController {

    private IClientModel model;
    private IClientView view;

    public ClientControllerDelegate() {}

    public void setModel(IClientModel model) {
        this.model = model;
    }

    public void setView(IClientView view) {
        this.view = view;
    }

    public void run() {
        view.render();
    }

    @Override
    public void setPlayerColor(PlayerColor color) {
        model.setPlayerColor(color);
        model.setConnectionState(ConnectionState.InGame);
        view.updatePlayerColor();
        view.updateConnectionState();
    }

    @Override
    public synchronized int getDropColumn() {
        model.setColumnIsChosen(false);
        System.out.println("Getting the drop column");
        while (!model.isColumnChosen()) {
            System.out.println("Beginning to wait");
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        System.out.println("Got the drop column: " + model.getChosenColumn());
        return model.getChosenColumn();
    }

    @Override
    public void setPlayerTurn(PlayerColor color) {
        if (color == model.getPlayerColor()) {
            model.setGameStatus(GameStatus.PlayerTurn);
        } else {
            model.setGameStatus(GameStatus.OpponentTurn);
        }
        view.updateGameStatus();
    }

    @Override
    public void registerPlayerDrop(PlayerColor color, int column, int row) {
        model.setBoardCell(column, row, color);
        view.updateBoardState();
    }

    @Override
    public void gameOver(PlayerColor winner) {
        if (winner == model.getPlayerColor()) {
            model.setGameStatus(GameStatus.Win);
        } else if (winner != PlayerColor.None) {
            model.setGameStatus(GameStatus.Loss);
        } else {
            model.setGameStatus(GameStatus.Draw);
        }
        view.updateGameStatus();
    }

    @Override
    public void connectToServer(String host, int port) {
        System.out.println(String.format("Connecting to %s:%s", host, port));
        Thread proxyServerThread = new Thread(() -> {
            try {
                new ProxyServer(host, port, this);
            } catch (IllegalStateException e) {
                System.out.println("Whoops");
            }
        });
        proxyServerThread.start();

        model.setConnectionState(ConnectionState.WaitingForOpponent);
        view.updateConnectionState();
    }

    @Override
    public synchronized void dropToken(int column) {
        System.out.println("Dropped in column " + column);
        model.setChosenColumn(column);
        model.setColumnIsChosen(true);
        System.out.println("Notifying all threads of the change");
        notifyAll();
    }
}
