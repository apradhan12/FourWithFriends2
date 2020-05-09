import dto.PlayerColor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * My Friend Aaron helped me with the methods to convert text to connect4 boards
 */

import static org.junit.jupiter.api.Assertions.*;

class WinCheckerTest {

  private static final String baseDirectory = "./test/";

  private static List<String> getFileLines(String path) {
    try {
      File myObj = new File(path);
      Scanner myReader = new Scanner(myObj);

      List<String> lines = new ArrayList<>();
      while (myReader.hasNextLine()) {
        lines.add(myReader.nextLine());
      }
      myReader.close();
      return lines;
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
      return null;
    }
  }

  private static PlayerColor convert(char character) {
    if (character == 'R') {
      return PlayerColor.Orange;
    } else if (character == 'Y') {
      return PlayerColor.Blue;
    } else if (character == 'N') {
      return PlayerColor.None;
    } else {
      throw new IllegalArgumentException(character + " is not a valid character");
    }
  }

  private static PlayerColor[][] getBoard(List<String> lines) {
    PlayerColor[][] board = new PlayerColor[7][6];
    for (int rowNum = 0; rowNum < lines.size(); rowNum++) {
      for (int colNum = 0; colNum < lines.get(0).length(); colNum++) {
        board[colNum][5 - rowNum] = convert(lines.get(rowNum).charAt(colNum));
      }
    }
    return board;
  }

  @Test
  public void testGetPlayerColor() {
    PlayerColor[][] board = getBoard(getFileLines(baseDirectory + "TestBoard.txt"));
    assertEquals(PlayerColor.Blue, WinChecker.getPlayerColor(board, 6));
    assertEquals(PlayerColor.Orange, WinChecker.getPlayerColor(board, 0));
    assertEquals(PlayerColor.Blue, WinChecker.getPlayerColor(board, 4));
    assertEquals(PlayerColor.Orange, WinChecker.getPlayerColor(board, 5));
  }

  @Test
  public void testCheckRow() {
    PlayerColor[][] board = getBoard(getFileLines(baseDirectory + "TestBoard1.txt"));
    assertEquals(true, WinChecker.checkRow(board, 3, PlayerColor.Orange));
    assertEquals(false, WinChecker.checkRow(board, 6, PlayerColor.Blue));
    assertEquals(true, WinChecker.checkRow(board, 0, PlayerColor.Orange));
    assertEquals(true, WinChecker.checkRow(board, 2, PlayerColor.Orange));
    board = getBoard(getFileLines(baseDirectory + "TestBoard5.txt"));
    assertEquals(false, WinChecker.checkRow(board, 2, PlayerColor.Blue));
    assertEquals(false, WinChecker.checkRow(board, 6, PlayerColor.Orange));
  }

  @Test
  public void testCheckColumn() {
    PlayerColor[][] board = getBoard(getFileLines(baseDirectory + "TestBoard.txt"));
    assertEquals(false, WinChecker.checkColumn(board, 6, PlayerColor.Blue));
    assertEquals(false, WinChecker.checkColumn(board, 0, PlayerColor.Orange));
    assertEquals(false, WinChecker.checkColumn(board, 2, PlayerColor.Orange));
    board = getBoard(getFileLines(baseDirectory + "TestBoard6.txt"));
    assertEquals(true, WinChecker.checkColumn(board, 6, PlayerColor.Blue));
  }

  @Test
  public void testCheckDiagonals() {
    PlayerColor[][] board = getBoard(getFileLines(baseDirectory + "TestBoard4.txt"));
    assertEquals(true, WinChecker.checkDiagonals(board, 3, PlayerColor.Orange));
    assertEquals(true, WinChecker.checkDiagonals(board, 2, PlayerColor.Orange));
    board = getBoard(getFileLines(baseDirectory + "TestBoard3.txt"));
    assertEquals(true, WinChecker.checkDiagonals(board, 3, PlayerColor.Blue));
    assertEquals(true, WinChecker.checkDiagonals(board, 6, PlayerColor.Blue));
    board = getBoard(getFileLines(baseDirectory + "TestBoard.txt"));
    assertEquals(false, WinChecker.checkDiagonals(board, 1, PlayerColor.Orange));
    assertEquals(false, WinChecker.checkDiagonals(board, 5, PlayerColor.Orange));
  }

  @Test
  public void testConnect4WinChecker() {
    PlayerColor[][] board = getBoard(getFileLines(baseDirectory + "TestBoard.txt"));
    assertEquals(false, WinChecker.checkWin(board, 3));
    board = getBoard(getFileLines(baseDirectory + "TestBoard1.txt"));
    assertEquals(true, WinChecker.checkWin(board, 0));
    board = getBoard(getFileLines(baseDirectory + "TestBoard2.txt"));
    assertEquals(false, WinChecker.checkWin(board, 6));
    board = getBoard(getFileLines(baseDirectory + "TestBoard3.txt"));
    assertEquals(true, WinChecker.checkWin(board, 3));
    try {
      WinChecker.checkWin(board, 0);
      fail();
    } catch (IllegalArgumentException iae) {
      assertEquals("The given column 0 has no tokens",
          iae.getMessage());
    }
    board = getBoard(getFileLines(baseDirectory + "TestBoard4.txt"));
    assertEquals(true, WinChecker.checkWin(board, 3));
    board = getBoard(getFileLines(baseDirectory + "TestBoard5.txt"));
    assertEquals(false, WinChecker.checkWin(board, 1));
    board = getBoard(getFileLines(baseDirectory + "TestBoard6.txt"));
    assertEquals(true, WinChecker.checkWin(board, 6));
    assertEquals(false, WinChecker.checkWin(board, 5));
    assertEquals(false, WinChecker.checkWin(board, 2));
  }
}