public interface IClientController {

  void connectToServer(String host, int port);

  void dropToken(int column);
}