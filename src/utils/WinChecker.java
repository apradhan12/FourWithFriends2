package utils;

import dto.PlayerColor;
import java.util.*;

import static client.ClientModel.NUM_ROWS;
import static client.ClientModel.NUM_COLUMNS;

/**
 * A class to determine if a player has won on a given connect4 board state
 *
 * @author Andrew Korchowsky
 * @version 4/25/2020
 */
public class WinChecker {

  //Attributes
  private static final int WINNING_LENGTH = 4;

  /**
   * A method to determine whether a piece placed ino a connect4 board forms a continuous line of 4
   * pieces in a row horizontally, vertically, or diagonally
   */
  public static boolean checkWin(PlayerColor[][] board, int column) {
    if (getRowNum(board, column) == -1) {
      throw new IllegalArgumentException(
          String.format("The given column %d has no tokens", column));
    }
    PlayerColor playerColor = getPlayerColor(board, column);
    return (checkColumn(board, column, playerColor) ||
        checkRow(board, column, playerColor) ||
        checkDiagonals(board, column, playerColor));
  }

  /**
   * A method to determine the color of the last player to make a move's piece in a game of connect4
   * given the board state after that player's move and the column which that player placed their
   * piece into
   *
   * @return a character representing the color of the player's piece (Y or R)
   */
  public static PlayerColor getPlayerColor(PlayerColor[][] board, int column) {
    int rowNum = getRowNum(board, column);
    return board[column][rowNum];
  }

  /**
   * A method to determine the whether the last player to make a move has connected 4 pieces of the
   * same color in the row that they placed their last piece into given the board state after that
   * player's move, the column which that player placed their piece into, and that player's piece
   * color
   *
   * @return a boolean representing if the player has won
   */
  public static boolean checkRow(PlayerColor[][] board, int column, PlayerColor playerColor) {
    int rowNum = getRowNum(board, column);
    PlayerColor[] row = new PlayerColor[NUM_ROWS];
    for (int i = 0; i < NUM_ROWS; i++) {
      row[i] = board[i][rowNum];
    }
    return checkLine(row, playerColor);
  }

  /**
   * A method to determine the row of a connect4 board piece in a given column
   *
   * @return the index of the row with the highest piece
   */
  public static int getRowNum(PlayerColor[][] board, int column) {
    int rowNum = 0;
    PlayerColor nextChar = board[column][rowNum];
    if (nextChar == PlayerColor.None) {
      return -1;
    }
    while (rowNum < (NUM_COLUMNS - 1) && nextChar != PlayerColor.None) {
      nextChar = board[column][rowNum + 1];
      rowNum += 1;
    }
    if (nextChar == PlayerColor.None) {
      return rowNum - 1;
    } else if (rowNum == (NUM_COLUMNS - 1)) {
      return rowNum;
    } else {
      return rowNum - 1;
    }
  }

  /**
   * A method to determine the whether the last player to make a move has connected 4 pieces of the
   * same color in the column that they placed their last piece into given the board state after
   * that player's move, the column which that player placed their piece into, and that player's
   * piece color
   *
   * @return a boolean representing if the player has won
   */
  public static boolean checkColumn(PlayerColor[][] board, int column, PlayerColor playerColor) {
    return checkLine(board[column], playerColor);
  }

  /**
   * A method to determine if a given character array has a streak of 4 or more of the given
   * player's color
   */
  public static boolean checkLine(PlayerColor[] line, PlayerColor playerColor) {
    int streak = 0;
    for (PlayerColor c : line) {
      if (c == playerColor) {
        streak += 1;
      } else {
        streak = 0;
      }
      if (streak >= WINNING_LENGTH) {
        return true;
      }
    }
    return false;
  }

  /**
   * A method to determine the whether the last player to make a move has connected 4 pieces of the
   * same color in any diagonals intersecting the location that they placed their last piece into
   * given the board state after that player's move, the column which that player placed their piece
   * into, and that player's piece color
   *
   * @return a boolean representing if the player has won
   */
  public static boolean checkDiagonals(PlayerColor[][] board, int column, PlayerColor playerColor) {
    PlayerColor[] positiveSlope = getDiagonal(board, column, true);
    PlayerColor[] negativeSlope = getDiagonal(board, column, false);
    return (checkLine(positiveSlope, playerColor) || checkLine(negativeSlope, playerColor));
  }

  /**
   * Return a character array of all the characters in the diagonal containing the top piece of a
   * given column on a connect4 board, given whether that diagonal has a positive or negative slope
   */
  public static PlayerColor[] getDiagonal(PlayerColor[][] board, int column, boolean positiveSlope) {
    List<PlayerColor> diagonalArrayList = new ArrayList<>();
    int[] diagonalStart = getDiagonalStart(board, column, positiveSlope);
    int rowIndex = diagonalStart[1];
    int columnIndex = diagonalStart[0];
    while (columnIndex < NUM_ROWS && rowIndex < NUM_COLUMNS && rowIndex >= 0) {
      diagonalArrayList.add(board[columnIndex][rowIndex]);
      if (positiveSlope) {
        rowIndex += 1;
      } else {
        rowIndex -= 1;
      }
      columnIndex += 1;
    }
    PlayerColor[] diagonal = new PlayerColor[diagonalArrayList.size()];
    for (int i = 0; i < diagonal.length; i++) {
      diagonal[i] = diagonalArrayList.get(i);
    }
    return diagonal;
  }

  /**
   * A method to determine the starting point for a diagonal on a connect4 board given the column
   * whose top piece that diagonal would have to pass through. The starting point will be in the top
   * left if the given boolean positiveSlope is false or bottom left if the given boolean
   * positiveSlope is true.
   *
   * @return an integer array with the first integer being the column number of the diagonal's
   * starting point and the second integer representing the row number of the starting point.
   */
  public static int[] getDiagonalStart(PlayerColor[][] board, int column, boolean positiveSlope) {
    int rowIndex = getRowNum(board, column);
    int columnIndex = column;
    while (columnIndex >= 0 && rowIndex >= 0 && rowIndex < NUM_COLUMNS) {
      if (positiveSlope) {
        rowIndex -= 1;
      } else {
        rowIndex += 1;
      }
      columnIndex -= 1;
    }
    if (positiveSlope) {
      rowIndex += 1;
    } else {
      rowIndex -= 1;
    }
    columnIndex += 1;
    return new int[]{columnIndex, rowIndex};
  }
}