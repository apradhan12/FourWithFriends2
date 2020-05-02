package messages;

public class GetDropColumnClientMessage {
  private int column;

  public GetDropColumnClientMessage(int column) {
    this.column = column;
  }

  public int getColumn() {
    return column;
  }
}
