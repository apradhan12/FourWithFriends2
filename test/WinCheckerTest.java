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

  private static char[][] getBoard(List<String> lines) {
    char[][] board = new char[7][6];
    for (int rowNum = 0; rowNum < lines.size(); rowNum++) {
      for (int colNum = 0; colNum < lines.get(0).length(); colNum++) {
        board[colNum][5 - rowNum] = lines.get(rowNum).charAt(colNum);
      }
    }
    return board;
  }

  @Test
  public void testGetPlayerColor() {
    char[][] board = getBoard(getFileLines(baseDirectory + "TestBoard.txt"));
    assertEquals('Y', WinChecker.getPlayerColor(board, 6));
    assertEquals('R', WinChecker.getPlayerColor(board, 0));
    assertEquals('Y', WinChecker.getPlayerColor(board, 4));
    assertEquals('R', WinChecker.getPlayerColor(board, 5));
  }

  @Test
  public void testCheckRow() {
    char[][] board = getBoard(getFileLines(baseDirectory + "TestBoard1.txt"));
    assertEquals(true, WinChecker.checkRow(board, 3, 'R'));
    assertEquals(false, WinChecker.checkRow(board, 6, 'Y'));
    assertEquals(true, WinChecker.checkRow(board, 0, 'R'));
    assertEquals(true, WinChecker.checkRow(board, 2, 'R'));
    board = getBoard(getFileLines(baseDirectory + "TestBoard5.txt"));
    assertEquals(false, WinChecker.checkRow(board, 2, 'Y'));
    assertEquals(false, WinChecker.checkRow(board, 6, 'R'));
  }

  @Test
  public void testCheckColumn() {
    char[][] board = getBoard(getFileLines(baseDirectory + "TestBoard.txt"));
    assertEquals(false, WinChecker.checkColumn(board, 6, 'Y'));
    assertEquals(false, WinChecker.checkColumn(board, 0, 'R'));
    assertEquals(false, WinChecker.checkColumn(board, 2, 'R'));
    board = getBoard(getFileLines(baseDirectory + "TestBoard6.txt"));
    assertEquals(true, WinChecker.checkColumn(board, 6, 'Y'));
  }

  @Test
  public void testCheckDiagonals() {
    char[][] board = getBoard(getFileLines(baseDirectory + "TestBoard4.txt"));
    assertEquals(true, WinChecker.checkDiagonals(board, 3, 'R'));
    assertEquals(true, WinChecker.checkDiagonals(board, 2, 'R'));
    board = getBoard(getFileLines(baseDirectory + "TestBoard3.txt"));
    assertEquals(true, WinChecker.checkDiagonals(board, 3, 'Y'));
    assertEquals(true, WinChecker.checkDiagonals(board, 6, 'Y'));
    board = getBoard(getFileLines(baseDirectory + "TestBoard.txt"));
    assertEquals(false, WinChecker.checkDiagonals(board, 1, 'R'));
    assertEquals(false, WinChecker.checkDiagonals(board, 5, 'R'));
  }

  @Test
  public void testConnect4WinChecker() {
    char[][] board = getBoard(getFileLines(baseDirectory + "TestBoard.txt"));
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