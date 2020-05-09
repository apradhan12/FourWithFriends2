import java.net.*;
import java.io.*;
import java.util.Arrays;

/**
 * Server code for FourWithFriends.
 *
 * @author Thomas Chenevey, Andrew Korchowsky, Aaron Pradhan
 * @version 2.0
 */

public class Server {

  private static final char CLIENT1_COLOR = 'O';
  private static final char CLIENT2_COLOR = 'B';
  private static final int NUM_COLUMNS = 7;
  private static final int NUM_ROWS = 6;

  private static final int PORT_NUM = 16789;

  public static void main(String[] args) {
    new Server().runServer();
  }

  public void runServer() {
    try (ServerSocket listener = new ServerSocket(PORT_NUM)) {
      System.out.println("Server is now running at port: " + PORT_NUM);
      Socket clientSocket1 = listener.accept();
      Socket clientSocket2 = listener.accept();
      ObjectOutputStream out1 = new ObjectOutputStream(clientSocket1.getOutputStream());
      ObjectInputStream in1 = new ObjectInputStream(clientSocket1.getInputStream());
      ObjectOutputStream out2 = new ObjectOutputStream(clientSocket2.getOutputStream());
      ObjectInputStream in2 = new ObjectInputStream(clientSocket2.getInputStream());
      IClient client1 = new ProxyClient(in1, out1);
      IClient client2 = new ProxyClient(in2, out2);
      runGame(client1, client2);
      out1.close();
      in1.close();
      out2.close();
      in2.close();
      clientSocket1.close();
      clientSocket2.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("IO failed");
    }
  }

  private static char[][] getInitialBoard() {
    char[][] board = new char[NUM_COLUMNS][NUM_ROWS];
    for (int col = 0; col < NUM_COLUMNS; col++) {
      for (int row = 0; row < NUM_ROWS; row++) {
        board[col][row] = 'N';
      }
    }
    return board;
  }

  private static boolean isBoardFull(char[][] board) {
    for (int col = 0; col < NUM_COLUMNS; col++) {
      for (int row = 0; row < NUM_ROWS; row++) {
        if (board[col][row] == 'N') {
          return false;
        }
      }
    }
    return true;
  }

  private static boolean isColumnFull(char[][] board, int column) {
    for (char cell : board[column]) {
      if (cell == 'N') {
        return false;
      }
    }
    return true;
  }

  private static boolean isLegalMove(char[][] board, int column) {
    if (column < 0 || column > NUM_COLUMNS) {
      return false;
    }
    return !isColumnFull(board, column);
  }

  /**
   * Returns the row in which the token was placed.
   */
  private static int placePlayerToken(char[][] board, char player, int column) {
    // getRowNum returns the highest row with a token in it, so must add 1 to get blank spot
    int row = WinChecker.getRowNum(board, column) + 1;
    board[column][row] = player;
    return row;
  }

  /**
   * Returns true if the player won, false otherwise.
   */
  private static boolean playTurn(char[][] board, IClient activePlayer, IClient opponent,
      char activePlayerColor) {
    activePlayer.setPlayerTurn(activePlayerColor);
    opponent.setPlayerTurn(activePlayerColor);
    int column = activePlayer.getDropColumn();
    while (!isLegalMove(board, column)) {
      column = activePlayer.getDropColumn();
    }
    int row = placePlayerToken(board, activePlayerColor, column);
    activePlayer.registerPlayerDrop(activePlayerColor, column, row);
    opponent.registerPlayerDrop(activePlayerColor, column, row);
    System.out.println(String.format("The player placed in column %s, row %s", column, row));
    System.out.println("Here is the new state of the board: " + Arrays.deepToString(board));
    if (WinChecker.checkWin(board, column)) {
      activePlayer.gameOver(activePlayerColor);
      opponent.gameOver(activePlayerColor);
      return true;
    }
    return false;
  }

  public static void runGame(IClient client1, IClient client2) {
    char[][] board = getInitialBoard();
    client1.setPlayerColor(CLIENT1_COLOR);
    client2.setPlayerColor(CLIENT2_COLOR);
    boolean isWin = false;
    while (!isBoardFull(board)) {
     isWin = playTurn(board, client1, client2, CLIENT1_COLOR);
      if (isWin) {
        System.out.println("Win for client 1");
        break;
      }
      isWin = playTurn(board, client2, client1, CLIENT2_COLOR);
      if (isWin) {
        System.out.println("Win for client 2");
        break;
      }
    }
    System.out.println("Is the board full? " + isBoardFull(board));
    // can have board full and win at the same time, so need to store isWin boolean
    if (!isWin) {
      client1.gameOver('N');
      client2.gameOver('N');
    }
  }
}
