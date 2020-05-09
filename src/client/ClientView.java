package client;

import dto.ConnectionState;
import dto.PlayerColor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import static client.ClientModel.NUM_ROWS;
import static client.ClientModel.NUM_COLUMNS;

/**
 * Client code for FourWithFriends.
 */
public class ClientView extends JFrame implements ActionListener, IClientView {

  private PlayerColor playerColor;
  private ConnectionState connectionState;
  private final IClientModel model;
  private final IClientController controller;

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
      white = new ImageIcon(ImageIO.read(ClientView.class.getResource("/white64.png")));
      blue = new ImageIcon(ImageIO.read(ClientView.class.getResource("/blue64.png")));
      orange = new ImageIcon(ImageIO.read(ClientView.class.getResource("/orange64.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static final Map<PlayerColor, ImageIcon> CHARACTER_MAP = Map.of(PlayerColor.Blue, blue,
      PlayerColor.Orange, orange, PlayerColor.None, white);

  JButton[] columnButtons = new JButton[NUM_COLUMNS];
  JLabel[][] guiBoard = new JLabel[NUM_COLUMNS][NUM_ROWS];

  public ClientView(IClientModel model, IClientController controller) {
    containerPanel.setLayout(new BorderLayout());
    containerPanel.add(status, BorderLayout.NORTH);
    status.setHorizontalAlignment(SwingConstants.LEFT);
    setJMenuBar(createMenuBar());
    mainGrid.setBackground(Color.BLACK);
    containerPanel.add(mainGrid, BorderLayout.CENTER);
    add(containerPanel);

    // add objects to main grid
    initializeColumnButtons();
    initializeGridCells();

    playerColor = PlayerColor.None;

    this.model = model;
    this.controller = controller;
  }

  @Override
  public void render() {
    setTitle("FourWithFriends");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(700, 600);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
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

  public void actionPerformed(ActionEvent event) {
    System.out.println("An action was performed");

    Object obj = event.getSource();

    if (obj == mConnect) {
      String stPort = JOptionPane
          .showInputDialog(null, "Input Server Port \n Or click OK for default", "16789");
      int port = Integer.parseInt(stPort);
      String ip = JOptionPane
          .showInputDialog(null, "Input Server IP \n Or Click OK for default", "localhost");
      controller.connectToServer(ip, port);
    }
    if (obj == mExit) {
      System.exit(0);
    }
    for (int col = 0; col < NUM_COLUMNS; col++) {
      if (obj == columnButtons[col]) {
        controller.dropToken(col);
      }
    }
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

  @Override
  public void updateBoardState() {
    PlayerColor[][] board = model.getBoard();
    for (int col = 0; col < NUM_COLUMNS; col++) {
      for (int row = 0; row < NUM_ROWS; row++) {
        guiBoard[col][row].setIcon(CHARACTER_MAP.get(board[col][row]));
      }
    }
    System.out.println("Here is Board: " + Arrays.deepToString(board));
    playSoundEffect();
  }

  @Override
  public void updatePlayerColor() {
    playerColor = model.getPlayerColor();
  }

  @Override
  public void updateConnectionState() {
    connectionState = model.getConnectionState();
  }

  @Override
  public void updateGameStatus() {
    switch (model.getGameStatus()) {
      case PlayerTurn:
        status.setText("It's your turn");
        break;
      case OpponentTurn:
        status.setText("It's the opponent's turn");
        break;
      case Win:
        status.setText("You won!");
        break;
      case Loss:
        status.setText("You lost.");
        break;
      case Draw:
        status.setText("It's a tie.");
        break;
    }
  }
}
