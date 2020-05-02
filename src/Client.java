import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.*;

public class Client extends JFrame implements ActionListener, IClient {

  // create variables
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


  //items for menu
  private JMenuBar mBar;
  private JMenu mGame;
  private JMenuItem mConnect;
  private JMenuItem mHelp;
  private JMenuItem mExit;

  //icons
  private static final ImageIcon white = new ImageIcon("ClientAssets/white64.png");
  private static final ImageIcon blue = new ImageIcon("ClientAssets/blue64.png");
  private static final ImageIcon orange = new ImageIcon("ClientAssets/orange64.png");
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

    //Create the menu bar.
    mBar = new JMenuBar();

    //Build the file menu.
    mGame = new JMenu("Game");
    mBar.add(mGame);

    //make and add JMenuItems
    mConnect = new JMenuItem("Connect");
    mConnect.addActionListener(this);
    mGame.add(mConnect);

    mHelp = new JMenuItem("Help");
    mHelp.addActionListener(this);
    mGame.add(mHelp);

    mExit = new JMenuItem("Exit");
    mExit.addActionListener(this);
    mGame.add(mExit);

    //set menu bar
    setJMenuBar(mBar);

    //main grid
    mainGrid.setBackground(new Color(0, 0, 0));

    add(mainGrid, BorderLayout.CENTER);

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

  //ActionListener things
  public void actionPerformed(ActionEvent event) {
    //set object
    Object obj = event.getSource();
    serverInterface servI = new serverInterface();

    if (obj == mConnect) {
      String stPort = JOptionPane
          .showInputDialog(null, "Input Server Port \n Or click OK for default", "16789");
      int port = Integer.parseInt(stPort);
      String ip = JOptionPane
          .showInputDialog(null, "Input Server IP \n Or Click OK for default", "123.456.7.89");

      System.out.println("STPort " + stPort + " Port " + port + " IP " + ip);

      try {
        servI.socketConnect(ip, port);
      } catch (UnknownHostException unh) {
        unh.printStackTrace();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
    if (obj == mExit) {
      servI.socketDisconnect();
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
    frame.playSound();

    // 'O', 'B'
    frame.setPlayerColor('O');
  }

  private int chosenColumn = -1;
  private volatile boolean columnIsChosen = false;
  private char playerTurn = 'N';

  //methods
  @Override
  public void setPlayerColor(char _c) {
    playerColor = _c;
  }

  public void playSound() {
    try {
      AudioInputStream audioInputStream = AudioSystem
          .getAudioInputStream(new File("/ClientAssets/theme.wav").getAbsoluteFile());
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();
    } catch (Exception ex) {
      System.out.println("Error with playing sound.");
      ex.printStackTrace();
    }
  }

  public void playSoundEffect() {
    try {
      AudioInputStream audioInputStream = AudioSystem
          .getAudioInputStream(new File("ClientAssets/drop.wav").getAbsoluteFile());
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();
    } catch (Exception ex) {
      System.out.println("Error with playing sound.");
      ex.printStackTrace();
    }
  }

  void drop(int column) {
    chosenColumn = column;
    columnIsChosen = true;
  }

  @Override
  public int getDropColumn() {
    columnIsChosen = false;
    while (!columnIsChosen) {
      try {
        wait();
      } catch (InterruptedException ignored) {}
    }
    return chosenColumn;
  }

  @Override
  public void setPlayerTurn(char player) {
    playerTurn = player;
    // show in GUI
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
    // show in GUI
  }

  static class serverInterface {

    public Socket soc;

    public void socketConnect(String pasIP, int pasPT) throws UnknownHostException, IOException {
      soc = new Socket(pasIP, pasPT);
    }

    public void socketDisconnect() {
      try {
        soc.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    public void socketSend(Object obj) {
      try {
        BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
        pw.println(obj);
        pw.flush();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

    }
  }

}
