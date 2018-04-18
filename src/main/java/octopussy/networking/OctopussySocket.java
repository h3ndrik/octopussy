package octopussy.networking;

import java.net.*;
import java.io.*;

public class OctopussySocket {
  static protected int serverPort = 6000;

  public void listen() throws IOException {
    ServerSocket server = new ServerSocket(serverPort);
    System.out.println("Server gestartet");

    while (true) {
      Socket client = server.accept();
      new Thread(new ClientHandler(client)).start();
    }
  }

}
