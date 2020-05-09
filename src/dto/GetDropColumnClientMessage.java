package dto;

import java.io.Serializable;

public class GetDropColumnClientMessage implements Serializable {

  private int column;

  public GetDropColumnClientMessage(int column) {
    this.column = column;
  }

  public int getColumn() {
    return column;
  }
}
