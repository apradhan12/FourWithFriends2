public class ClientModel implements IClientModel {

  public static final int NUM_ROWS = 6;
  public static final int NUM_COLUMNS = 7;

  private PlayerColor[][] board;
  private PlayerColor playerColor;
  private ConnectionState connectionState;
  private GameStatus gameStatus;

  public ClientModel() {
    initializeBoard();
    playerColor = PlayerColor.None;
    connectionState = ConnectionState.Disconnected;
  }

  @Override
  public PlayerColor[][] getBoard() {
    return board;
  }

  @Override
  public void setBoardCell(int column, int row, PlayerColor color) {
    board[column][row] = color;
  }

  @Override
  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public void setPlayerColor(PlayerColor color) {
    this.playerColor = color;
  }

  @Override
  public ConnectionState getConnectionState() {
    return connectionState;
  }

  @Override
  public void setConnectionState(ConnectionState connectionState) {
    this.connectionState = connectionState;
  }

  @Override
  public GameStatus getGameStatus() {
    return gameStatus;
  }

  @Override
  public void setGameStatus(GameStatus status) {
    this.gameStatus = status;
  }

  private void initializeBoard() {
    board = new PlayerColor[NUM_COLUMNS][NUM_ROWS];
    for (int row = 0; row < NUM_ROWS; row++) {
      for (int col = 0; col < NUM_COLUMNS; col++) {
        board[col][row] = PlayerColor.None;
      }
    }
  }
}
