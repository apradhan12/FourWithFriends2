import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Client code for FourWithFriends.
 */
public class Client extends JFrame implements ActionListener, IClient {

  private static final int NUM_ROWS = 6;
  private static final int NUM_COLUMNS = 7;

  private int chosenColumn = -1;
  private volatile boolean columnIsChosen = false;
  char playerColor;
  char[][] board = new char[NUM_COLUMNS][NUM_ROWS];

  JPanel containerPanel = new JPanel();
  JLabel status = new JLabel("Waiting for game to start...");
  JPanel mainGrid = new JPanel(new GridLayout(0, 7));
  private JMenuItem mConnect;
  private JMenuItem mExit;
  private static ImageIcon white;
  private static ImageIcon blue;
  private static ImageIcon orange;

  static {
    try {
      white = new ImageIcon(ImageIO.read(Client.class.getResource("/white64.png")));
      blue = new ImageIcon(ImageIO.read(Client.class.getResource("/blue64.png")));
      orange = new ImageIcon(ImageIO.read(Client.class.getResource("/orange64.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static final Map<Character, ImageIcon> CHARACTER_MAP = Map.of('B', blue,
      'O', orange, 'N', white);

  JButton[] columnButtons = new JButton[NUM_COLUMNS];

  //array for local board state
  JLabel[][] guiBoard = new JLabel[NUM_COLUMNS][NUM_ROWS];

  //constructor
  public Client() {
    containerPanel.setLayout(new BorderLayout());
    containerPanel.add(status, BorderLayout.NORTH);
    status.setHorizontalAlignment(SwingConstants.LEFT);
    setJMenuBar(createMenuBar());
    mainGrid.setBackground(Color.BLACK);
    containerPanel.add(mainGrid, BorderLayout.CENTER);
    add(containerPanel);

    //add objects to main grid
    initializeGridCells();
    initializeColumnButtons();

    playerColor = 'N';
  }

  private JMenuBar createMenuBar() {
    JMenuBar mBar = new JMenuBar();
    JMenu mGame = new JMenu("Game");
    mBar.add(mGame);
    mConnect = new JMenuItem("Connect");
    mConnect.addActionListener(this);
    mGame.add(mConnect);
    JMenuItem mHelp = new JMenuItem("Help");
    mHelp.addActionListener(this);
    mGame.add(mHelp);
    mExit = new JMenuItem("Exit");
    mExit.addActionListener(this);
    mGame.add(mExit);
    return mBar;
  }

  private void initializeGridCells() {
    for (int row = NUM_ROWS - 1; row >= 0; row--) {
      for (int col = 0; col < NUM_COLUMNS; col++) {
        guiBoard[col][row] = new JLabel(white);
        mainGrid.add(guiBoard[col][row]);
      }
    }
  }

  private void initializeColumnButtons() {
    for (int col = 0; col < NUM_COLUMNS; col++) {
      columnButtons[col] = new JButton("Drop Piece");
      mainGrid.add(columnButtons[col]);
      columnButtons[col].addActionListener(this);
    }
  }

  private void connectToServer(String host, int port) {
    System.out.println(String.format("Connecting to %s:%s", host, port));
    Thread proxyServerThread = new Thread(() -> new ProxyServer(host, port, this));
    proxyServerThread.start();
  }

  //ActionListener things
  public void actionPerformed(ActionEvent event) {
    System.out.println("An action was performed");

    //set object
    Object obj = event.getSource();

    if (obj == mConnect) {
      String stPort = JOptionPane
          .showInputDialog(null, "Input Server Port \n Or click OK for default", "16789");
      int port = Integer.parseInt(stPort);
      String ip = JOptionPane
          .showInputDialog(null, "Input Server IP \n Or Click OK for default", "localhost");
      connectToServer(ip, port);
    }
    if (obj == mExit) {
      System.exit(0);
    }

    //button 1
    for (int col = 0; col < NUM_COLUMNS; col++) {
      if (obj == columnButtons[col]) {
        drop(col);
      }
    }
  }

  //main
  public static void main(String[] args) {
    Client frame = new Client();
    frame.setTitle("FourWithFriends");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(700, 600);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  //methods
  @Override
  public void setPlayerColor(char _c) {
    playerColor = _c;
  }

  public void playSoundEffect() {
    try {
      AudioInputStream audioInputStream = AudioSystem
          .getAudioInputStream(getClass().getResource("/drop.wav"));
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();
    } catch (Exception ex) {
      System.out.println("Error with playing sound.");
      ex.printStackTrace();
    }
  }

  synchronized void drop(int column) {
    System.out.println("Dropped in column " + column);
    chosenColumn = column;
    columnIsChosen = true;
    System.out.println("Notifying all threads of the change");
    notifyAll();
  }

  @Override
  public synchronized int getDropColumn() {
    columnIsChosen = false;
    System.out.println("Getting the drop column");
    while (!columnIsChosen) {
      System.out.println("Beginning to wait");
      try {
        wait();
      } catch (InterruptedException e) {
        System.out.println("Interrupted");
      }
    }
    System.out.println("Got the drop column: " + chosenColumn);
    return chosenColumn;
  }

  @Override
  public void setPlayerTurn(char player) {
    if (player == playerColor) {
      status.setText("It's your turn");
    } else {
      status.setText("It's the opponent's turn");
    }
  }

  @Override
  public void registerPlayerDrop(char player, int column, int row) {
    guiBoard[column][row].setIcon(CHARACTER_MAP.get(player));
    board[column][row] = player;
    System.out.println("Here is Board: " + Arrays.deepToString(board));
    playSoundEffect();
  }

  @Override
  public void gameOver(char winner) {
    if (winner == playerColor) {
      status.setText("You won!");
    } else if (winner != 'N') {
      status.setText("You lost.");
    } else {
      status.setText("It's a tie.");
    }
  }

}
