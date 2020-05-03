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

  // create variables
  JPanel containerPanel = new JPanel();
  JLabel status = new JLabel("Waiting for game to start...");

  JPanel mainGrid = new JPanel(new GridLayout(0, 7));

  //misc variables
  char playerColor;

  //array for local board state
  char[][] board = {
      {'N', 'N', 'N', 'N', 'N', 'N', 'N'},
      {'N', 'N', 'N', 'N', 'N', 'N', 'N'},
      {'N', 'N', 'N', 'N', 'N', 'N', 'N'},
      {'N', 'N', 'N', 'N', 'N', 'N', 'N'},
      {'N', 'N', 'N', 'N', 'N', 'N', 'N'},
      {'N', 'N', 'N', 'N', 'N', 'N', 'N'},
  };

  private JMenuItem mConnect;
  private JMenuItem mExit;

  //icons
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

  //Labels
  //Row 1 (starting from the bottom)
  JLabel R1C1 = new JLabel(white);
  JLabel R1C2 = new JLabel(white);
  JLabel R1C3 = new JLabel(white);
  JLabel R1C4 = new JLabel(white);
  JLabel R1C5 = new JLabel(white);
  JLabel R1C6 = new JLabel(white);
  JLabel R1C7 = new JLabel(white);

  //Row 2
  JLabel R2C1 = new JLabel(white);
  JLabel R2C2 = new JLabel(white);
  JLabel R2C3 = new JLabel(white);
  JLabel R2C4 = new JLabel(white);
  JLabel R2C5 = new JLabel(white);
  JLabel R2C6 = new JLabel(white);
  JLabel R2C7 = new JLabel(white);

  //Row 3
  JLabel R3C1 = new JLabel(white);
  JLabel R3C2 = new JLabel(white);
  JLabel R3C3 = new JLabel(white);
  JLabel R3C4 = new JLabel(white);
  JLabel R3C5 = new JLabel(white);
  JLabel R3C6 = new JLabel(white);
  JLabel R3C7 = new JLabel(white);

  //Row 4
  JLabel R4C1 = new JLabel(white);
  JLabel R4C2 = new JLabel(white);
  JLabel R4C3 = new JLabel(white);
  JLabel R4C4 = new JLabel(white);
  JLabel R4C5 = new JLabel(white);
  JLabel R4C6 = new JLabel(white);
  JLabel R4C7 = new JLabel(white);

  //Row 5
  JLabel R5C1 = new JLabel(white);
  JLabel R5C2 = new JLabel(white);
  JLabel R5C3 = new JLabel(white);
  JLabel R5C4 = new JLabel(white);
  JLabel R5C5 = new JLabel(white);
  JLabel R5C6 = new JLabel(white);
  JLabel R5C7 = new JLabel(white);

  //Row 6
  JLabel R6C1 = new JLabel(white);
  JLabel R6C2 = new JLabel(white);
  JLabel R6C3 = new JLabel(white);
  JLabel R6C4 = new JLabel(white);
  JLabel R6C5 = new JLabel(white);
  JLabel R6C6 = new JLabel(white);
  JLabel R6C7 = new JLabel(white);

  //Buttons (starting from the left)
  JButton C1Button = new JButton("Drop Piece");
  JButton C2Button = new JButton("Drop Piece");
  JButton C3Button = new JButton("Drop Piece");
  JButton C4Button = new JButton("Drop Piece");
  JButton C5Button = new JButton("Drop Piece");
  JButton C6Button = new JButton("Drop Piece");
  JButton C7Button = new JButton("Drop Piece");

  //array for local board state
  JLabel[][] guiBoard = {
      {R6C1, R6C2, R6C3, R6C4, R6C5, R6C6, R6C7},
      {R5C1, R5C2, R5C3, R5C4, R5C5, R5C6, R5C7},
      {R4C1, R4C2, R4C3, R4C4, R4C5, R4C6, R4C7},
      {R3C1, R3C2, R3C3, R3C4, R3C5, R3C6, R3C7},
      {R2C1, R2C2, R2C3, R2C4, R2C5, R2C6, R2C7},
      {R1C1, R1C2, R1C3, R1C4, R1C5, R1C6, R1C7}
  };


  //constructor
  public Client() {
    containerPanel.setLayout(new BorderLayout());
    containerPanel.add(status, BorderLayout.NORTH);
    status.setHorizontalAlignment(SwingConstants.LEFT);

    //Create the menu bar.
    //items for menu
    JMenuBar mBar = new JMenuBar();

    //Build the file menu.
    JMenu mGame = new JMenu("Game");
    mBar.add(mGame);

    //make and add JMenuItems
    mConnect = new JMenuItem("Connect");
    mConnect.addActionListener(this);
    mGame.add(mConnect);

    JMenuItem mHelp = new JMenuItem("Help");
    mHelp.addActionListener(this);
    mGame.add(mHelp);

    mExit = new JMenuItem("Exit");
    mExit.addActionListener(this);
    mGame.add(mExit);

    //set menu bar
    setJMenuBar(mBar);

    //main grid
    mainGrid.setBackground(new Color(0, 0, 0));

    containerPanel.add(mainGrid, BorderLayout.CENTER);
    add(containerPanel);

    //add objects to main grid
    mainGrid.add(C1Button);
    C1Button.addActionListener(this);
    mainGrid.add(C2Button);
    C2Button.addActionListener(this);
    mainGrid.add(C3Button);
    C3Button.addActionListener(this);
    mainGrid.add(C4Button);
    C4Button.addActionListener(this);
    mainGrid.add(C5Button);
    C5Button.addActionListener(this);
    mainGrid.add(C6Button);
    C6Button.addActionListener(this);
    mainGrid.add(C7Button);
    C7Button.addActionListener(this);

    mainGrid.add(R6C1);
    mainGrid.add(R6C2);
    mainGrid.add(R6C3);
    mainGrid.add(R6C4);
    mainGrid.add(R6C5);
    mainGrid.add(R6C6);
    mainGrid.add(R6C7);

    mainGrid.add(R5C1);
    mainGrid.add(R5C2);
    mainGrid.add(R5C3);
    mainGrid.add(R5C4);
    mainGrid.add(R5C5);
    mainGrid.add(R5C6);
    mainGrid.add(R5C7);

    mainGrid.add(R4C1);
    mainGrid.add(R4C2);
    mainGrid.add(R4C3);
    mainGrid.add(R4C4);
    mainGrid.add(R4C5);
    mainGrid.add(R4C6);
    mainGrid.add(R4C7);

    mainGrid.add(R3C1);
    mainGrid.add(R3C2);
    mainGrid.add(R3C3);
    mainGrid.add(R3C4);
    mainGrid.add(R3C5);
    mainGrid.add(R3C6);
    mainGrid.add(R3C7);

    mainGrid.add(R2C1);
    mainGrid.add(R2C2);
    mainGrid.add(R2C3);
    mainGrid.add(R2C4);
    mainGrid.add(R2C5);
    mainGrid.add(R2C6);
    mainGrid.add(R2C7);

    mainGrid.add(R1C1);
    mainGrid.add(R1C2);
    mainGrid.add(R1C3);
    mainGrid.add(R1C4);
    mainGrid.add(R1C5);
    mainGrid.add(R1C6);
    mainGrid.add(R1C7);

    playerColor = 'N';
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
    if (obj == C1Button) {
      drop(0);
    }

    //button 2
    if (obj == C2Button) {
      drop(1);
    }

    //button 3
    if (obj == C3Button) {
      drop(2);
    }

    //button 4
    if (obj == C4Button) {
      drop(3);
    }

    //button 5
    if (obj == C5Button) {
      drop(4);
    }

    //button 6
    if (obj == C6Button) {
      drop(5);
    }

    //button 7
    if (obj == C7Button) {
      drop(6);
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

  private int chosenColumn = -1;
  private volatile boolean columnIsChosen = false;

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
    guiBoard[row][column].setIcon(CHARACTER_MAP.get(player));
    board[row][column] = player;
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
