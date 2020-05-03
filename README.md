# FourWithFriends


FourWithFriends is a Java-based multiplayer Connect 4 game that you can play over the Internet with your friends.

This project was originally created for Rochester Institute of Technology's ISTE-121 "Computer Problem Solving in the Information Domain II" course as a group project.
I rewrote the server code and added client code so that two clients may connect to a server and play a complete game.

Note that you must have Java 11 or higher to run this project.

To run the server:
```bash
java -jar FourWithFriendsServer.jar
```

To run the client:
```bash
java -jar FourWithFriendsClient.jar
```

The server accepts exactly two clients and runs one game. To connect to the
server, run the client, then click "Game" and then "Connect" in the menu.
Follow the prompts for host and port number on the screen. You will need
port forwarding enabled on the server computer in order to join a game
outside of your local area network. To play additional games, you must
restart the server and reconnect both of the clients, following the
same procedure as in the initial setup.
