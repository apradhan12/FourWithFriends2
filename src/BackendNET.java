import java.net.*;
import java.io.*;

/**
 * ISTE-121 final project backend multithreaded server code
 *
 * @author Thomas Chenevey, Andrew Korchowsky
 * @version 1.1
 * @date: 4/27/2020
 * @project: four with friends
 */

public class BackendNET {

  public static void main(String[] args) {
    new BackendNET();
  }

  //creating src.BackendNET constructor
  public BackendNET() {
    ServerSocket ss = null;
    //Initializes a blank connect4 board
    char[][] board = {{'N', 'N', 'N', 'N', 'N', 'N'}, {'N', 'N', 'N', 'N', 'N', 'N'},
        {'N', 'N', 'N', 'N', 'N', 'N'}, {'N', 'N', 'N', 'N', 'N', 'N'},
        {'N', 'N', 'N', 'N', 'N', 'N'},
        {'N', 'N', 'N', 'N', 'N', 'N'}, {'N', 'N', 'N', 'N', 'N', 'N'}};

    // getting the addresses of the localhost and the computer
    try {
      System.out.println("getLocalHost: " + InetAddress.getLocalHost());
      System.out.println("getByName:    " + InetAddress.getByName("localhost"));

      ss = new ServerSocket(16789);
      Socket cs = null;
      System.out.println("Server Started");

      //after socket is created the server waits for a client to be accepted
      System.out.println("Waiting for a client...");
      while (true) {
        cs = ss.accept();
        System.out.println("src.Client accepted");
        ThreadServer ts = new ThreadServer(cs);
        ts.start();
      }
    } catch (BindException be) {
      System.out.println("Server running, stopping");
    } catch (IOException ioe) {
      System.out.println("IO Exception has occured inside catch (constructor)");
      ioe.printStackTrace();
    }
  }

  private static final char CLIENT1_COLOR = 'O';
  private static final char CLIENT2_COLOR = 'B';
  private static final int NUM_COLUMNS = 7;
  private static final int NUM_ROWS = 6;

  /**
   * The client indexes rows with 0 at the top, but the server has 0 at the bottom.
   * This is an involutory function, i.e. when performed twice returns the original result,
   * so the method is the same for converting from client to server row numbers as server to
   * client row numbers.
   */
  private static int convertRowNum(int clientRowNum) {
    return (NUM_ROWS - 1) - clientRowNum;
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
        if (board[col][row] != 'N') {
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
    int row = Connect4WinChecker.getRowNum(board, column) + 1;
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
    activePlayer.registerPlayerDrop(activePlayerColor, column, convertRowNum(row));
    opponent.registerPlayerDrop(activePlayerColor, column, convertRowNum(row));
    if (Connect4WinChecker.checkWin(board, column)) {
      activePlayer.gameOver(activePlayerColor);
      opponent.gameOver(activePlayerColor);
      return true;
    }
    return false;
  }

  public void runGame(IClient client1, IClient client2) {
    char[][] board = getInitialBoard();
    client1.setPlayerColor(CLIENT1_COLOR);
    client2.setPlayerColor(CLIENT2_COLOR);
    boolean isWin = false;
    while (!isBoardFull(board)) {
     isWin = playTurn(board, client1, client2, CLIENT1_COLOR);
      if (isWin) {
        break;
      }
      isWin = playTurn(board, client2, client1, CLIENT2_COLOR);
      if (isWin) {
        break;
      }
    }
    // can have board full and win at the same time, so need to store isWin boolean
    if (!isWin) {
      client1.gameOver('N');
      client2.gameOver('N');
    }
  }

  //Threadserver class
  class ThreadServer extends Thread {

    Socket cs;

    public ThreadServer(Socket cs) {
      this.cs = cs;
    }

    //run method that handles receiving messages from client as well as sending messages back
    public void run() {
      BufferedReader br;
      PrintWriter pw;
      String clientMsg;
      Object clientData;
      PrintStream ps;
      ObjectInputStream ois;
      ObjectOutputStream os;

      try {
        br = new BufferedReader(new InputStreamReader(cs.getInputStream()));

        pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()));

        ois = new ObjectInputStream(cs.getInputStream());
        os = new ObjectOutputStream(cs.getOutputStream());

        clientMsg = br.readLine();
        clientData = ois.readObject();
        System.out.println("Server read: " + clientMsg);
        pw.println(clientMsg.toUpperCase());
        os.writeObject(clientData);

        pw.flush();
        os.flush();
      } catch (IOException ioe) {
        System.out.println("IO exception occurred inside catch");
        ioe.printStackTrace();
      } catch (ClassNotFoundException cnf) {

        System.out.println("Class not found");
        cnf.printStackTrace();

      }

    } //run method
  } //ThreadServer
}//src.BackendNET
