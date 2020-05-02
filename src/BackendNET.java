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
  private static final char[] PLAYER_TURN_ORDER = {CLIENT1_COLOR, CLIENT2_COLOR};

  /**
   * The client indexes rows with 0 at the top, but the server has 0 at the bottom.
   * This is an involutory function, i.e. when performed twice returns the original result,
   * so the method is the same for converting from client to server row numbers as server to
   * client row numbers.
   */
  private int convertRowNum(int clientRowNum) {
    return (NUM_ROWS - 1) - clientRowNum;
  }

  private char[][] getInitialBoard() {
    char[][] board = new char[NUM_COLUMNS][NUM_ROWS];
    for (int col = 0; col < NUM_COLUMNS; col++) {
      for (int row = 0; row < NUM_ROWS; row++) {
        board[col][row] = 'N';
      }
    }
    return board;
  }

  private boolean isBoardFull(char[][] board) {
    for (int col = 0; col < NUM_COLUMNS; col++) {
      for (int row = 0; row < NUM_ROWS; row++) {
        if (board[col][row] != 'N') {
          return false;
        }
      }
    }
    return true;
  }

  public void runGame(IClient client1, IClient client2) {
    char[][] board = getInitialBoard();
    client1.setPlayerColor(CLIENT1_COLOR);
    client2.setPlayerColor(CLIENT2_COLOR);
    while (!isBoardFull(board) && )

  }

  /**
   * The following is roughly the code needed to implement win checker The code below cannot be
   * implemented, since the method cannot return multiple things If there is a way to send clients
   * messages from the code below, that would fix this
   * <p>
   * Currently, the method recieves the board and the player's move in the form of the index of the
   * column that player played into, and returns the updated board state. This method would
   * theoretically be able to be implemented in a place where it has access to some way to update
   * board, and send a message to either player. I am extremely sorry for leaving this mess, but
   * hopefully it is helpful to you in some way. IF THIS CODE IS NOT HELPFUL, PLEASE DELETE IT.
   **/
  public char[][] receiveMove(char[][] board, int column, char playerColor) {
    /**
     * Checks for the next free space in the column that was played, and updates that with
     * the color of the player who just played. If the column is full, this determines that.
     **/
    int rowIndex = 0;
    char nextChar = board[column][rowIndex];
    while (rowIndex < 5 && nextChar != 'N') {
      nextChar = board[column][rowIndex + 1];
      rowIndex += 1;
    }
    if (rowIndex == 5 && nextChar != 'N') {
      /**
       * In this case, a player attempted to place a piece in a filled column. Implement the proper
       * procedure for telling the client that it did this here
       */
      return board;
    } else {
      board[column][rowIndex] = playerColor;
      /**
       * I could not get java to recognize my class, and I don't know what I was doing wrong. This is where
       * wins are supposed to be tested for. The method definitely works, and this is how it is supposed to
       * be implemented, but for some reason java doesn't see the Connect4WinChecker class.
       if(new Connect4WinChecker().checkWin(board, column)){
       //Implement winning procedure here (What to do if a player won using this move)
       return board;
       }
       **/
      return board;
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
